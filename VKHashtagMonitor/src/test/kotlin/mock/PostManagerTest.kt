package mock

import Client
import PostManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`

class PostManagerTest {
    private lateinit var postManager: PostManager

    @Mock
    private lateinit var vkClient: Client

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        postManager = PostManager(vkClient)
    }

    @Test
    fun testPosts() {
        val hashTag = "autumn"
        val period = PostManager.PERIOD * 2
        val startTime = postManager.currentTime - period

        `when`(vkClient.getPostsWithinPeriod(hashTag, startTime)).thenReturn(77)
        `when`(vkClient.getPostsWithinPeriod(hashTag, startTime + PostManager.PERIOD)).thenReturn(88)

        val lastPostsPerHours = postManager.getPostsByHours(hashTag, 2)
        assert(lastPostsPerHours.contentEquals(intArrayOf(77, 88)))
    }
}
