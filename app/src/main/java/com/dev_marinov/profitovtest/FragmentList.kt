package com.dev_marinov.profitovtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.PagerSnapHelper
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import androidx.recyclerview.widget.SnapHelper









class FragmentList : Fragment() {

    val url: String = "https://demo7877231.mockable.io/api/v1/hot"
    lateinit var recyclerView: RecyclerView
    lateinit var  adapterList: AdapterList
    val radius = 16
    val dotsHeight = 20
    lateinit var img_LeftScroll: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    val view: View

        view = inflater.inflate(R.layout.fragment_list, container, false)
        getData1()


        recyclerView = view.findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager


        adapterList = AdapterList(requireActivity(), (activity as MainActivity).arrayList, recyclerView)
        recyclerView.adapter = adapterList



        //val recyclerIndicator: ScrollingPagerIndicator = view.findViewById(R.id.indicator)
        //recyclerIndicator.attachToRecyclerView(recyclerView)
        // переключает весь элемент на страницу при скроле
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)


        recyclerView.scrollToPosition(Integer.MAX_VALUE / 2)


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
                    recyclerView.smoothScrollToPosition(
                        linearLayoutManager.findLastVisibleItemPosition() + 1
                    )
                }
            }
        })




//        val myColor = ContextCompat.getColor(requireContext(), R.color.black)
//        recyclerView.addItemDecoration(
//            DotsIndicatorDecoration(
//                radius,
//                radius * 4,
//                dotsHeight,
//                myColor,
//                myColor
//            )
//        )
//
//        PagerSnapHelper().attachToRecyclerView(recyclerView)

        return view;

    }



        fun getData1(){
            (activity as MainActivity).arrayList.add("www1").toString()
            (activity as MainActivity).arrayList.add("www2").toString()
            (activity as MainActivity).arrayList.add("www3").toString()
        }

//
//
//    fun getData(){
//
//        val asyncHttpClient = AsyncHttpClient()
//        asyncHttpClient.get(url, null, object : TextHttpResponseHandler() {
//            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
//                Log.e("333", "-onSuccess-$responseString")
//
//                try {
//
//                    if(responseString != null) {
//
//                        val jsonArray = JSONArray(responseString)
//
//                    for (item in 0 until jsonArray.length()) {
//                        (activity as MainActivity).arrayList.add(jsonArray[item].toString()).toString()
//                    }
//
//
//
//                        (activity as MainActivity).runOnUiThread {
//
//                        adapterList.notifyDataSetChanged()
//                        }
//
//
//                    }
//
//
//                } catch (e: JSONException) {
//                    Log.e("333", "-try catch-$e")
//                }
//
//
//
//            }
//
//            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
//                Log.e("333", "-onFailure-$responseString")
//            }
//        })
//
//
//    }



}