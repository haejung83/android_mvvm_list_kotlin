package com.haejung.template.drones


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.haejung.template.LiveEvent
import com.haejung.template.data.Drone
import com.haejung.template.databinding.FragmentDronesBinding
import com.haejung.template.details.DetailsActivity
import com.haejung.template.util.setupSnackBar
import com.orhanobut.logger.Logger


/**
 * A simple [Fragment] subclass.
 *
 */
class DronesFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentDronesBinding
    private lateinit var dronesAdapter: DronesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentDronesBinding.inflate(inflater, container, false).apply {
            droneViewModel = (activity as DronesActivity).obtainViewModel()
        }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackBar()
        setupRecyclerView()
        setupEvent()
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupSnackBar() {
        viewDataBinding.droneViewModel?.let { viewModel ->
            view?.setupSnackBar(this, viewModel.snackBarMessage, Snackbar.LENGTH_LONG)
        }
    }

    private fun setupRecyclerView() {
        viewDataBinding.droneViewModel?.let { viewModel ->
            dronesAdapter = DronesAdapter(viewModel)
            viewDataBinding.recyclerView.adapter = dronesAdapter
            viewDataBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupEvent() {
        viewDataBinding.droneViewModel?.let { viewModel ->
            // Make a binding for an event (Open Detail UI)
            viewModel.openDroneEvent.observe(this, Observer<LiveEvent<String>> { event ->
                event.getContentIfNotHandled()?.let { content ->
                    openDroneDetailsUI(content)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.droneViewModel?.start()
    }

    private fun openDroneDetailsUI(droneName: String) {
        Logger.d("showDroneDetailsUI: $droneName")
        val intent = Intent(context, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_DRONE_NAME, droneName)
        }
        startActivity(intent)
    }

    interface DroneItemListener {
        fun onDroneClick(clickedDrone: Drone)
    }

    companion object {
        fun newInstance() = DronesFragment()
    }

}
