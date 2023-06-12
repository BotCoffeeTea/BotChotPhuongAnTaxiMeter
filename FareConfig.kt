@Entity(tableName = "fare_config")

data class FareConfig(

    @PrimaryKey(autoGenerate = true)

    val id: Int = 0,

    val baseFare: Int,

    val distanceRateShort: Int,

    val distanceRateLong: Int,

    val waitingTimeRate: Int

)
