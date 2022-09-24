package com.exponential_groth.sight_reading_training

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val sessionEndingPreference = findPreference<ListPreference>(getString(R.string.session_ending_key))

            if (sessionEndingPreference?.value == "1") {
                val pref = findPreference<EditTextPreference>(getString(R.string.num_of_rounds_key))
                if (pref != null) {
                    pref.isVisible = true
                }
            }

            sessionEndingPreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                if (newValue == "1") {
                    val preference1 = findPreference<EditTextPreference>(getString(R.string.num_of_rounds_key))
                    if (preference1 != null && !preference1.isVisible) {
                        preference1.isVisible = true
                    }
                } else {
                    val preference2 = findPreference<EditTextPreference>(getString(R.string.num_of_rounds_key))
                    if (preference2 != null && preference2.isVisible) {
                        preference2.isVisible = false
                    }
                }

                true
            }
        }
    }
}