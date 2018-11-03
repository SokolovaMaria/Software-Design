import com.google.gson.*
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import org.apache.http.HttpStatus
import java.io.File
import java.io.IOException
import java.util.*

interface Client {
    fun getPostsWithinPeriod(hashTag: String, startTime: Int, period: Int = PostManager.PERIOD): Int
}

class VKClient : Client {
    private val vk = VkApiClient(HttpTransportClient.getInstance(), Gson(), 10)
    private val actor: UserActor

    init {
        val scanner = Scanner(File(AUTH_INFO))
        val accessToken = scanner.nextLine()
        val userId = scanner.nextInt()
        actor = UserActor(userId, accessToken)
    }

    override fun getPostsWithinPeriod(hashTag: String, startTime: Int, period: Int): Int {
        var totalPosts = 0
        var startFrom = ""
        do {
            val newsFeedQuery = vk.newsfeed().search(actor)
                .q(hashTag)
                .count(200)
                .startTime(startTime)
                .endTime(startTime + period)
                .startFrom(startFrom)
            val queryResponse = newsFeedQuery.executeAsRaw()
            if (queryResponse.statusCode != HttpStatus.SC_OK) {
                throw IOException("unexpected error " + queryResponse.statusCode)
            }
            val parser = VKResponseParser(queryResponse.content)
            totalPosts += parser.count
            println(queryResponse.content)
            startFrom = parser.nextPage
        } while (startFrom.isNotEmpty())
        return totalPosts
    }

    companion object {
        private const val AUTH_INFO = "VKHashtagMonitor/src/main/resources/access_token"
    }
}

class VKResponseParser(clientResponseContent: String) {
    val response: JsonObject

    init {
        val json = JsonParser().parse(clientResponseContent)
        response = json.asJsonObject.getAsJsonObject(RESPONSE)
    }

    val items: JsonArray = response.getAsJsonArray(ITEMS)

    val count: Int = Integer.parseInt(response.getAsJsonPrimitive(COUNT).toString())

    val nextPage: String =  if (response.has(NEXT_FROM)) response.getAsJsonPrimitive(NEXT_FROM).toString() else ""


    companion object {
        private const val RESPONSE = "response"
        private const val ITEMS = "items"
        private const val COUNT = "count"
        private const val NEXT_FROM = "next_from"
    }
}