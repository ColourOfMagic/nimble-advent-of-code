import utils.readFileAsString

// TODO: create abstract class and common test
fun main() {

    second()

    println("Done !")
}

private fun fistTest() {
    val result = readFileAsString("Day02-test")
        .split("\n")
        .map {
            val res = it.split(' ')
            Pair(res.first().toEnum(), res.last().convert().toEnum())
        }
        .map { (first, second) ->
            Shape.calculateWin(first, second)
        }.sum()

    check(result == 15)
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

private fun first() {
    val result = readFileAsString("Day02")
        .split("\n")
        .map {
            val res = it.split(' ')
            Pair(res.first().toEnum(), res.last().convert().toEnum())
        }
        .map { (first, second) ->
            Shape.calculateWin(first, second)
        }.sum()

    println("Answer: $result")
}

private fun secondTest() {
    val result = readFileAsString("Day02-test")
        .split("\n")
        .map {
            val res = it.split(' ')
            Pair(res.first().toEnum(), convert2(res.first(), res.last()).toEnum())
        }
        .map { (first, second) ->
            Shape.calculateWin(first, second)
        }.sum()

    val answer = 12
    check(result == answer) { "$result != $answer" }
}

private fun second() {
    val result = readFileAsString("Day02")
        .split("\n")
        .map {
            val res = it.split(' ')
            Pair(res.first().toEnum(), convert2(res.first(), res.last()).toEnum())
        }
        .map { (first, second) ->
            Shape.calculateWin(first, second)
        }.sum()

    println("Answer: $result")
}