package com.mobileapp.covid19app.ui.main

//import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobileapp.covid19app.R
import android.widget.Button
import androidx.navigation.findNavController

class MainFragment : Fragment() {

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)

        //use the Fragment Layout File
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        val reportButton: Button? = view?.findViewById(R.id.reportButton)
        val symptopmButton: Button? = view?.findViewById(R.id.symptopmButton)
        val preventionButton: Button? = view?.findViewById(R.id.preventionButton)

        reportButton?.setOnClickListener { v: View -> onButtonReportButtonClick(v) }
        symptopmButton?.setOnClickListener { v: View -> onButtonSymptopmButtonClick(v) }
        preventionButton?.setOnClickListener { v: View -> onButtonPreventionButtonClick(v) }

        return view
    }

    //navigatation to report
    private fun onButtonReportButtonClick(view: View) {
        view.findNavController().navigate(R.id.mainToReport)
    }
    //To sysmptoms
    private fun onButtonSymptopmButtonClick(view: View) {
        view.findNavController().navigate(R.id.mainToSymptoms)
    }
    // To prevention
    private fun onButtonPreventionButtonClick(view: View) {
        view.findNavController().navigate(R.id.mainToPrevention)
    }
}
//    companion object {
//        fun newInstance() = MainFragment()
//    }
//
//    private lateinit var viewModel: MainViewModel
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View {
//        return inflater.inflate(R.layout.main_fragment, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

