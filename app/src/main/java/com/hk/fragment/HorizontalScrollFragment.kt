package com.hk.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hk.base.R
import kotlinx.android.synthetic.main.fragment_horizontal_scroll.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HorizontalScrollFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_horizontal_scroll, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView
            .ViewHolder = object : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.layout_header_deal_record,
                    parent, false)){}

            override fun getItemCount(): Int = 20

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            }
        }
    }


}
