class Day01 : Day(
    "Day01",
    firstTestAnswer = 24000,
    secondTestAnswer = 45000
) {

    override fun fistPart(input: String) =
        input
            .split("\n\n")
            .map { part -> part.lines().sumOf { it.toInt() } }
            .max()

    override fun secondPart(input: String) =
        input
            .split("\n\n")
            .map { part -> part.lines().sumOf { it.toInt() } }
            .sortedDescending()
            .slice(0..2)
            .sum()
}