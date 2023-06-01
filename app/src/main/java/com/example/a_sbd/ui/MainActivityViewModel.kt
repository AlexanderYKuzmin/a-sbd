package com.example.a_sbd.ui

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.example.a_sbd.R
import com.example.a_sbd.data.commands.*
import com.example.a_sbd.data.mapper.JsonConverter
import com.example.a_sbd.data.mapper.ModemResponseMapper
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.domain.model.MessageType
import com.example.a_sbd.domain.model.WorkBleConnectionResponse
import com.example.a_sbd.domain.usecases.*
import com.example.a_sbd.domain.usecases.ClearDatabaseByDateUseCase.Companion.THREE_DAYS_HOURS
import com.example.a_sbd.extensions.hours
import com.example.a_sbd.services.BleService.Companion.MESSAGE_ID
import com.example.a_sbd.services.BleService.Companion.MO_STATUS
import com.example.a_sbd.services.BleService.Companion.MT_STATUS
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    application: Application,
    private val scanUseCase: ScanUseCase,
    private val getMessageUseCase: GetMessageUseCase,
    private val getLastMessageByContactidUseCase: GetLastMessageByContactIdUseCase,
    private val getAllMessagesUnsentUseCase: GetAllMessagesUnsentUseCase,
    private val getMessagesByContactIdIncomeUseCase: GetMessagesByContactIdIncomeUseCase,
    private val updateMessageUseCase: UpdateMessageUseCase,
    private val updateMessageByIdToDeparted: UpdateMessageByIdToDeparted,
    private val jsonConverter: JsonConverter,
    private val modemResponseMapper: ModemResponseMapper,
    private val bluetoothAdapter: BluetoothAdapter,
    private val clearDatabaseByDateUseCase: ClearDatabaseByDateUseCase,
    private val insertMessageByTextUseCase: InsertMessageByTextUseCase
) : ViewModel() {

    private val departReminderUseCase = DepartReminderUseCase(viewModelScope)

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

    private val _deviceAddressConnected = MutableLiveData<String?>()
    val deviceAddressConnected: LiveData<String?>
        get() = _deviceAddressConnected

    private val _startSession = MutableLiveData<Unit>() // todo maybe i need Int to have range for command
    val startSession: LiveData<Unit>
        get() = _startSession

    private val _checkSignalAndStartSession = MutableLiveData<Unit>()
    val checkSignalAndStartSession: LiveData<Unit>
        get() = _checkSignalAndStartSession

    private val _startReadBuffer = MutableLiveData<Unit>()
    val startReadBuffer: LiveData<Unit>
        get() = _startReadBuffer

    private val _startWriteBuffer = MutableLiveData<Message>()
    val startWriteBuffer: LiveData<Message>
        get() = _startWriteBuffer

    private val _activateSbdRing = MutableLiveData<Boolean>()
    val activateSbdRing: LiveData<Boolean>
        get() = _activateSbdRing

    private val _resetMessageIdInService = MutableLiveData<Unit>()
    val resetMessageIdInService: LiveData<Unit>
        get() = _resetMessageIdInService

    //private val workManager = WorkManager.getInstance(context)

    private var _isMessageDeparted = true

    lateinit var messagesUnsent: LiveData<List<Message>>

    val isTimeToStartSession = departReminderUseCase.isTimeToStartSession

    init {
        Log.d(TAG, "Init view model started")
        viewModelScope.launch {
            clearDatabaseByDateUseCase(Date().hours() - THREE_DAYS_HOURS)
            messagesUnsent = getAllMessagesUnsentUseCase()
            Log.d(TAG, "message unsent ${messagesUnsent}")
        }
    }

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

    fun updateAppStateConnected(isConnected: Boolean) {
        _isBleConnected = isConnected
        if (isBleConnected) {
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

    fun setSignalQualityIndicator(sLevel: Int) {
        _appState.value = appState.value?.copy(
            signalLevel = sLevel
        )
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

    fun startDepartureTimer(duration: Int) = departReminderUseCase.startTimer(duration)

    fun stopDepartureTimer() = departReminderUseCase.resetTimer()

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
        Log.d(TAG, "Results: ")
        results?.forEach { Log.d(TAG, "Device name: ${it.device.name}, device address: ${it.device.address}") }
        _devicesSimple.value =
            results?.map { scanResult -> DeviceSimple(scanResult.device.name, scanResult.device.address) }
    }

    fun handleSignalQuality(signal: Int) {
        if (signal > 2) {
            _startSession.value = Unit
        }
        else {
            Log.d(TAG, "Signal is too low: $signal. The Departure timer is starting.")
            startDepartureTimer(LOW_SIGNAL_DURATION)
        } // todo make a notice to screen
    }

    private var attemptCount = 0
    fun handleDataAvailable(jsonStringSessionData: String) {
        val sessionData = jsonConverter.fromJsonToMapSessionParameters(jsonStringSessionData)
        if (
            sessionData[MO_STATUS] != MO_MESSAGE_TRANSFER_SUCCESSFUL &&
            sessionData[MO_STATUS] != MO_MESSAGE_TRANSFER_SUCCESSFUL_BUT_LOCATION_NO &&
            sessionData[MO_STATUS] != MO_MESSAGE_TRANSFER_SUCCESSFUL_BUT_MT_TOO_BIG
        ) {
            _isMessageDeparted = false
        } else {
            _isMessageDeparted = true
            _resetMessageIdInService.value = Unit
            viewModelScope.launch { sessionData[MESSAGE_ID]?.let { updateMessageByIdToDeparted(it.toLong()) } }
        }

        when {
            //sessionData[MO_STATUS] == MO_MESSAGE_TRANSFER_SUCCESSFUL -> {}
            sessionData[MT_STATUS] == MT_MESSAGE_RECEIVE_SUCCESSFUL -> {
                attemptCount = 0
                _startReadBuffer.value = Unit
            }
            sessionData[MT_STATUS] == MT_MESSAGE_ERROR -> {
                if (attemptCount < 3) {
                    attemptCount++
                    Log.d(TAG, "MT message error: ${sessionData[MT_STATUS]}")
                    //_startSession.value = Unit
                    startDepartureTimer(DEPARTURE_ERROR_DURATION)
                }

            }
            sessionData[MT_STATUS] == MT_NO_SBD_MESSAGE_TO_RECEIVE -> {
                Log.d(TAG, "MT message no to receive: ${sessionData[MT_STATUS]}")

                if (!_isMessageDeparted) { // todo check it out. Create the deque or something
                    if (attemptCount < 3) {
                        //_startSession.value = Unit
                        startDepartureTimer(DEPARTURE_ERROR_DURATION)
                    }
                }
                else _activateSbdRing.value = true
            }
        }
    }

    fun handleMessageWritten(id: Long) {
        Log.d(TAG, "Written ID : $id")
        _checkSignalAndStartSession.value = Unit
    }

    fun handleMessageIncome(text: String, contactId: Long) {
        //var lastMesssageIncome: Message? = null
        var lastMessage: Message? = null
        runBlocking(Dispatchers.IO) {
            val jobGetAllBYContact = launch {
                //lastMesssageIncome = getMessagesByContactIdIncomeUseCase(contactId).lastOrNull()
                lastMessage = getLastMessageByContactidUseCase(contactId)
            }
            jobGetAllBYContact.join()

            launch {
                insertMessageByTextUseCase(
                    text.trim(),
                    //if (lastMesssageIncome == null) MessageType.START_IN else MessageType.NORMAL_IN,
                    lastMessage?.messageType?.nextTypeForIncoming() ?: MessageType.START_IN,
                    contactId
                )
            }
        }
        _checkSignalAndStartSession.value = Unit
    }

    fun setConnection(devicePosition: Int) {
        Log.d(TAG, "Device is chosen")
        val devices = devicesSimple.value
            ?: throw java.lang.RuntimeException("List of devices can not be null whilst connection is being established")

        _deviceAddressConnected.value = devices[devicePosition].address!!
    }

    fun removeConnection() {
        _deviceAddressConnected.value = null
    }

    fun defaultActionsAfterConnectionSet(owner: LifecycleOwner) {
        messagesUnsent.observe(owner) {
            if (it.isNotEmpty()) {
                _startWriteBuffer.value = it.first()
            } else {
                _checkSignalAndStartSession.value = Unit
            }
        }
    }

    fun sendMessageByBle(message: Message) {
        _startWriteBuffer.value = message
        /*viewModelScope.launch {
            getMessageUseCase(id)?.let {
                _textToSendByBle.value = it.text
            }
        }*/
        /*var delayed: Message? = null //todo check everything
        viewModelScope.launch {
            val job: Job = launch {
                delayed = getMessageDelayed.invoke()
                Log.d(TAG, "Delayed message: $delayed")
            }
            job.join()
            _startWriteBuffer.value = delayed!!
        }*/

    }

    fun checkSignalLevel() {
        Log.d(TAG, "Check signal level")
        //checkSignalLevelUseCase()
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
        const val WRONG_ID = -1L

        const val LOW_SIGNAL_DURATION = 1
        const val DEPARTURE_ERROR_DURATION = 3
    }

    data class AppState(
        val isNavBarVisible: Boolean = true,
        val isAppbarLogoVisible: Boolean = true,
        val title: String = "A - S B D",
        val signalLevel: Int = 0,
        val isBleConnectedIcon: Boolean = false
        )
}