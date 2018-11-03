package mock

import org.junit.ClassRule
import org.junit.Test
import java.util.Arrays
import VKClient
import java.util.concurrent.TimeUnit


@HostReachableRule.HostReachable(ClientReachabilityTest.HOST)
class ClientReachabilityTest {
    companion object {
        const val HOST = "api.vk.com"

        @ClassRule @JvmField
        val rule = HostReachableRule()
    }

    @Test
    fun getInfo() {
        val client = VKClient()
        val hashTag = "#spb"
        val startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toInt()
        val items = client.getPostsWithinPeriod(hashTag, startTime - PostManager.PERIOD * 3)
        assert(items > 0)
    }
}