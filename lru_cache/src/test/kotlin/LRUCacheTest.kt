import org.junit.Test

class CacheTest {

    @Test
    fun oneElement() {
        val lruCache = LRUCache<Int, Int>(1)
        check(lruCache.size == 0)
        check(lruCache.isEmpty())

        lruCache.put(12, 56)
        check(lruCache.size == 1)
        check(!lruCache.isEmpty())

        lruCache.put(1, 4)
        check(lruCache.size == 1)
        check(lruCache[1] == 4)
        check(lruCache[12] == null)
    }

    @Test
    fun checkLRU() {
        val lruCache = LRUCache<Int, Int>(3)
        for (i in 0 until 5) {
            lruCache.put(i, i)
        }
        check( lruCache.size == 3)
        check(lruCache[2] == 2)
        check(lruCache[0] == null)
        check(lruCache[1] == null)
        check(lruCache[3] == 3)
        check(lruCache[4] == 4)
        lruCache.put(12, 12)
        check(lruCache[2] == null)
    }
}