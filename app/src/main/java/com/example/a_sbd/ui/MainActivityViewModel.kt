package com.example.a_sbd.ui

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass.Device
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.a_sbd.R
import com.example.a_sbd.data.mapper.JsonConverter
import com.example.a_sbd.data.workers.commands.BLE_CONNECTION_ESTABLISHED
import com.example.a_sbd.data.workers.commands.SET_BLE_CONNECTION
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.domain.model.WorkBleConnectionResponse
import com.example.a_sbd.domain.usecases.ScanUseCase
import com.example.a_sbd.domain.usecases.SetBleConnectionUseCase
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    application: Application,
    private val scanUseCase: ScanUseCase,
    private val setBleConnectionUseCase: SetBleConnectionUseCase,
    private val jsonConverter: JsonConverter,
    //private val bluetoothLeScanner: BluetoothLeScanner,
    //private val scanSettings: ScanSettings,
) : ViewModel() {

    private val context = application
    private val _appState = MutableLiveData<AppState>()
    val appState: LiveData<AppState>
        get() = _appState

    //private lateinit var _scanBroadcastReceiver: ScanBroadcastReceiver

    private val _devicesSimple = MutableLiveData<List<DeviceSimple>>()
    val devicesSimple: LiveData<List<DeviceSimple>>
    get() = _devicesSimple

    //private val workManager = WorkManager.getInstance(context)

    //var scanService: ScanService? = null
    //set(value) {field = value}

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

    fun handleBleConnectionButton() {
        if (!isBleConnectedInFact.value!!) {
            _appState.value = AppState().copy(
                isBleConnectedIcon = true
            )

        } else {

        }
    }

    fun handleBleConnectionWorkResult(workInfos: MutableList<WorkInfo>) {
        Log.d(TAG, "workInfos size ${workInfos.size}")
        val workResultJson = workInfos[workInfos.lastIndex].outputData.getString(REPORT) ?: throw java.lang.RuntimeException("Response from worker is empty")
        val response = jsonConverter.fromJsonToSimpleDataClass(workResultJson, WorkBleConnectionResponse::class.java)

        if (response.commandWorker == SET_BLE_CONNECTION && response.responseWorker == BLE_CONNECTION_ESTABLISHED) {
            handleBleConnectionButton()
        } else {
            // todo log
        }

    }

    @SuppressLint("MissingPermission")
    fun startScan() {
        scanUseCase.invoke(true)
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        scanUseCase.invoke(false)
    }

    //private var isBleConnectedInFact = false  // observe this and set signal level
    private val _isBleConnectedInFact = MutableLiveData(false)
    val isBleConnectedInFact: LiveData<Boolean>
        get() = _isBleConnectedInFact

    /*private val _isScanningWorkStarted = MutableLiveData<Unit>()
    val isScanningWorkStarted: LiveData<Unit>
    get() = _isScanningWorkStarted*/

    @SuppressLint("MissingPermission")
    fun handleBleScanResult(results: List<ScanResult>?) {
        Log.d(TAG, "Handling scan results in view model...")
        _devicesSimple.value =
            results?.map { scanResult -> DeviceSimple(scanResult.device.name, scanResult.device.address) }
    }

    fun setConnection(devicePosition: Int) {
        Log.d(TAG, "Device is chosen")
        val devices = devicesSimple.value
            ?: throw java.lang.RuntimeException("List of devices can not be null whilst connection is being established")
        setBleConnectionUseCase(devices[devicePosition].address!!)
    }

    companion object {
        private const val LOG_TAG = "MainViewModel"

        const val SCAN_TAG = "scan"
        const val CONNECTION_TAG = "connection_tag"

        const val COMMAND = "worker_command"
        const val REPORT = "worker_report"

        //modem commands

    }

    data class AppState(
        val isNavBarVisible: Boolean = true,
        val isAppbarLogoVisible: Boolean = true,
        val title: String = "",
        val signalLevel: Int = 0,
        val isBleConnectedIcon: Boolean = false
        )
}