package com.hk.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hk.ApiPresenter
import com.hk.R
import com.hk.library.BaseFragment
import kotlinx.android.synthetic.main.fragment_simple.*


/**
 * A simple [Fragment] subclass.
 *
 */
class SimpleFragment : BaseFragment() {
    val apiPresenter by lazy { ApiPresenter(this) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simple, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiPresenter.getInformationList(0)
        activity.supportFragmentManager
                .beginTransaction()
                .remove(this)
                .commit()
    }

    override fun showSuccess(`object`: Any, task: Int) {
        super.showSuccess(`object`, task)
        tv_rs.text = `object`.toString()
    }

}
