package sorting

import java.util.*

abstract class Sortable<T> {
    abstract val datumName: String
    abstract val max: T?
    abstract fun getNext(scanner: Scanner): T
    abstract fun printMax()

    val data = mutableListOf<T>()
    init {
        val scanner = Scanner(System.`in`)
        while(scanner.hasNext()) {
            data.add(getNext(scanner))
        }
    }

    fun report() {
        val total = data.size
        println("Total ${datumName}s: $total.")
        printMax()
        val occurrences = data.count { it == max }
        val percent = occurrences * 100 / total
        println("($occurrences time(s), $percent%)")
    }
}

class Numbers : Sortable<Long>() {
    override val datumName = "number"
    override val max = data.maxOrNull()
    override fun getNext(scanner: Scanner): Long = scanner.nextLong()
    override fun printMax() = print("The greatest number: $max ")
}

class Lines : Sortable<String>() {
    override val datumName = "line"
    override val max = data.maxByOrNull { it.length }
    override fun getNext(scanner: Scanner): String = scanner.nextLine()
    override fun printMax() = print("The longest line:\n$max\n")
}

class Words : Sortable<String>() {
    override val datumName = "word"
    override val max = data.maxByOrNull { it.length }
    override fun getNext(scanner: Scanner): String = scanner.next()
    override fun printMax() = print("The longest word: $max ")
}

fun main(args: Array<String>) {
    val dataType = if (args.size > 1 && args[0] == "-dataType") args[1] else "word"
    when(dataType) {
        "long" -> Numbers()
        "line" -> Lines()
        else -> Words()
    }.report()
}
