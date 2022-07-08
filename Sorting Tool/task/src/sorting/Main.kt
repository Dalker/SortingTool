package sorting

import java.util.Scanner

interface SortableDataType<T> {
    val datumName: String
    fun Scanner.getNext(): T
    fun getMax(data: List<T>): T?
    fun printMax(max: T)
    fun sorted(data: List<T>): List<T>
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
    fun reportSorted() {
        val total = data.size
        println("Total ${datumName}s: $total.")
        print("Sorted data: ")
        println(sorted(data).joinToString(" "))
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
    override fun sorted(data: List<Long>) = data.sortedBy { it }
    override fun getMax(data: List<Long>) = data.maxOrNull()
    override fun printMax(max: Long) = print("The greatest $datumName: $max ")
}

class Lines : SortableDataType<String> {
    override val datumName = "line"
    override fun Scanner.getNext(): String = this.nextLine()
    override fun sorted(data: List<String>) = data.sortedBy { it }
    override fun getMax(data: List<String>) = data.maxByOrNull { it.length }
    override fun printMax(max: String) = print("The longest $datumName:\n$max\n")
}

class Words : SortableDataType<String> {
    override val datumName = "word"
    override fun Scanner.getNext(): String = this.next()
    override fun sorted(data: List<String>) = data.sortedBy { it }
    override fun getMax(data: List<String>) = data.maxByOrNull { it.length }
    override fun printMax(max: String) = print("The longest $datumName: $max ")
}

fun main(args: Array<String>) {
    if (args.contains("-sortIntegers")) {
       Sortable(Numbers()).reportSorted()
    } else {
        val dataType = if (args.size > 1 && args[0] == "-dataType") args[1] else "word"
        when(dataType) {
            "long" -> Sortable(Numbers())
            "line" -> Sortable(Lines())
            else -> Sortable(Words())
        }.report()
    }
}
