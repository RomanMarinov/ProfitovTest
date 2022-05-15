package com.dev_marinov.profitovtest

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.*
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class FragmentList : Fragment() {

    // api youtube AIzaSyDXkOHnFiyC1-zcvQZMSuO1QmOAhbGGAaE использовать с параметром как key=API_KEY
    val urlNumbers: String = "https://demo7877231.mockable.io/api/v1/hot"
    val urlTypes = "http://demo7877231.mockable.io/api/v1/post/"
    lateinit var recyclerView: RecyclerView
    lateinit var adapterList: AdapterList
    lateinit var tvText: TextView
    lateinit var cardViewFragList: CardView
    lateinit var webView: WebView
    lateinit var youTubePlayerView: YouTubePlayerView
    var temp: Int = -1
    var temp2: Int = 0
    var numString = ""
    var flagPause: Boolean = false
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View

        view = layoutInflater.inflate(R.layout.fragment_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        tvText = view.findViewById(R.id.tvText)
        webView = view.findViewById(R.id.webView)
        cardViewFragList = view.findViewById(R.id.cardViewFragList)
        youTubePlayerView = view.findViewById(R.id.webViewPlayer)
        lifecycle.addObserver(youTubePlayerView)

        if((activity as MainActivity).arrayListNumbers.size == 0) { // если массив пуст, запросить данные
            getNumber()
        }

        linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager

        val snapHelper: SnapHelper = PagerSnapHelper() // переключение на всю ширину экрана
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.scrollToPosition(Integer.MAX_VALUE / 2) // скрол в соответ. с адаптером

            // интерфейс для обновления адаптера
        (activity as MainActivity).setInterFaceUpdateAdapter(object : MainActivity.InterFaceUpdateAdapter{
            override fun myInterFaceUpdateAdapter() {
                adapterList = AdapterList(requireActivity(), (activity as MainActivity).arrayListNumbers)
                recyclerView.adapter = adapterList

            }
        })

        // интерфейс срабатывающий при клике стрелок лево право из адаптера
        (activity as MainActivity).setInterFaceAdapter(object : MainActivity.InterFaceAdapter {
            override fun myInterFaceAdapter(leftOrRight: String) {
                if (leftOrRight.equals("left")) {
                    if (linearLayoutManager.findFirstVisibleItemPosition() > 0) {
                        recyclerView.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1)
                    } else {
                        recyclerView.smoothScrollToPosition(0)
                    }
                }
                else {
                    recyclerView.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1)
                }
            }
        })

        // слушатель для работы с копией arrayListNumbersCopy, чтобы получать position
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val pos = linearLayoutManager.findFirstVisibleItemPosition()
                if (pos > temp) {
                    temp = pos
                    temp2 = temp % (activity as MainActivity).arrayListNumbers.size
                    for (item in 0 until (activity as MainActivity).arrayListNumbersCopy.size) {
                        if (item == temp2) {
                            numString = (activity as MainActivity).arrayListNumbersCopy[temp2]
                            getTypes(numString)
                            break
                        }
                    }
                }
                if (pos < temp) {
                    temp = pos
                    temp2 = temp % (activity as MainActivity).arrayListNumbers.size
                    for (item in 0 until (activity as MainActivity).arrayListNumbersCopy.size) {
                        if (item == temp2) {
                            numString = (activity as MainActivity).arrayListNumbersCopy[temp2]
                            getTypes(numString)
                            break
                        }
                    }

                }
            }
        })

        return view
    }


    fun getNumber(){ // функция запрашивает данные номеров

        val asyncHttpClient = AsyncHttpClient()
        asyncHttpClient.get(urlNumbers, null, object : TextHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                Log.e("333", "-onSuccess-$responseString")

                try {
                    if(responseString != null) {
                        val jsonArray = JSONArray(responseString)
                        for (item in 0 until jsonArray.length()) {
                            (activity as MainActivity).arrayListNumbers.add(jsonArray[item].toString()).toString()
                            (activity as MainActivity).arrayListNumbersCopy.add(jsonArray[item].toString()).toString()
                        }
                        // команда обновить адаптер
                        MainActivity.interFaceUpdateAdapter.myInterFaceUpdateAdapter()
                    }
                } catch (e: JSONException) {
                    Log.e("333", "-try catch-$e")
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.e("333", "-onFailure-$responseString")
            }
        })
    }

    // метод получения type по number
    fun getTypes(number: String) {
        Log.e("333", "-getTypes пришло number=" + number)

        val asyncHttpClient = AsyncHttpClient()
        asyncHttpClient.get(urlTypes + number, null, object : TextHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                Log.e("333", "-onSuccess FragmentShow-$responseString")

                try {
                    val jsonObject = JSONObject(responseString)
                    val type = jsonObject.getString("type")

                    if (type.equals("text")){
                        val text = jsonObject.getJSONObject("payload").getString("text")

                        startText(text)
                        try {
                            // интерфейс для остановки видео
                            MainActivity.interFaceStop.myInterFaceStop("stop")
                        }
                        catch (e:Exception){
                            Log.e("333", "-try catch-$e")
                        }
                    }

                    if (type.equals("webpage")) {
                        val url = jsonObject.getJSONObject("payload").getString("url")

                        if (url.contains("youtube")){
                            Log.e("333", "-onSuccess youtube")
                            val idVideo = url.substring(url.length - 11)
                                startYouTubePlayer(idVideo)

                        } else {
                            startSite(url)
                            try {
                                // интерфейс для остановки видео
                                MainActivity.interFaceStop.myInterFaceStop("stop")
                            }
                            catch (e:Exception){
                                Log.e("333", "-try catch-$e")
                            }
                        }
                    }
                } catch (e: JSONException) {
                    Log.e("333", "-try catch FragmentShow-$e")
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.e("333", "-onFailure FragmentShow-$responseString")
            }
        })
    }

    // метод показа textView
    fun startText(text: String) {
        flagPause = false
        (activity as MainActivity).runOnUiThread {
            cardViewFragList.visibility = View.VISIBLE
            webView.visibility = View.GONE
            youTubePlayerView.visibility = View.GONE

            tvText.text = text
        }
    }

    // метод показа webView
    fun startSite(url: String) {
        flagPause = false
        (activity as MainActivity).runOnUiThread {
            cardViewFragList.visibility = View.GONE
            youTubePlayerView.visibility = View.GONE
            webView.visibility = View.VISIBLE
            webView.loadUrl(url)
        }
    }

    // метод показа youTube
    fun startYouTubePlayer(idVideo: String) {
        (activity as MainActivity).runOnUiThread {
            cardViewFragList.visibility = View.GONE
            webView.visibility = View.GONE
            youTubePlayerView.visibility = View.VISIBLE
        }

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
               // myYouTubePlayer = youTubePlayer
                Log.e("333", "-youTubePlayer-$youTubePlayer")
                    if(!flagPause) {
                        youTubePlayer.loadVideo(idVideo, 0f)
                        flagPause = true
                    }
                    if(flagPause) {
                            // интерфейс для остановки видео
                        (activity as MainActivity).setInterFaceStop(object :MainActivity.InterFaceStop {
                            override fun myInterFaceStop(type: String) {
                                youTubePlayer.pause()
                            }
                        })
                    }
            }
        })

    }

}