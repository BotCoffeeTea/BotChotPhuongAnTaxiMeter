@Entity(tableName = "trip_history")

data class Trip(

    @PrimaryKey(autoGenerate = true)

    val id: Int = 0,

    val startLat: Double,

    val startLng: Double,

    val endLat: Double,

    val endLng: Double,

    val distance: Double,

    val fare: Int,

    val waitingTime: Int,

    val date: Date

)
