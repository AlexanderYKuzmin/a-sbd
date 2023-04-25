package com.example.a_sbd.ui

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import com.example.a_sbd.R
import com.example.a_sbd.data.mapper.JsonConverter
import com.example.a_sbd.data.mapper.ModemResponseMapper
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.domain.model.WorkBleConnectionResponse
import com.example.a_sbd.domain.usecases.CheckSignalLevelUseCase
import com.example.a_sbd.domain.usecases.ScanUseCase
import com.example.a_sbd.domain.usecases.SetBleConnectionUseCase
import com.example.a_sbd.services.BleService
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    application: Application,
    private val scanUseCase: ScanUseCase,
    private val setBleConnectionUseCase: SetBleConnectionUseCase,
    private val checkSignalLevelUseCase: CheckSignalLevelUseCase,
    private val jsonConverter: JsonConverter,
    private val modemResponseMapper: ModemResponseMapper
    //private val bluetoothLeScanner: BluetoothLeScanner,
    //private val scanSettings: ScanSettings,
) : ViewModel() {

    val context = application
    private val _appState = MutableLiveData(AppState())
    val appState: LiveData<AppState>
        get() = _appState

    private var _isBleConnected = false
    val isBleConnected: Boolean
        get() = _isBleConnected

    private val _devicesSimple = MutableLiveData<List<DeviceSimple>>()
    val devicesSimple: LiveData<List<DeviceSimple>>
        get() = _devicesSimple

    //private val workManager = WorkManager.getInstance(context)

    fun handleUiMode(uiMode: Int, bundle: Bundle) {
        when(uiMode) {
            MainActivity.SINGLE_CHAT_MODE -> {
                _appState.value = appState.value?.copy(
                    isNavBarVisible = false,
                    isAppbarLogoVisible = false,
                    title = bundle.getString("title") ?: "Ass"

                )
            }
            MainActivity.CHAT_CONTACTS_MODE -> {
                _appState.value = appState.value?.copy(
                    isNavBarVisible = true,
                    isAppbarLogoVisible = true,
                    title = context.getString(R.string.app_name)
                )
            }
        }
    }

    private fun updateAppStateConnected() {
        if (_isBleConnected) {
            _appState.value = appState.value?.copy(
                isBleConnectedIcon = true,
                title = context.getString(R.string.app_name),
                isAppbarLogoVisible = true
            )

        } else {
            _appState.value = appState.value?.copy(
                isBleConnectedIcon = false,
                title = context.getString(R.string.app_name),
                isAppbarLogoVisible = true,
                signalLevel = SIGNAL_LEVEL_ZERO
            )
        }
    }

    fun handleBleConnectionPressed() {
        _appState.value = appState.value?.copy(
            isAppbarLogoVisible = false
        )
    }

    private fun setSignalQualityIndicator(sLevel: Int) {
        _appState.value = appState.value?.copy(
            signalLevel = sLevel
        )
    }

    fun handleGattUpdateReceiverResult(intent: Intent) {
        when (intent.action) {
            BleService.ACTION_GATT_CONNECTED -> {
                _isBleConnected = true
                updateAppStateConnected()
            }
            BleService.ACTION_GATT_DISCONNECTED -> {
                _isBleConnected = false
                updateAppStateConnected()
            }
        }
    }

   /* fun handleBleConnectionWorkResult(workInfos: MutableList<WorkInfo>) {
        Log.d(TAG, "Handle connection work result. workInfos size ${workInfos.size}")

        val response = getDataFromWorkInfos(workInfos)
        response?.let {
            if (it.commandWorker == SET_BLE_CONNECTION
                && it.responseWorker == BLE_CONNECTION_ESTABLISHED) {
                _isBleConnected = true
                checkSignalLevel()
            } else {
                _isBleConnected = false
                Log.d(TAG, "BLE disconnected")
            }
        }
        handleBleConnectionResultAppState()
    }*/

    /*fun handleCheckSignalWorkResult(workInfos: MutableList<WorkInfo>) {
        Log.d(TAG, "Handle check signal work result. workInfos size ${workInfos.size}")
        val response = getDataFromWorkInfos(workInfos)
        response?.let {
            if (it.responseWorker == MODEM_SIGNAL_QUALITY_OK
                && it.commandModem == GET_SIGNAL_QUALITY_LEVEL) {
                //TODO setSignalLevelIndicator() set level from response modem. Create mapper to parse modem responses
                val signalQuality = modemResponseMapper.parseSignalQuality(it.responseModem!!)
                setSignalQualityIndicator(signalQuality)
            }
            // TODO Check for another cases
        }
    }*/

    @SuppressLint("MissingPermission")
    fun startScan() {
        scanUseCase.invoke(true)
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        scanUseCase.invoke(false)
    }

    //private var isBleConnectedInFact = false  // observe this and set signal level
    /*private val _isBleConnectedInFact = MutableLiveData(false)
    val isBleConnectedInFact: LiveData<Boolean>
        get() = _isBleConnectedInFact*/

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

    fun removeConnection() {
        setBleConnectionUseCase(null)
    }

    fun checkSignalLevel() {
        Log.d(TAG, "Check signal level")
        checkSignalLevelUseCase()
    }

    private fun getDataFromWorkInfos(workInfos: MutableList<WorkInfo>): WorkBleConnectionResponse? {
        val data = workInfos[workInfos.lastIndex].outputData
        return if (data.hasKeyWithValueOfType(REPORT, String::class.java)) {
            jsonConverter.fromJsonToSimpleDataClass(
                data.getString(REPORT)!!,
                WorkBleConnectionResponse::class.java
            )
        } else null
    }

    companion object {
        private const val LOG_TAG = "MainViewModel"

        const val SCAN_TAG = "scan"
        const val CONNECTION_TAG = "connection_tag"
        const val CHECK_SIGNAL_TAG = "check_signal_tag"

        const val WORKER_COMMAND = "worker_command"
        const val MODEM_COMMAND = "modem_command"
        const val REPORT = "worker_report"

        const val CONNECTION_WORK = "set_connection_work_name"
        const val CHECK_SIGNAL_WORK = "check_signal_work_name"

        const val SIGNAL_LEVEL_ZERO = 0
    }

    data class AppState(
        val isNavBarVisible: Boolean = true,
        val isAppbarLogoVisible: Boolean = true,
        val title: String = "A - S B D",
        val signalLevel: Int = 0,
        val isBleConnectedIcon: Boolean = false
        )
}