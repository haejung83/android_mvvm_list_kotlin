package com.haejung.template.drones

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.haejung.template.data.Drone

object DronesListBindings {

    @BindingAdapter("app:items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: List<Drone>) {
        with(recyclerView.adapter as DronesAdapter) {
            this.items = items
        }
    }

}