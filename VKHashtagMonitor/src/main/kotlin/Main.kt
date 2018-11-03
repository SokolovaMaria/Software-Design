
fun main(args: Array<String>) {
    if (args.size != 2) {
        System.err.println("Usage: java Main <hashtag> <hours>")
        System.exit(1)
    }
    val hashTag = "#${args[0]}"
    val hours = Integer.parseInt(args[1])

    val postManager = PostManager(VKClient())
    val statistics = postManager.getPostsByHours(hashTag, hours)
    statistics.forEachIndexed {index, i -> println("$index : $i") }
}