package com.sriyank.globochat

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get NavHost and NavController
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        navController = navHostFrag.navController

        // Get AppBarConfiguration
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // Link ActionBar with NavController
        setupActionBarWithNavController(navController, appBarConfiguration)


        //        Step1: Get reference to the SharedPreference (XML file)
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)

//        Step 2: Get the "value" using the "key"
//        if the user don't add the value then the default value is added, avoid null pointer exception
        val autoReplyTime = sharedPreference.getString(getString(R.string.key_auto_reply_time), "")
        Log.i("MainActivity", "Auto reply time: $autoReplyTime")

//        get values on account setting
        val publicInfo: Set<String>? = sharedPreference.getStringSet(getString(R.string.key_public_info), null)
        Log.i("MainActivity", "Public Info: $publicInfo")
    }

//    enable the up navigation for our current nav bar
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    called only 'after' the preference value has changed
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.key_status)) {
            val  newStatus = sharedPreferences?.getString(key, "")
            Toast.makeText(this, "New status: $newStatus", Toast.LENGTH_SHORT).show()

        }

        if (key == getString(R.string.key_auto_reply)){
            val autoReply = sharedPreferences?.getBoolean(key, false)
            if (autoReply!!) {
                Toast.makeText(this, "Auto Reply: ON", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Auto Reply: OFF", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    register the listener withing this activity
    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
    }

//    unregister so as to avoid any memory leak
    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this)
    }
}
