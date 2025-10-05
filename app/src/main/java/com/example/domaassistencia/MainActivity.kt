package com.example.domaassistencia

import android.content.Context
import android.content.Intent
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var audioManager: AudioManager
    private lateinit var audioHelper: AudioHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioHelper = AudioHelper(this)

        audioManager.registerAudioDeviceCallback(object : AudioDeviceCallback() {
            override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>?) {
                super.onAudioDevicesAdded(addedDevices)
                if (audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                    Log.d("AudioCallback", "Fone Bluetooth conectado.")
                }
            }

            override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
                super.onAudioDevicesRemoved(removedDevices)
                if (!audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                    Log.d("AudioCallback", "Fone Bluetooth desconectado.")
                }
            }
        }, null)

        val bluetoothButton: Button = findViewById(R.id.connect_bluetooth_button)
        bluetoothButton.setOnClickListener {
            val intent = with(Intent(Settings.ACTION_BLUETOOTH_SETTINGS)) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putExtra("EXTRA_CONNECTION_ONLY", true)
                putExtra("EXTRA_CLOSE_ON_CONNECT", true)
                putExtra("android.bluetooth.devicepicker.extra.FILTER_TYPE", 1)
            }
            startActivity(intent)
        }
    }
}