class FareCalculator(private val config: FareConfig) {

    fun calculateFare(distance: Double, waitingTime: Long): Int {

        val distanceFare = if (distance < 25) {

            distance * config.shortDistanceRate

        } else {

            distance * config.longDistanceRate

        }

        

        val waitingTimeFare = waitingTime * config.waitingTimeRate

        

        return config.baseFare + distanceFare.toInt() + waitingTimeFare.toInt()

    }

}
