package com.example.a_sbd.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanResult
import android.content.*
import android.graphics.Typeface
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkManager
import com.example.a_sbd.ASBDApp
import com.example.a_sbd.R
import com.example.a_sbd.databinding.ActivityMainBinding
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.extensions.dpToIntPx
import com.example.a_sbd.extensions.hasRequiredRuntimePermissions
import com.example.a_sbd.extensions.requestRelevantRuntimePermissions
import com.example.a_sbd.services.BleService
import com.example.a_sbd.services.BleService.Companion.ACTION_DATA_AVAILABLE
import com.example.a_sbd.services.BleService.Companion.ACTION_DATA_READ
import com.example.a_sbd.services.BleService.Companion.ACTION_DATA_WRITTEN
import com.example.a_sbd.services.BleService.Companion.ACTION_EVENT_SIGNAL_REPORT_ENABLE
import com.example.a_sbd.services.BleService.Companion.ACTION_GATT_CONNECTED
import com.example.a_sbd.services.BleService.Companion.ACTION_GATT_DISCONNECTED
import com.example.a_sbd.services.BleService.Companion.ACTION_GATT_SERVICES_DISCOVERED
import com.example.a_sbd.services.BleService.Companion.ACTION_MODEM_READY
import com.example.a_sbd.services.BleService.Companion.ACTION_MO_BUFFER_CLEARED
import com.example.a_sbd.services.BleService.Companion.ACTION_SERVICE_CONNECTED
import com.example.a_sbd.services.BleService.Companion.ACTION_SERVICE_IDLE
import com.example.a_sbd.services.BleService.Companion.ACTION_SIGNAL_LEVEL
import com.example.a_sbd.ui.MainActivityViewModel.Companion.WRONG_ID
import com.example.a_sbd.ui.chats.ChatContactsFragment
import com.example.a_sbd.ui.chats.SingleChatFragment
//import com.example.a_sbd.services.BleService
//import com.example.a_sbd.services.ScanServiceConnection
//import com.example.a_sbd.services.ScanService
import javax.inject.Inject

