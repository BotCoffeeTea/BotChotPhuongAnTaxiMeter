class MainActivity : AppCompatActivity() {

    private lateinit var fareCalculator: FareCalculator

    private lateinit var googleMap: GoogleMap

    private lateinit var printer: BluetoothPrinter

    private var startTime: Long = 0

    private var lastLatLng: LatLng? = null

    private var lastTime: Long = 0

    // Khai báo giới hạn tốc độ và khoảng cách

    private val speedLimit = 10.0 // km/h

    private val distanceLimit = 0.05 // km

    // Khởi tạo các view

    private lateinit var distanceTextView: TextView

    private lateinit var fareTextView: TextView

    private lateinit var waitingTimeTextView: TextView

    private lateinit var distanceRateTextView: TextView

    private lateinit var startButton: Button

    private lateinit var tripHistoryButton: Button

    private lateinit var fareConfigButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Khởi tạo FareCalculator với cấu hình giá vé từ cơ sở dữ liệu Room

        val config = AppDatabase.getInstance(this).fareConfigDao().getFareConfig()

        fareCalculator = FareCalculator(config)

        // Khởi tạo Google Map

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync { map ->

            googleMap = map

            googleMap.uiSettings.isZoomControlsEnabled = true

            googleMap.setOnMapClickListener { latLng ->

                // Xử lý sự kiện click trên bản đồ

                // Tính toán khoảng cách và hiển thị trên giao diện

                val distance = calculateDistance(latLng)

                val distanceText = getString(R.string.distance, distance)

                distanceTextView.text = distanceText

            }

        }

        // Khởi tạo Bluetooth Printer

        printer = BluetoothPrinter(this)

        // Khởi tạo các view

        distanceTextView = findViewById(R.id.distanceTextView)

        fareTextView = findViewById(R.id.fareTextView)

        waitingTimeTextView = findViewById(R.id.waitingTimeTextView)

        distanceRateTextView = findViewById(R.id.distanceRateTextView)

        startButton = findViewById(R.id.startButton)

        tripHistoryButton = findViewById(R.id.tripHistoryButton)

        fareConfigButton = findViewById(R.id.fareConfigButton)

        // Xử lý sự kiện click startButton

        startButton.setOnClickListener {

            // Tính giá vé và hiển thị thông tin chuyến đi

            val distance = calculateDistance(googleMap.cameraPosition.target)

            val waitingTime = getWaitingTime()

            val fare = fareCalculator.calculateFare(distance, waitingTime)

            val distanceText = getString(R.string.distance, distance)

            val fareText = getString(R.string.fare, fare)

            val waitingTimeText = getString(R.string.waiting_time, waitingTime)

            val distanceRateText = getDistanceRateText(distance)

            distanceTextView.text = distanceText

            fareTextView.text = fareText

            waitingTimeTextView.text = waitingTimeText

            distanceRateTextView.text = distanceRateText

            // Lưu thông tin chuyến đi vào cơ sở dữ liệu Room

            val trip = Trip(

                startLat = googleMap.cameraPosition.target.latitude,

                startLng = googleMap.cameraPosition.target.longitude,

                endLat = googleMap.cameraPosition.target.latitude,

                endLng = googleMap.cameraPosition.target.longitude,

                distance = distance,

                fare = fare,

                waitingTime = waitingTime,

                date = Date()

            )

            AppDatabase.getInstance(this).tripDao().insertTrip(trip)

            // In hóa đơn

            val invoice = Invoice(distance, fare, waitingTime, distanceRateText)

            printer.print(invoice)

        }

        // Xử lý sự kiện click tripHistoryButton

        tripHistoryButton.setOnClickListener {

            val intent = Intent(this, TripHistoryActivity::class.java)

            startActivity(intent)

        }

        // Xử lý sự kiện click fareConfigButton

        fareConfigButton.setOnClickListener {

            val intent = Intent(this, FareConfigActivity::class.java)

            startActivity(intent)

        }

    }

    // Tính toán khoảng cách giữa vị trí hiện tại và vị trí trước đó

    private fun calculateDistance(latLng: LatLng): Double {

        if (lastLatLng == null) {

            lastLatLng = latLng

            return 0.0

        }

        val distance = distanceBetween(lastLatLng!!, latLng)

        lastLatLng = latLng

        return distance

    }

    // Tính toán thời gian chờ

    private fun getWaitingTime(): Long {

        val currentTime = System.currentTimeMillis()

        val waitingTime = currentTime - lastTime

        lastTime = currentTime

        return waitingTime

    }

    // Tính toán tốc độ di chuyển

    private fun getSpeed(startLatLng: LatLng, endLatLng: LatLng, time: Long): Double {

        val distance = distanceBetween(startLatLng, endLatLng)

        val speed = distance / time * 3.6 // km/h

        return speed

    }

    // Kiểm tra xe đang di chuyển hay dừng lại

    private fun isMoving(startLatLng: LatLng, endLatLng: LatLng, time: Long): Boolean {

        val speed = getSpeed(startLatLng, endLatLng, time)

        return speed > speedLimit

    }

    // Tính toán tỷ lệ di chuyển trong thành phố hay đường cao tốc

    private fun getDistanceRateText(distance: Double): String {

        return if (distance < distanceLimit) {

            getString(R.string.distance_rate_city)

        } else {

            getString(R.string.distance_rate_highway)

        }

    }

    // Tính toán khoảng cách giữa hai vị trí

    private fun distanceBetween(startLatLng: LatLng, endLatLng: LatLng): Double {

        val results = FloatArray(1)

        Location.distanceBetween(

            startLatLng.latitude,

            startLatLng.longitude,

            endLatLng.latitude,

            endLatLng.longitude,

            results

        )

        return results[0].toDouble() / 1000.0 // convert to km

    }

}
