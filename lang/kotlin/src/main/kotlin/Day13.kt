class Day13 : Day(
    "Day13",
    firstTestAnswer = 13,
    secondTestAnswer = 140
) {

    private val dividerPackets = listOf("[[2]]", "[[6]]")

    private val orderComparator = Comparator { first: String, second: String ->
        isRightOrder(first, second).let { if (it) -1 else 1 }
    }

    override fun fistPart(input: String) =
        input
            .split("\n\n")
            .map { it.lines().run { first() to last() } }
            .map { (first, last) -> isRightOrder(first, last) }
            .mapIndexedNotNull { index, b -> if (b) index + 1 else null }
            .sum()

    override fun secondPart(input: String) =
        input
            .split("\n\n")
            .flatMap { it.lines() }
            .let { inputPackets ->
                (inputPackets + dividerPackets)
                    .sortedWith(orderComparator)
                    .mapIndexedNotNull { i, el -> if (el in dividerPackets) i + 1 else null }
                    .fold(1) { acc, next -> acc * next }
            }

    private fun isRightOrder(first: String, second: String) = compareLists(parse(first), parse(second))!!

    private fun parse(str: String): DataList {
        val result = ArrayDeque<MutableList<Element>>()

        var number = ""
        val addNumber: () -> Unit = {
            if (number.isNotEmpty()) result.first().add(DataValue(number.toInt())).also { number = "" }
        }
        str.forEach { c ->
            when (c) {
                '[' -> result.addFirst(mutableListOf())
                ']' -> {
                    addNumber()
                    val el = DataList(result.removeFirst())
                    if (result.isEmpty()) return el
                    result.first().add(el)
                }

                in '0'..'9' -> number += c

                else -> addNumber()
            }
        }
        return DataList(result.removeFirst())
    }

    private fun convert(first: Element, second: Element): Pair<DataList, DataList> {
        val asList: (Element) -> DataList = { el -> if (el is DataList) el else DataList(mutableListOf(el)) }
        return asList(first) to asList(second)
    }

    private fun compareLists(first: DataList, second: DataList): Boolean? {
        val firstElements: MutableList<Element> = first.nested
        val secondElements = second.nested

        if (firstElements.isEmpty() && secondElements.isEmpty()) return null
        if (firstElements.isEmpty()) return true
        if (secondElements.isEmpty()) return false

        repeat(listOf(firstElements.size, secondElements.size).max()) { index ->
            val result = compareElements(firstElements.getOrNull(index), secondElements.getOrNull(index))
            if (result != null) return result
        }
        return null
    }

    private fun compareElements(first: Element?, second: Element?): Boolean? =
        when {
            first == null -> true
            second == null -> false
            first is DataList || second is DataList -> convert(first, second).let { compareLists(it.first, it.second) }
            else -> {
                val firstValue = (first as DataValue).number
                val secondValue = (second as DataValue).number
                when {
                    firstValue < secondValue -> true
                    firstValue > secondValue -> false
                    else -> null
                }
            }
        }

    sealed interface Element

    class DataValue(val number: Int) : Element

    class DataList(val nested: MutableList<Element>) : Element
}