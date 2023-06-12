@Dao

interface TripDao {

    @Query("SELECT * FROM trip_history ORDER BY date DESC")

    fun getTrips(): List<Trip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)

    fun insertTrip(trip: Trip)

    @Delete

    fun deleteTrip(trip: Trip)

}
