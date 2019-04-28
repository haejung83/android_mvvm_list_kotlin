package com.haejung.template.drones

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haejung.template.data.Drone
import com.haejung.template.databinding.ViewDroneItemBinding
import kotlin.properties.Delegates


class DronesAdapter(
    private val dronesViewModel: DronesViewModel
) : RecyclerView.Adapter<DronesAdapter.ViewHolder>() {

    var items: List<Drone> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    private var droneItemListener: DronesFragment.DroneItemListener = object : DronesFragment.DroneItemListener {
        override fun onDroneClick(clickedDrone: Drone) {
            dronesViewModel.openDrone(clickedDrone.name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewDroneItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], droneItemListener)
    }

    class ViewHolder(
        private val binding: ViewDroneItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(bindDrone: Drone, bindListener: DronesFragment.DroneItemListener) {
            with(binding) {
                this.drone = bindDrone
                this.listener = bindListener
                executePendingBindings()
            }
        }
    }

}