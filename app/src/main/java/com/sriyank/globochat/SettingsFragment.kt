package com.sriyank.globochat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.*


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val dataStore = DataStore()
        // Enable PreferenceDataStore for entire hierarchy and disable the SharedPreference
//        preferenceManager.preferenceDataStore = dataStore

        val accSettingsPref = findPreference<Preference>(getString(R.string.key_account_settings))

        accSettingsPref?.setOnPreferenceClickListener {

            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_frag) as NavHostFragment
            val navController = navHostFragment.navController
            val action = SettingsFragmentDirections.actionSettingsToAccSettings()
            navController.navigate(action)

            true
        }

//        Read Preference value in a Fragment
//        Step1: Get reference to the SharedPreference (XML file)
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)

//        Step 2: Get the "value" using the "key"
//        if the user don't add the value then the default value is added, avoid null pointer exception
        val autoReplyTime = sharedPreference.getString(getString(R.string.key_auto_reply_time), "")
        Log.i("SettingsFragment", "Auto reply time: $autoReplyTime")

        val autoDownload = sharedPreference.getBoolean(getString(R.string.key_auto_download), false)
        Log.i("SettingsFragment", "Auto Download: $autoDownload")

//       track any update on preference value
        val statusPref = findPreference<EditTextPreference>(getString(R.string.key_status))
        statusPref?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->

                val newStatus = newValue as String

                if (newStatus.contains("sex")){
                    Toast.makeText(context, "Inappropriate status. Please maintain community guidelines.",
                    Toast.LENGTH_SHORT).show()

                    false

                }else {
                    true  // true: accept the new value. false: reject the new value.
                    //                the return value state that: onPreferenceChangeListener is executed before the
                    //                Preference value has changed in the SharedPreference file.
                }
            }

//        Dynamically update preference summery
        val notificationPref = findPreference<SwitchPreferenceCompat>(getString(R.string.key_new_msg_notif))
        notificationPref?.summaryProvider = Preference.SummaryProvider<SwitchPreferenceCompat> { switchPref ->

            if (switchPref?.isChecked!!)
                "Status: ON"
            else
                "Status: OFF"
        }

        notificationPref?.preferenceDataStore = dataStore
        val isNotIfEnabled = dataStore.getBoolean("key_new_msg_notify", false)
    }

//  implement the preference data store
//  step 1: Create a subclass of PreferenceDataStore
    class DataStore : PreferenceDataStore(){
        // Override method only as per your need
        // Do Not override method which you don't want to use
        // After overriding, remove the super call. (could throw UnsupportedOperationException)

//       using 'put' methods you can store data to a custom storage eg cloud, local database
//       using 'get' methods we can retrieve the values from the custom storage

    //  step 2: Enable the datastore


    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        if (key == "key_new_msg_notify") {
            // Retrieve value cloud or local DB
            Log.i("DataStore", "getBoolean executed for $key")
        }

        return defValue
    }

    override fun putBoolean(key: String?, value: Boolean) {

        if (key == "key_new_msg_notify") {
            // Save value to cloud or local DB
            Log.i("DataStore", "putBoolean executed for $key with new value: $value")
        }
    }

    }
}