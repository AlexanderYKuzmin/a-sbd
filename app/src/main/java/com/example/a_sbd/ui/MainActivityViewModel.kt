package com.example.a_sbd.ui

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.example.a_sbd.R
import com.example.a_sbd.data.mapper.JsonConverter
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.domain.usecases.ScanUseCase
import com.example.a_sbd.domain.usecases.SetBleConnectionUseCase
import com.example.a_sbd.ui.MainActivity.Companion.DEVICES_KEY
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    application: Application,
    //private val scanUseCase: ScanUseCase,
    private val setBleConnectionUseCase: SetBleConnectionUseCase,
    private val jsonConverter: JsonConverter,
    //private val bluetoothLeScanner: BluetoothLeScanner,
    //private val scanSettings: ScanSettings,
) : ViewModel() {

    private val context = application
    private val _appState = MutableLiveData<AppState>()
    val appState: LiveData<AppState>
        get() = _appState

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
            // _isBleConnectedInFact = setBleConnectionUseCase()

            /*scanService?.let {
                getAvailableDevicesUseCase(it)
            }*/

            //startScan()

            // _isScanningWorkStarted.value = Unit
        } else {

        }
    }

    private val bleAdapter = BluetoothAdapter.getDefaultAdapter()
    private val bleScanner = bleAdapter.bluetoothLeScanner

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
        .build()

    @SuppressLint("MissingPermission")
    fun startScan() {
        viewModelScope.launch {
            //scanUseCase.startScan()
            Log.d(TAG, "View model starting scan")
        bleScanner.startScan(null, scanSettings, bleScanCallback)

        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        viewModelScope.launch {
            bleScanner.stopScan(bleScanCallback)
        }

    }


    private val _deviceAddress = MutableLiveData<String>()
    val deviceAddress: LiveData<String>
        get() = _deviceAddress

    private val _device = MutableLiveData<BluetoothDevice>()
    val device: LiveData<BluetoothDevice>
        get() = _device

    private var _isScanning = false
    val isScanning: Boolean
        get() = _isScanning

    private val _devices = MutableLiveData<MutableList<BluetoothDevice>?>(mutableListOf())
    val devices: LiveData<MutableList<BluetoothDevice>?>
        get() = _devices

    //private val _devicesSimple = MutableLiveData<List<DeviceSimple>>()
    /*val devicesSimple: LiveData<List<DeviceSimple>>
        get() = scanUseCase.devicesSimple*/

    //private var isBleConnectedInFact = false  // observe this and set signal level
    private val _isBleConnectedInFact = MutableLiveData(false)
    val isBleConnectedInFact: LiveData<Boolean>
        get() = _isBleConnectedInFact

    private val _isScanningWorkStarted = MutableLiveData<Unit>()
    val isScanningWorkStarted: LiveData<Unit>
    get() = _isScanningWorkStarted



    /*fun handleBleScanWorkInfos(workInfos: MutableList<WorkInfo>) {
        if (workInfos.size > 0) {
            val workInfo = workInfos[0]
            if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                val devicesJson = workInfo.outputData.getString(DEVICES_KEY)
                _devicesSimple.value = jsonConverter.fromJsonToListOfDevicesSimple(devicesJson!!)
                *//*Log.d(
                    TAG, "device-1: ${devicesSimple.value?.get(0)?.name} : ${devicesSimple.value?.get(0)?.address}\n" +
                            "device-2: ${devicesSimple.value?.get(1)?.name} : ${devicesSimple.value?.get(1)?.address}"
                )*//*
            }
        }
    }*/

    /*fun handleBleScanResult(devices: List<DeviceSimple>?) {
        Log.d(TAG, "Handling scan results in view model...")
        devices?.let {
            _devicesSimple.value = it
        }
    }*/

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
    fun setConnection(devicePosition: Int) {
        Log.d(TAG, "Device is chosen")
        //setBleConnectionUseCase(devicesSimple.value?.get(devicePosition)!!.address!!)
    }

    private val bleScanCallback =  object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d(TAG, "Found device: ${result.device}")

            val mutableListDevices = devices.value
            val indexQuery = devices.value?.indexOfFirst { it.address == result.device.address }
            if (indexQuery != -1) {// device is already exist
                mutableListDevices?.set(indexQuery!!, result.device)
                stopScan()
            } else {
                /*with(result.device) {
                    Log.d(TAG, "Found BLE device! Name: ${name ?: "Unnamed"}, " +
                            "Address: $address")
                }*/
                mutableListDevices?.add(result.device)
                //Log.d(TAG, "device added to devices: ${device.value?.address}")
            }
            if (!mutableListDevices.isNullOrEmpty()) _devices.value = mutableListDevices
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            Log.d(TAG, "Batch scan results")
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e(TAG, "onScanFailed: scan error $errorCode")
        }
    }


    companion object {
        private const val LOG_TAG = "MainViewModel"

        const val SCAN_TAG = "scan"

        const val SCAN_ID = "00000001-0000-1000-8000-00805f9b34fb"
    }

    data class AppState(
        val isNavBarVisible: Boolean = true,
        val isAppbarLogoVisible: Boolean = true,
        val title: String = "",
        val signalLevel: Int = 0,
        val isBleConnectedIcon: Boolean = false
        )
}