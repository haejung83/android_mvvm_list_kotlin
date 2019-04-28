package com.haejung.template.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.haejung.template.LiveEvent

fun View.showSnackBar(message: String, duration: Int) {
    Snackbar.make(this, message, duration).show()
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackBar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<LiveEvent<Int>>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            showSnackBar(context.getString(it), timeLength)
        }
    })
}
