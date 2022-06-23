package sorting

import java.util.Scanner

interface SortableDataType<T> {
    val datumName: String
    fun Scanner.getNext(): T
    fun getMax(data: List<T>): T?
    fun printMax(max: T)
}

class Sortable<T>(dataType: SortableDataType<T>) : SortableDataType<T> by dataType {
    private val data: List<T>
    init {
        val mData = mutableListOf<T>()
        val scanner = Scanner(System.`in`)
        while(scanner.hasNext()) {
            mData.add(scanner.getNext())
        }
        data = mData.toList()
    }

    fun report() {
        val maxDatum = getMax(data)!!
        val total = data.size
        println("Total ${datumName}s: $total.")
        printMax(maxDatum)
        val occurrences = data.count { it == maxDatum }
        val percent = occurrences * 100 / total
        println("($occurrences time(s), $percent%)")
    }
}

class Numbers : SortableDataType<Long> {
    override val datumName = "number"
    override fun Scanner.getNext(): Long = this.nextLong()
    override fun getMax(data: List<Long>) = data.maxOrNull()
    override fun printMax(max: Long) = print("The greatest number: $max ")
}

class Lines : SortableDataType<String> {
    override val datumName = "line"
    override fun Scanner.getNext(): String = this.nextLine()
    override fun getMax(data: List<String>) = data.maxByOrNull { it.length }
    override fun printMax(max: String) = print("The longest line:\n$max\n")
}

class Words : SortableDataType<String> {
    override val datumName = "word"
    override fun Scanner.getNext(): String = this.next()
    override fun getMax(data: List<String>) = data.maxByOrNull { it.length }
    override fun printMax(max: String) = print("The longest word: $max ")
}

fun main(args: Array<String>) {
    val dataType = if (args.size > 1 && args[0] == "-dataType") args[1] else "word"
    when(dataType) {
        "long" -> Sortable(Numbers())
        "line" -> Sortable(Lines())
        else -> Sortable(Words())
    }.report()
}
