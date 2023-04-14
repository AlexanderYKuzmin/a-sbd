package com.example.a_sbd.ui

import android.app.Service
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkManager
import com.example.a_sbd.ASBDApp
import com.example.a_sbd.R
import com.example.a_sbd.databinding.ActivityMainBinding
import com.example.a_sbd.di.ASBDComponent
import com.example.a_sbd.domain.model.DeviceSimple
import com.example.a_sbd.extensions.dpToIntPx
import com.example.a_sbd.extensions.hasRequiredRuntimePermissions
import com.example.a_sbd.extensions.requestRelevantRuntimePermissions
import com.example.a_sbd.receivers.ScanBroadcastReceiver
//import com.example.a_sbd.services.BleService
//import com.example.a_sbd.services.ScanServiceConnection
//import com.example.a_sbd.services.ScanService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val _inflater: LayoutInflater by lazy {
        LayoutInflater.from(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    /*@Inject
    lateinit var appWorkerFactory: AppWorkerFactory*/

    /*@Inject
    lateinit var scanServiceConnection: ScanServiceConnection*/

    //private var scanService : ScanService? = null
    private var characteristic: BluetoothGattCharacteristic? = null

    @Inject
    lateinit var scanBroadcastReceiver: ScanBroadcastReceiver

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]
    }

    private val workManager = WorkManager.getInstance(this)

    private val deviceAddress: String? = null
    private lateinit var ivSignal: View

    private val appComponent by lazy {
        (application as ASBDApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val gattServiceIntent = Intent(this, BleService::class.java)
        bindService(gattServiceIntent, bleServiceConnection, Context.BIND_AUTO_CREATE)*/

        //setScanServiceConnection()

        //registerScanReceiver()

        supportFragmentManager
            .setFragmentResultListener("single_chat_started", this)
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
        binding.ibtnDisconnected.setOnClickListener {
            if (checkBluetoothPermissions()) {
                //viewModel.handleBleConnectionButton()
                startBleScan()
            }
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_chat, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.appState.observe(this) {
            renderUi(it)
        }

        viewModel.devices.observe(this) {
            viewModel.stopScan()
            //launchShowDevicesFragment(it)
        }

        /*viewModel.isScanningWorkStarted.observe(this) {
            Log.d(TAG, "Scanning starting")
            *//*workManager
                .getWorkInfosForUniqueWorkLiveData(SCANNING_WORK)
                .observe(this) {
                    viewModel.handleBleScanWorkInfos(it)
                }*//*
        }*/
    }

    private fun startBleScan() {
        if (checkBluetoothPermissions()) {
            viewModel.startScan()
            Log.d("BLE2", "Start scan from activity")
            Thread.sleep(1000)
        } else {
            throw java.lang.RuntimeException("No permissions")
        }
    }

    override fun onResume() {
        super.onResume()
        //registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
        /*if (bleService != null) {
            val result = bleService!!.connect(deviceAddress)
            Log.d(TAG, "Connect request result=$result")
        }*/
    }

    override fun onPause() {
        super.onPause()
        //unregisterReceiver(gattUpdateReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        workManager.cancelAllWork()

        Log.d(TAG, "On destroy")
    }



    /*private fun makeGattUpdateIntentFilter(): IntentFilter? {
        return IntentFilter().apply {
            addAction(ACTION_GATT_CONNECTED)
            addAction(ACTION_GATT_DISCONNECTED)
            addAction(ACTION_GATT_SERVICES_DISCOVERED)
            addAction(ACTION_DATA_AVAILABLE)
            addAction(ACTION_DATA_READ)
            addAction(ACTION_DATA_WRITTEN)
            addAction(ACTION_SIGNAL_LEVEL)
            addAction(ACTION_SERVICE_IDLE)
            addAction(ACTION_MODEM_READY)
            addAction(ACTION_SERVICE_CONNECTED)
        }
    }*/

    /*private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                *//*ACTION_GATT_CONNECTED -> {
                    isConnected = true
                    updateConnectionState("Connected")
                }
                ACTION_GATT_DISCONNECTED -> {
                    isConnected = false
                    updateConnectionState("Disconnected")
                }
                ACTION_GATT_SERVICES_DISCOVERED -> {
                    // Show all the supported services and characteristics on the user interface.
                    isServiceDiscovered = true
                    //displayGattServices(bluetoothService?.getSupportedGattServices())
                    characteristic = bleService?.getSupportedGattCharacteristic()

                    bleService?.setCharacteristicNotification(characteristic!!, true)
                    Thread.sleep(1000)

                    Log.d(TAG, "Receiver GATT_SERVICES_DISCOVERED. Write characteristic AT.")
                    bleService?.writeCharacteristic(characteristic!!, CHECK_MODEM)
                }
                ACTION_DATA_READ -> {
                    Log.d(TAG, "Receiver ACTION_DATA_READ. Display message.")
                    displayMessage(intent.getStringExtra(EXTRA_DATA))
                }
                ACTION_DATA_WRITTEN -> {
                    Log.d(TAG, "Receiver ACTION_DATA_WRITTEN. ")
                    displayMessage(intent.getStringExtra(EXTRA_DATA))
                }
                ACTION_DATA_AVAILABLE -> {
                    Log.d(TAG, "Receiver ACTION_DATA_AVAILABLE. Data have changed")
                    val text = intent.getStringExtra(EXTRA_DATA)

                    displayMessage(text)
                }
                ACTION_SIGNAL_LEVEL -> {
                    val level = intent.getIntExtra(SIGNAL_DATA, -1)
                    Log.d(TAG, "Receiver ACTION_SIGNAL_DATA. Signal level: $level")
                }
                ACTION_SERVICE_IDLE -> {
                    isServiceIdle = intent.getBooleanExtra(SERVICE_IDLE, false)
                }
                ACTION_MODEM_READY -> {
                    val isModemReady = intent.getBooleanExtra(MODEM_READY, false)
                    handleModemReady(isModemReady)
                }*//*
                ACTION_SERVICE_CONNECTED -> {
                    Log.d(TAG, " Service connection successful")
                    //bleService?.startScan()
                }
            }
        }
    }*/

    /*private val scanServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bleService = (service as BleService.LocalBinder).getService()
            Log.d(TAG, "Service $bleService")
            bleService?.let { bluetooth ->
                // call functions on service to check connection and connect to devices
                if (!bluetooth.initialize()) {
                    Log.e(TAG, "Unable to initialize Bluetooth")
                    finish()
                }
                // perform device connection
                Log.d(TAG, "Service Connected $bleService")
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.d(TAG, "Service Disconnected $bleService")
            bleService = null
        }
    }*/

    /*private fun setScanServiceConnection() {
        //serviceConnection = BleServiceConnection(this, deviceAddress)
        scanServiceConnection.onScanServiceConnectionListener = object : ScanServiceConnection.OnScanServiceConnectionListener {
            override fun setServiceToViewModel(service: ScanService?) {
                viewModel.scanService = service
            }
        }
        val scanServiceIntent = Intent(this, ScanService::class.java)
        bindService(scanServiceIntent, scanServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun registerScanReceiver() {
        scanBroadcastReceiver.onScanResultListener = object : ScanBroadcastReceiver.OnScanResultListener {
            override fun setScanResult(devices: List<DeviceSimple>?) {
                Log.d(TAG, "Working scan listener...")
                viewModel.handleBleScanResult(devices)
            }
        }
        registerReceiver(scanBroadcastReceiver, setScanWorkIntentFilter())
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController(R.id.nav_host_fragment_activity_main).popBackStack()
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
        Log.d("MainActivity", "title of toolbar = ${title.text.toString()}")
        title.typeface = Typeface.createFromAsset(assets, "fonts/invasion.ttf")
        //setTitle(title)
        title.text = appState.title
    }

    fun getComponent(): ASBDComponent {
        return appComponent
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

    private fun changeIv() {
        runBlocking {
            launch {
                delay(1000)

                //val ivSignal = binding.toolbar.getChildAt(2) as ImageView
                val drawableList = listOf(
                    R.drawable.slevel_1_1,
                    R.drawable.slevel_2_0,
                    R.drawable.slevel_3_0,
                    R.drawable.slevel_4_0,
                    R.drawable.slevel_5_0
                )
                for (i in 0 until 5) {
                    //ivSignal.background = getDrawable(drawableList[i])
                    binding.toolbar.menu.findItem(R.id.ivSignal).setIcon(drawableList[i])
                    Log.d("MainActivity", "Set drawable: $i")
                    //renderUi()
                    //setSupportActionBar(binding.toolbar)
                    delay(1500)
                }
            }

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
        view.typeface = typeFace
        //val subtitle = toolbar.getChildAt(0) as TextView

        view.setPadding(dpToIntPx(16), 0, 0,0)
        //subtitle.setPadding(dpToIntPx(16), 0, 0,0)
    }

    private fun setLogo() {
        val logo = if (binding.toolbar.childCount > 1) binding.toolbar.getChildAt(1) as ImageView else null
        logo?.scaleType = ImageView.ScaleType.CENTER_CROP

        logo?.layoutParams?.width = dpToIntPx(60)
        logo?.layoutParams?.height = dpToIntPx(50)
    }

    private fun checkBluetoothPermissions(): Boolean {
        if (!hasRequiredRuntimePermissions()) {
            requestRelevantRuntimePermissions()
        } else {
            return true
        }
        //throw java.lang.RuntimeException("Invalid permission state!")
        return false
    }

    private fun launchShowDevicesFragment(devicesSimple: List<DeviceSimple>) {
        val alertDialog = DeviceListDialogFragment(devicesSimple).apply {
            onDeviceItemClickListener = object : DeviceListDialogFragment.OnDeviceItemClickListener {
                override fun onDeviceItemClick(position: Int) {
                    viewModel.setConnection(position)
                }
            }
        }
        alertDialog.show(this.supportFragmentManager.beginTransaction(), null)
    }

    private fun setScanWorkIntentFilter(): IntentFilter? {
        return IntentFilter().apply {
            addAction(ACTION_DEVICES_FOUND)
        }
    }

    companion object {
        const val TAG = "service"
        //const val SCAN_TAG = "scan_tag"

        const val SINGLE_CHAT_MODE = 200
        const val CHAT_CONTACTS_MODE = 201

        const val SCANNING_LOG_TAG = "Scanning"
        const val CONNECTION_LOG_TAG = "Connection"
        const val GATT_CALLBACK_LOG_TAG = "GattCallback"

        const val SCANNING_WORK = "scanning_work"
        const val CONNECTING_WORK = "connecting_work"
        const val DEVICES_KEY = "devices"

        const val SCAN_INTENT_REQUEST_CODE = 1

        //receiver data
        const val ACTION_DEVICES_FOUND = "a_sbd.ui.devices_found"
    }
}