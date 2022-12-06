class Day06 : Day(
    "Day06",
    firstTestAnswer = 6,
    secondTestAnswer = 23
) {

    override fun fistPart(input: String) = findUniqueSequence(input, 4)

    override fun secondPart(input: String) = findUniqueSequence(input, 14)

    private fun findUniqueSequence(input: String, size: Int): Int {
        val dq = ArrayDeque<Char>()
        input.forEachIndexed { index, c ->
            dq.addFirst(c)
            if (dq.size > size) dq.removeLast()
            if (dq.distinct().size == size) return index + 1
        }
        return -1
    }
}