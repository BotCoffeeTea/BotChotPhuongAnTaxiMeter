@Dao

interface FareConfigDao {

    @Query("SELECT * FROM fare_config LIMIT 1")

    fun getFareConfig(): FareConfig

    @Insert(onConflict = OnConflictStrategy.REPLACE)

    fun insertFareConfig(fareConfig: FareConfig)

    @Delete

    fun deleteFareConfig(fareConfig: FareConfig)

}
