interface Cache<K, V> {
    operator fun get(key: K): V?

    fun put(key: K, value: V): V?

    val size: Int

    fun isEmpty() = size == 0
}

class LRUCache<K, V>(private val maxSize: Int) : Cache<K, V> {

    data class Node<K, V>(val key: K, var value: V, var prev: Node<K, V>?, var next: Node<K, V>?)

    private var head: Node<K, V>? = null

    private var tail: Node<K, V>? = null

    private val map: MutableMap<K, Node<K, V>> = hashMapOf()

    override fun get(key: K): V? {
        map[key]?.let {
            assert(head != null && tail != null)
            moveToHead(it)
            assert(head == it)
            return it.value
        }
        return null
    }

    private fun moveToHead(node: Node<K, V>) {
        assert(!isEmpty())
        if (node == head) return
        node.prev!!.next = node.next
        if (node == tail) {
            tail = node.prev
        } else {
            node.next!!.prev = node.prev
        }
        node.prev = null
        node.next = head
        head!!.prev = node
        head = node
    }

    override fun put(key: K, value: V): V? {
        map[key]?.let {
            assert(head != null && tail != null)
            moveToHead(it)
            assert(head == it)
            val prevValue = it.value
            it.value = value
            return prevValue
        }
        assert(!map.contains(key))
        if (size == maxSize) {
            if (head != tail) {
                tail!!.prev!!.next = null
                map.remove(tail!!.key)
                tail = tail!!.prev
            } else {
                head = null
                tail = null
                map.clear()
            }
        }
        val newNode = Node(key, value, null, null)
        if (isEmpty()) {
            head = newNode
            tail = newNode
        } else {
            assert(head != null && tail != null)
            newNode.next = head
            head!!.prev = newNode
            head = newNode
        }
        map[key] = newNode
        return null
    }

    override val size: Int
        get() = map.size
}