class MainActivity :
    AppCompatActivity(),
    DeviceListDialogFragment.OnDeviceItemClickListener,
    SingleChatFragment.OnMessageSendListener
{

    private lateinit var binding: ActivityMainBinding

    private val _inflater: LayoutInflater by lazy {
        LayoutInflater.from(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var bluetoothAdapter: BluetoothAdapter

    //@Inject
    //lateinit var scanBroadcastReceiver: ScanBroadcastReceiver

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]
    }

    private val workManager = WorkManager.getInstance(this)

    private var bleService: BleService? = null

   /* private val deviceAddress: String? = null
    private lateinit var ivSignal: View*/


    private var isDevicesListLocked = false

    private var stateConnected = false

    private val appComponent by lazy {
        (application as ASBDApp).component
    }

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment_activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gattServiceIntent = Intent(this, BleService::class.java)
        bindService(gattServiceIntent, bleServiceConnection, Context.BIND_AUTO_CREATE)

        //setScanServiceConnection()

        registerScanReceiver()

        registerGattUpdateReceiver()

        supportFragmentManager
            .setFragmentResultListener(FRAGMENT_IN_ACTION, this)
            { str, bundle ->
                Log.d("MainActivity", "BTN in fragment pressed!!!!")
                if (bundle.getBoolean("isSingleChatStarted")) {
                    viewModel.handleUiMode(SINGLE_CHAT_MODE, bundle)
                } else {
                    viewModel.handleUiMode(CHAT_CONTACTS_MODE, bundle)
                }
            }

        setupToolbar()

        //var isConnected = false

        val navView: BottomNavigationView = binding.navView
        //val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_chat,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.ibtnDisconnected.setOnClickListener {
            if (checkBluetoothPermissions()) {
                viewModel.handleBleConnectionPressed()

                if (!viewModel.isBleConnected) {
                    navController.navigate(R.id.scanningShowFragment)
                    startBleScan()
                } else {
                    viewModel.removeConnection()
                }
            }
        }

        viewModel.appState.observe(this) {
            /*if (stateConnected  != it.isBleConnectedIcon) {   // check it out , maybe remove condition
                //navController.popBackStack(R.id.deviceListDialogFragment, true)
                supportFragmentManager.popBackStack()
            }*/
            renderUi(it)
        }

        viewModel.devicesSimple.observe(this) {
            if (it.isNotEmpty() && !isDevicesListLocked) {
                isDevicesListLocked = true
                navController.popBackStack()
                launchShowDevicesFragment(it)
            }
        }

        viewModel.deviceAddressConnected.observe(this) {
            //launchShowConnectFragment()
            if (it != null) {
                bleService?.connect(it)
            }
            else {
                bleService?.disconnect()
                viewModel.updateAppStateConnected(false)
            }
        }

        viewModel.isTimeToStartSession.observe(this) {
            if(it) {
                viewModel.stopDepartureTimer()
                bleService?.checkSignalLevel()
            }
        }

        viewModel.checkSignalAndStartSession.observe(this) {
            viewModel.stopDepartureTimer()
            //bleService?.checkSignalLevel()
            bleService?.setSbdRingStateInActive(false)
        }

        viewModel.startSession.observe(this) {
            viewModel.stopDepartureTimer()
            bleService?.openSession()  //no need open session at once. First we need to check for unsent messages.
        }

        viewModel.startWriteBuffer.observe(this) {
            Log.d(TAG, "Activity. Starting to write message")
            bleService?.writeToBuffer(it.text, it.id)
        }

        viewModel.startReadBuffer.observe(this) {
            Log.d(TAG, "Activity. Starting read buffer")
            bleService?.readFromBuffer()
        }

        viewModel.activateSbdRing.observe(this) {
            Log.d(TAG, "Activity. Set SBDRING.")
            bleService?.setSbdRingStateInActive(it)
        }

        viewModel.resetMessageIdInService.observe(this) {
            bleService?.resetMessageId()
        }

        workManager.pruneWork()
    }

    private fun startBleScan() {
        if (checkBluetoothPermissions()) {
            viewModel.startScan()
            Log.d("BLE2", "Start scan from activity")
        } else {
            throw java.lang.RuntimeException("No permissions")
        }
    }

    override fun onPause() {
        super.onPause()
        //unregisterReceiver(gattUpdateReceiver)
        workManager.cancelAllWork()
        unregisterReceiver(scanBroadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        workManager.cancelAllWork()

        Log.d(TAG, "On destroy")
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.scanningShowFragment -> {
                viewModel.stopScan()
            }
            R.id.connectShowFragment -> {
                bleService?.disconnect()
            }
        }
        super.onBackPressed()
    }

    private val bleServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bleService = (service as BleService.LocalBinder).getService()
            Log.d(TAG, "Ble service connected: $bleService")
            bleService?.let {
                if (!it.initialize(bluetoothAdapter)) {
                    throw java.lang.RuntimeException("Unable to initialize Bluetooth")
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bleService = null
        }
    }

    private fun createGattUpdateIntentFilter(): IntentFilter? {
        return IntentFilter().apply {
            addAction(ACTION_GATT_CONNECTED)
            addAction(ACTION_GATT_DISCONNECTED)
            addAction(ACTION_GATT_SERVICES_DISCOVERED)
            addAction(ACTION_DATA_AVAILABLE)
            addAction(ACTION_DATA_READ)
            addAction(ACTION_DATA_WRITTEN)
            addAction(ACTION_SIGNAL_LEVEL)
            addAction(ACTION_EVENT_SIGNAL_REPORT_ENABLE)
            addAction(ACTION_SERVICE_IDLE)
            addAction(ACTION_MODEM_READY)
            addAction(ACTION_SERVICE_CONNECTED)
            addAction(ACTION_MO_BUFFER_CLEARED)
        }
    }

    private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "Broadcast receive")
            when (intent.action) {
                ACTION_GATT_CONNECTED -> {
                    //_isBleConnected = intent.getBooleanExtra(BleService.IS_CONNECTED, false)
                    navController.popBackStack(R.id.deviceListDialogFragment, true)
                    viewModel.updateAppStateConnected(true)
                    viewModel.defaultActionsAfterConnectionSet(this@MainActivity) // todo init preparations
                }
                ACTION_GATT_DISCONNECTED -> {
                    viewModel.updateAppStateConnected(false)
                //TODO
                }
                /*ACTION_EVENT_SIGNAL_REPORT_ENABLE -> {
                    val signalLevel = intent.getIntExtra(ACTION_EVENT_SIGNAL_REPORT_ENABLE, 0)
                    viewModel.setSignalQualityIndicator(signalLevel)
                }*/
                ACTION_SIGNAL_LEVEL -> {
                    val signal = intent.getIntExtra(ACTION_SIGNAL_LEVEL, 0)
                    viewModel.setSignalQualityIndicator(signal)
                    viewModel.handleSignalQuality(signal)
                }
                ACTION_DATA_AVAILABLE -> {
                    val jsonStringSessionData = if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                        intent.getSerializableExtra(ACTION_DATA_AVAILABLE, String::class.java)
                    } else {
                        intent.getSerializableExtra(ACTION_DATA_AVAILABLE) as String
                    }
                    viewModel.handleDataAvailable(jsonStringSessionData!!)
                    Log.d(TAG, "Session parameters from broadcast: ${jsonStringSessionData}")
                }
                ACTION_DATA_WRITTEN -> {
                    val id = intent.getLongExtra(ACTION_DATA_WRITTEN, -1)
                    if (id != WRONG_ID) viewModel.handleMessageWritten(id)
                    else throw RuntimeException("Wrong id to update message.")
                }
                ACTION_DATA_READ -> {
                    val textIncome = intent.getStringExtra(ACTION_DATA_READ)
                    if (!textIncome.isNullOrEmpty()) viewModel.handleMessageIncome(textIncome, 3L)
                }
            }
        }
    }
    private fun registerGattUpdateReceiver() {
        registerReceiver(gattUpdateReceiver, createGattUpdateIntentFilter())
    }

    private val scanBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            Log.d(TAG, "onReceive scan broadcast Main Activity")
            if (intent.hasExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT)) {
                Log.d(TAG, "onReceive intent has extra EXTRA_LIST_SCAN_RESULT")

                val results = if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                    intent.getParcelableArrayListExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT, ScanResult::class.java)
                } else {
                    intent.getParcelableArrayListExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT)
                }

                viewModel.handleBleScanResult(results)
                viewModel.stopScan()
            }
        }
    }
    private fun registerScanReceiver() {
        registerReceiver(scanBroadcastReceiver, setScanWorkIntentFilter())
    }

    // navigation menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navController.popBackStack()
                viewModel.handleUiMode(CHAT_CONTACTS_MODE, bundleOf())
                return true
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private fun renderUi(appState: MainActivityViewModel.AppState) {
        Log.d("MainActivity", "Render UI")
        if (appState.isNavBarVisible) binding.navView.visibility = View.VISIBLE
        else binding.navView.visibility = View.INVISIBLE

        if (appState.isAppbarLogoVisible) {
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setLogo(R.drawable.iridium_logo_2)
        }
        else supportActionBar?.setDisplayShowHomeEnabled(false)

        //supportActionBar?.title = appState.title
        val title = binding.toolbar.getChildAt(0) as TextView
        Log.d("MainActivity", "title of toolbar _1 = ${title.text.toString()}")
        title.typeface = Typeface.createFromAsset(assets, "fonts/invasion.ttf")
        //setTitle(title)
        title.text = appState.title
        Log.d("MainActivity", "title of toolbar _2 = ${title.text.toString()}")

        changeConnectionIv(appState.isBleConnectedIcon)
        changeIv(appState.signalLevel)
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.appbar_menu, menu)

        val ivSignal: MenuItem = menu!!.findItem(R.id.ivSignal)
        ivSignal.setIcon(getDrawable(R.drawable.slevel_5_0))

        *//*runBlocking {
            launch {
                delay(1000)
                binding.toolbar.menu.findItem(R.id.ivSignal).setIcon(getDrawable(R.drawable.slevel_5_0))
                Log.d("MainActivity", "Set drawable: 5!!!!!!!!!!!!!!")
                delay(3000)
            }
        }*//*
                return true
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }*/

    private fun changeIv(sLevel: Int) {
        runOnUiThread {
            val signalIcon = when (sLevel) {
                0 -> R.drawable.slevel_0_1
                1 -> R.drawable.slevel_1_1
                2 -> R.drawable.slevel_2_0
                3 -> R.drawable.slevel_3_0
                4 -> R.drawable.slevel_4_0
                5 -> R.drawable.slevel_5_0
                else -> throw java.lang.RuntimeException("Unreachable signal level")
            }
            //binding.toolbar.menu.findItem(R.id.ivSignal).setIcon(signalIcon)
            binding.toolbar.findViewById<ImageView>(R.id.iv_signal2).background =
                ContextCompat.getDrawable(this, signalIcon)
        }
    }

    private fun changeConnectionIv(isConnected: Boolean) {
        runOnUiThread {
            val isConnectedDrawable = if (isConnected) {
                ContextCompat.getDrawable(this, R.drawable.connected_2)
            } else {
                ContextCompat.getDrawable(this, R.drawable.disconnected_2)
            }
            binding.toolbar.findViewById<ImageView>(R.id.ibtn_disconnected).background = isConnectedDrawable
        }
    }
