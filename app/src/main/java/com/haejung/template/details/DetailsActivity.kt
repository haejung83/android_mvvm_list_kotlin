package com.haejung.template.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haejung.template.R
import com.haejung.template.util.obtainViewModel
import com.haejung.template.util.replaceFragmentInActivity

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Get a drone name from intent
        val droneName = intent.getStringExtra(EXTRA_DRONE_NAME)

        // Create Fragment (View)
        supportFragmentManager.findFragmentById(R.id.content)
                as DetailsFragment? ?: DetailsFragment.newInstance(droneName).also {
            replaceFragmentInActivity(it, R.id.content)
        }
    }

    fun obtainViewModel() = obtainViewModel(DetailsViewModel::class.java)

    companion object {
        const val EXTRA_DRONE_NAME = "drone_name"
    }

}
