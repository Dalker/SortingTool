package sorting

import java.util.Scanner

class ParsingException(err: String) : java.lang.RuntimeException(err)
class SortableScannerException(err: String) : java.lang.RuntimeException(err)

interface SortableDataType<T> {
    val datumName: String
    fun Scanner.getNext(): T
    fun chooseLeft(left: T, right: T): Boolean
}

class Sortable<T>(dataType: SortableDataType<T>)
    : SortableDataType<T> by dataType {
    companion object {
        const val DEFAULT_SORTING = "natural"
        const val DEFAULT_TYPE = "word"
        fun forType(dataTypeName: String) = Sortable(
            when(dataTypeName) {
                "long" -> Numbers()
                "line" -> Lines()
                "word" -> Words()
                else -> error("unknown data type: $dataTypeName")
            }
        )
    }
    private val data: List<T>
    init {
        val mData = mutableListOf<T>()
        val scanner = Scanner(System.`in`)
        while(scanner.hasNext()) {
            try {
                mData.add(scanner.getNext())
            } catch (err: SortableScannerException) {
                println(err.message)
            }
        }
        data = mData.toList()
    }
    fun report(sortingType: String = DEFAULT_SORTING) {
        val total = data.size
        println("Total ${datumName}s: $total.")
        if (sortingType == "byCount") reportByCount(total) else reportSorted()
    }
    private fun reportSorted() {
        print("Sorted data: ")
        val separator = if (datumName == "line") {
            println()
            "\n"
        } else " "
        println(mergeSort(data, ::chooseLeft).joinToString(separator))
    }
    private fun reportByCount(total: Int) {
        val occurrences = mutableMapOf<T, Int>()
        for (datum in data) occurrences[datum] = (occurrences[datum] ?: 0) + 1
        mergeSort(occurrences.toList()) { x, y ->
            x.second < y.second
                || x.second == y.second && chooseLeft(x.first, y.first)
        }.forEach {
            val percent = it.second * 100 / total
            println("${it.first}: ${it.second} time(s), $percent%")
        }
    }
}

fun <T> mergeSort(data: List<T>, chooseLeft: (T, T) -> Boolean): List<T> {
    if (data.size <= 1) return data
    val half = data.size / 2
    val leftData = mergeSort(data.take(half), chooseLeft)
    val rightData = mergeSort(data.drop(half), chooseLeft)
    val result = mutableListOf<T>()
    var leftPointer = 0
    var rightPointer = 0
    while (leftPointer < leftData.size && rightPointer < rightData.size) {
        result.add(
            if (chooseLeft(leftData[leftPointer], rightData[rightPointer]))
                leftData[leftPointer++]
            else rightData[rightPointer++]
        )
    }
    return result + leftData.drop(leftPointer) + rightData.drop(rightPointer)
}

class Numbers : SortableDataType<Long> {
    override val datumName = "number"
    override fun Scanner.getNext(): Long { // = this.nextLong()
        val next = this.next()
        try {
            return next.toLong()
        } catch (err: NumberFormatException) {
            throw SortableScannerException("\"$next\" is not a long. It will be skipped")
        }
    }
    override fun chooseLeft(left: Long, right: Long) = left < right
}

class Lines : SortableDataType<String> {
    override val datumName = "line"
    override fun Scanner.getNext(): String = this.nextLine()
    override fun chooseLeft(left: String, right: String) = left < right
    // following was for previous stages:
    // override fun chooseLeft(left: String, right: String) = left.length > right.length
}

class Words : SortableDataType<String> {
    override val datumName = "word"
    override fun Scanner.getNext(): String = this.next()
    override fun chooseLeft(left: String, right: String) = left < right
}

fun parseArgs(args: Array<String>): Pair<String, String> {
    var index = 0
    var sortingType = Sortable.DEFAULT_SORTING
    var dataType = Sortable.DEFAULT_TYPE
    while (index < args.size) {
        when (args[index]) {
            "-sortingType" -> try {
                sortingType = args[++index]
            } catch (err: ArrayIndexOutOfBoundsException) {
                throw(ParsingException("No sorting type defined!"))
            }
            "-dataType" -> try {
                dataType = args[++index]
            } catch (err: ArrayIndexOutOfBoundsException) {
                throw(ParsingException("No data type defined!"))
            }
            else -> {
                println("\"${args[index]}\" is not a valid parameter. It will be skipped.")
            }
        }
        ++index
    }
    return dataType to sortingType
}
fun main(args: Array<String>) {
    try {
        parseArgs(args).let { Sortable.forType(it.first).report(it.second) }
    } catch (err: ParsingException) { println(err.message) }
}
