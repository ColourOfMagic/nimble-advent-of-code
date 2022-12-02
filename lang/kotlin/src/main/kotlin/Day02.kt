class Day02 : Day(
    "Day02",
    firstTestAnswer = 15,
    secondTestAnswer = 12
) {

    override fun fistPart(input: String): Long =
        input
            .split("\n")
            .map {
                val res = it.split(' ')
                Pair(res.first().toEnum(), res.last().convert().toEnum())
            }
            .sumOf { (first, second) -> Shape.calculateWin(first, second) }
            .toLong()

    override fun secondPart(input: String): Long =
        input
            .split("\n")
            .map {
                val res = it.split(' ')
                Pair(res.first().toEnum(), convert2(res.first(), res.last()).toEnum())
            }
            .sumOf { (first, second) -> Shape.calculateWin(first, second) }
            .toLong()
}

private fun String.convert(): String =
    when (this) {
        "X" -> "A"
        "Y" -> "B"
        "Z" -> "C"
        else -> throw NotImplementedError()
    }

private fun convert2(first: String, second: String): String =
    when (second) {
        "X" -> when (first) {
            "A" -> "C"
            "C" -> "B"
            "B" -> "A"
            else -> throw NotImplementedError()
        }
        "Y" -> first
        "Z" -> when (first) {
            "A" -> "B"
            "C" -> "A"
            "B" -> "C"
            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

private enum class Shape(val num: Int) {
    A(1),
    B(2),
    C(3);

    companion object {
        fun calculateWin(first: Shape, second: Shape) = when {
            first == A && second == B -> 6
            first == B && second == C -> 6
            first == C && second == A -> 6
            first == second -> 3
            else -> 0
        }.let { it + second.num }
    }
}

private fun String.toEnum(): Shape {
    return Shape.valueOf(this)
}