class Day01 : Day(
    "Day01",
    firstTestAnswer = 24000,
    secondTestAnswer = 45000
) {

    override fun fistPart(input: String): Long =
        input
            .split("\n\n")
            .map { part -> part.lines().sumOf { it.toInt() } }
            .max()
            .toLong()

    override fun secondPart(input: String): Long =
        input
            .split("\n\n")
            .map { part -> part.lines().sumOf { it.toInt() } }
            .sortedDescending()
            .slice(0..2)
            .sum()
            .toLong()
}