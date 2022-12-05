class Day03 : Day(
    "Day03",
    firstTestAnswer = 157,
    secondTestAnswer = 70
) {

    override fun fistPart(input: String) =
        input
            .lines()
            .map {
                val mid = it.length / 2
                Pair(it.substring(0, mid), it.substring(mid))
            }
            .map { (first, second) ->
                val sSet = second.toSet()
                first.toSet().first { sSet.contains(it) }
            }
            .sumOf { it.toAdventNumber() }

    override fun secondPart(input: String) =
        input
            .lines()
            .chunked(3)
            .map { part ->
                part[0].first { part[1].contains(it) && part[2].contains(it) }
            }
            .sumOf { it.toAdventNumber() }

    private fun Char.toAdventNumber() = if (isLowerCase()) code - 96 else lowercaseChar().code - 96 + 26
}