class FareConfigActivity : AppCompatActivity() {

    private lateinit var fareConfig: FareConfig

    private lateinit var fareConfigDao: FareConfigDao

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fare_config)

        fareConfigDao = AppDatabase.getInstance(this).fareConfigDao()

        fareConfig = fareConfigDao.getFareConfig()

        baseFareEditText.setText(fareConfig.baseFare.toString())

        distanceRateShortEditText.setText(fareConfig.distanceRateShort.toString())

        distanceRateLongEditText.setText(fareConfig.distanceRateLong.toString())

        waitingTimeRateEditText.setText(fareConfig.waitingTimeRate.toString())

        saveButton.setOnClickListener {

            val newFareConfig = FareConfig(

                baseFare = baseFareEditText.text.toString().toInt(),

                distanceRateShort = distanceRateShortEditText.text.toString().toInt(),

                distanceRateLong = distanceRateLongEditText.text.toString().toInt(),

                waitingTimeRate = waitingTimeRateEditText.text.toString().toInt()

            )

            fareConfigDao.insertFareConfig(newFareConfig)

            finish()

        }

        deleteButton.setOnClickListener {

            fareConfigDao.deleteFareConfig(fareConfig)

            finish()

        }

    }

}
