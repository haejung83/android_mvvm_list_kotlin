package com.haejung.template.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.haejung.template.databinding.FragmentDetailsBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailsFragment(
    private val droneName: String
) : Fragment() {

    private lateinit var viewDataBinding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentDetailsBinding.inflate(LayoutInflater.from(context), container, false).apply {
            detailsViewModel = (activity as DetailsActivity).obtainViewModel()
        }
        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.detailsViewModel?.start(droneName)
    }

    companion object {
        fun newInstance(droneName: String) = DetailsFragment(droneName)
    }

}
