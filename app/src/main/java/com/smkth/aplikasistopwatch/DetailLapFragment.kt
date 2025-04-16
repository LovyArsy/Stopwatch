package com.smkth.aplikasistopwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailLapFragment : Fragment() {

    private lateinit var lapDetailTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_lap, container, false)
        lapDetailTextView = view.findViewById(R.id.lapDetailTextView)

        val lapDetails = arguments?.getStringArrayList(ARG_LAPS) ?: arrayListOf()
        lapDetailTextView.text = lapDetails.joinToString("\n")

        return view
    }

    companion object {
        private const val ARG_LAPS = "lap_details"

        fun newInstance(laps: ArrayList<String>): DetailLapFragment {
            val fragment = DetailLapFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_LAPS, laps)
            fragment.arguments = args
            return fragment
        }
    }
}
