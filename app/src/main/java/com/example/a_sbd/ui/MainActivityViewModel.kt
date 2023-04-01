package com.example.a_sbd.ui

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.a_sbd.R
import com.example.a_sbd.domain.usecases.SetBleConnectionUseCase
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    application: Application,
    private val setBleConnectionUseCase: SetBleConnectionUseCase
    /*private val bluetoothLeScanner: BluetoothLeScanner,
    private val scanSettings: ScanSettings,
    private val bleScanCallback: BleScanCallback*/
) : ViewModel() {

    private val context = application
    private val _appState = MutableLiveData<AppState>()
    val appState: LiveData<AppState>
        get() = _appState

    fun handleUiMode(uiMode: Int, bundle: Bundle) {
        when(uiMode) {
            MainActivity.SINGLE_CHAT_MODE -> {
                _appState.value = AppState().copy(
                    isNavBarVisible = false,
                    isAppbarLogoVisible = false,
                    title = bundle.getString("title") ?: "Ass"
                )
            }
            MainActivity.CHAT_CONTACTS_MODE -> {
                _appState.value = AppState().copy(
                    isNavBarVisible = true,
                    isAppbarLogoVisible = true,
                    title = context.getString(R.string.app_name)
                )
            }
        }
    }


    private val _deviceAddress = MutableLiveData<String>()
    val deviceAddress: LiveData<String>
        get() = _deviceAddress

    private val _devices = MutableLiveData<List<BluetoothDevice>>()
    val devices: LiveData<List<BluetoothDevice>>
        get() = _devices

    //private var isBleConnectedInFact = false  // observe this and set signal level
    private val _isBleConnectedInFact = MutableLiveData(false)
    val isBleConnectedInFact: LiveData<Boolean>
        get() = _isBleConnectedInFact

    fun handleBleConnection() {
        if (!isBleConnectedInFact.value!!) {
            _appState.value = AppState().copy(
                isBleConnectedIcon = true
            )
           // _isBleConnectedInFact = setBleConnectionUseCase()
        } else {

        }
    }
    private var scanResult: ScanResult? = null

    /*@SuppressLint("MissingPermission")
    fun startScan() {
        Log.d(LOG_TAG, "Permission granted. Start scan!")
        bluetoothLeScanner.startScan(null, scanSettings, bleScanCallback)
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        Log.d(LOG_TAG, "Stop scan!")
        bluetoothLeScanner.stopScan(bleScanCallback)
    }*/

    companion object {
        private const val LOG_TAG = "MainViewModel"
    }

    data class AppState(
        val isNavBarVisible: Boolean = true,
        val isAppbarLogoVisible: Boolean = true,
        val title: String = "",
        val signalLevel: Int = 0,
        val isBleConnectedIcon: Boolean = false
        )
}