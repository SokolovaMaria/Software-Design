import java.util.concurrent.TimeUnit.*


class PostManager(val client: Client) {

    companion object {
        const val PERIOD = 3600
    }

    val currentTime: Int
        get() = MILLISECONDS.toSeconds(System.currentTimeMillis()).toInt()

    fun getPostsByHours(hashTag: String, hours: Int): IntArray {
        var startTime = (currentTime - HOURS.toSeconds(hours.toLong())).toInt()
        val statisticsPerHour = IntArray(hours)
        for (i in 0 until hours) {
            statisticsPerHour[i] = client.getPostsWithinPeriod(hashTag, startTime)
            startTime += PERIOD
        }
        return statisticsPerHour
    }
}