/*
    private fun renderUi() {
        with(binding) {
            toolbar.title =
        }
        toolbar.title = data.title ?: "Skill Articles"
        toolbar.subtitle = data.category ?: "loading..."
        if (data.categoryIcon != null) toolbar.logo = getDrawable(data.categoryIcon as Int)
    }*/

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //logo?.background = getDrawable(R.drawable.pngwing)
        /*val lp = logo?.layoutParams as? Toolbar.LayoutParams
        lp?.let{
            it.width = dpToIntPx(40)
            it.height = dpToIntPx(40)
            it.marginEnd = dpToIntPx(16)
            logo.layoutParams = it
            println("Layout Params of logotype ${logo.layoutParams.width} :: ${logo.layoutParams.height}")
        }*/

        setLogo()
        val title = binding.toolbar.getChildAt(0) as TextView
        setTitle(title)


        /*ivSignal = _inflater.inflate(R.layout.iv_signal, null)
        binding.toolbar.addView(ivSignal, Toolbar.LayoutParams(Gravity.END))*/
        //binding.toolbar.inflateMenu(R.menu.appbar_menu)
    }

    private fun setTitle(view: TextView) {
        val typeFace = Typeface.createFromAsset(assets, "fonts/invasion.ttf")
        with(view) {
            typeface = typeFace
            setPadding(dpToIntPx(16), 0, 0, 0)
        }
    }

    private fun setLogo() {
        val logo = if (binding.toolbar.childCount > 1) binding.toolbar.getChildAt(1) as ImageView else null
        logo?.let {
            with(it) {
                scaleType = ImageView.ScaleType.CENTER_CROP
                layoutParams?.width = dpToIntPx(60)
                layoutParams?.height = dpToIntPx(50)
            }
        }
    }

    private fun checkBluetoothPermissions(): Boolean {
        if (!hasRequiredRuntimePermissions()) requestRelevantRuntimePermissions()
        else return true
        return false
    }

    private fun launchShowDevicesFragment(devicesSimple: List<DeviceSimple>) {
        /*navController.navigate(
            ChatContactsFragmentDirections.actionNavigationChatToDeviceListDialogFragment(devicesSimple.toTypedArray())
        )*/
        Log.d(TAG, "Show Devices: ")
        devicesSimple.forEach { Log.d(TAG, "name: ${it.name}, address: ${it.address}") }

        val bundle = Bundle().apply {
            putParcelableArrayList(DeviceListDialogFragment.EXTRA_DEVICES_LIST, devicesSimple as ArrayList<out Parcelable>)
        }
        navController.navigate(R.id.deviceListDialogFragment, bundle)
        /*supportFragmentManager.beginTransaction()
            .replace(R.id.container, DeviceListDialogFragment.newInstance(devicesSimple))
            .commit()*/
    }

    private fun launchShowConnectFragment() {
        navController.navigate(R.id.connectShowFragment)
    }

    // Listen click in device list

    override fun onDeviceItemClick(position: Int) {

        isDevicesListLocked = false
        launchShowConnectFragment()

        if (position > -1) {
            Log.d(TAG, "bleService in MainActivity: $bleService")
            viewModel.setConnection(position)
        } else {
            navController.popBackStack(R.id.navigation_chat, false)
        }
    }

    //Listen to message send action
    override fun onMessageSend(message: Message) {
        //viewModel.sendMessageByBle(message) // Мне не надо спецмально отправлять одно сообщение. Отправляем из базы по изменению live data базы
        Log.d(TAG, "View model sending message at the moment")
    }

    private fun setScanWorkIntentFilter(): IntentFilter {
        return IntentFilter().apply {
            addAction(ACTION_DEVICES_FOUND)
        }
    }

    companion object {
        const val TAG = "service"
        //const val SCAN_TAG = "scan_tag"

        const val FRAGMENT_IN_ACTION = "which_fragment_in_action"

        const val SINGLE_CHAT_MODE = 200
        const val CHAT_CONTACTS_MODE = 201

        const val SCANNING_LOG_TAG = "Scanning"
        const val CONNECTION_LOG_TAG = "Connection"
        const val GATT_CALLBACK_LOG_TAG = "GattCallback"

        const val IS_SCAN_START = "is_scan_start"
        const val SCANNING_DATA = "scanning_data"

        const val SCANNING_WORK = "scanning_work"
        const val DEVICES_KEY = "devices"

        const val SCAN_INTENT_REQUEST_CODE = 1

        //receiver data
        const val ACTION_DEVICES_FOUND = "a_sbd.ui.devices_found"
    }
}