typealias TreeMatrix = List<List<Int>>

class Day08 : Day(
    "Day08",
    firstTestAnswer = 21,
    secondTestAnswer = 8
) {

    // TODO: i think it's ugly code, refactoring is needed
    override fun fistPart(input: String) =
        matrix(input)
            .let {
                val leftLimit = it.first().size
                val downLimit = it.size
                var total = 0
                it.forEachIndexed { topIndex, line ->
                    line.forEachIndexed { rightIndex, el ->
                        if (topIndex == 0 || topIndex == downLimit - 1 || rightIndex == 0 || rightIndex == leftLimit - 1) {
                            total++
                        } else {
                            if (
                                it.checkLeftRight(0 until rightIndex, topIndex) < el
                                || it.checkLeftRight(rightIndex + 1 until leftLimit, topIndex) < el
                                || it.checkTopDown(0 until topIndex, rightIndex) < el
                                || it.checkTopDown(topIndex + 1 until downLimit, rightIndex) < el
                            ) total++
                        }
                    }

                }
                total
            }

    private fun TreeMatrix.checkLeftRight(ck: IntRange, topIndex: Int): Int {
        val line = get(topIndex)
        return ck.maxOf { line[it] }
    }

    private fun TreeMatrix.checkTopDown(ck: IntRange, rightIndex: Int): Int {
        return ck.maxOf {
            get(it)[rightIndex]
        }
    }

    override fun secondPart(input: String) =
        matrix(input)
            .let {
                val leftLimit = it.first().size
                val downLimit = it.size
                var total = 0
                it.forEachIndexed { topIndex, line ->
                    line.forEachIndexed { rightIndex, el ->

                        val current = it.getRightLeft(0 until rightIndex, topIndex, el) *
                                it.getLeftRight(rightIndex + 1 until leftLimit, topIndex, el) *
                                it.getDownTop(0 until topIndex, rightIndex, el) *
                                it.getTopDown(topIndex + 1 until downLimit, rightIndex, el)

                        if (current > total) total = current
                    }
                }
                total
            }

    private fun matrix(input: String): TreeMatrix = input.lines().map { list -> list.map { it.toString().toInt() } }

    private fun TreeMatrix.getRightLeft(ck: IntRange, topIndex: Int, el: Int): Int {
        if (ck.singleOrNull() != null) return 1
        val line = get(topIndex)
        var count = 0
        ck.reversed().forEach {
            if (line[it] >= el) {
                count++
                return count
            } else count++
        }
        return count
    }

    private fun TreeMatrix.getLeftRight(ck: IntRange, topIndex: Int, el: Int): Int {
        if (ck.singleOrNull() != null) return 1
        val line = get(topIndex)
        var count = 0
        ck.forEach {
            if (line[it] >= el) {
                count++
                return count
            } else count++
        }
        return count
    }

    private fun TreeMatrix.getTopDown(ck: IntRange, rightIndex: Int, el: Int): Int {
        if (ck.singleOrNull() != null) return 1

        var count = 0
        ck.forEach {
            if (get(it)[rightIndex] >= el) {
                count++
                return count
            } else count++
        }
        return count
    }

    private fun TreeMatrix.getDownTop(ck: IntRange, rightIndex: Int, el: Int): Int {
        if (ck.singleOrNull() != null) return 1
        var count = 0
        ck.reversed().forEach {
            if (get(it)[rightIndex] >= el) {
                count++
                return count
            } else count++
        }
        return count
    }
}