package com.haejung.template.drones

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haejung.template.R
import com.haejung.template.util.obtainViewModel
import com.haejung.template.util.replaceFragmentInActivity

class DronesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drones)

        supportFragmentManager.findFragmentById(R.id.content)
                as DronesFragment? ?: DronesFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.content)
        }
    }

    fun obtainViewModel(): DronesViewModel = obtainViewModel(DronesViewModel::class.java)

}
