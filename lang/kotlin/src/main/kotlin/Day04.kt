class Day04 : Day(
    "Day04",
    firstTestAnswer = 2,
    secondTestAnswer = 4
) {

    override fun fistPart(input: String): Long =
        input
            .lines()
            .map {
                it.split(',').run { first().toRange() to last().toRange() }
            }.count { (first, second) ->
                first.all(second) || second.all(first)
            }
            .toLong()

    override fun secondPart(input: String): Long =
        input
            .lines()
            .map {
                it.split(',').run { first().toRange() to last().toRange() }
            }
            .count { (first, second) ->
                first.any(second) || second.any(first)
            }
            .toLong()

    private fun IntRange.all(other: IntRange) = this.all { other.contains(it) }

    private fun IntRange.any(other: IntRange) = this.any { other.contains(it) }

    private fun String.toRange() = split('-').let { (first, second) -> first.toInt()..second.toInt() }
}