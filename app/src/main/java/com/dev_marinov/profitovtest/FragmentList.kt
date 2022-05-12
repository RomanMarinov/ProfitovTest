package com.dev_marinov.profitovtest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException


class FragmentList : Fragment() {

    val url: String = "https://demo7877231.mockable.io/api/v1/hot"
    lateinit var recyclerView: RecyclerView
    lateinit var  adapterList: AdapterList

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view: View



        view = inflater.inflate(R.layout.fragment_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        adapterList = AdapterList(requireActivity(), (activity as MainActivity).arrayList)
        recyclerView.adapter = adapterList

        getData()

        return view;

    }







    fun getData(){

        val asyncHttpClient = AsyncHttpClient()
        asyncHttpClient.get(url, null, object : TextHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                Log.e("333", "-onSuccess-$responseString")

                try {

                    if(responseString != null) {

                        val jsonArray = JSONArray(responseString)

                    for (item in 0 until jsonArray.length()) {
                        (activity as MainActivity).arrayList.add(jsonArray[item].toString()).toString()
                    }

                        (activity as MainActivity).runOnUiThread {

                        adapterList.notifyDataSetChanged()
                        }


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

}