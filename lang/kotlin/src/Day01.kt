import utils.readFileAsString

// TODO: create abstract class and common test
fun main() {

    second()

    println("Done !")
}

private fun fistTest() {
    val result = readFileAsString("Day01-test")
        .split("\n\n")
        .map { part -> part.lines().sumOf { it.toInt() } }
        .max()

    check(result == 24000)
}

private fun first() {
    val result = readFileAsString("Day01")
        .split("\n\n")
        .map { part -> part.lines().sumOf { it.toInt() } }
        .max()

    println("Answer: $result")
}

private fun secondTest() {
    val result = readFileAsString("Day01-test")
        .split("\n\n")
        .map { part -> part.lines().sumOf { it.toInt() } }
        .sortedDescending()
        .slice(0..2)
        .sum()
    val answer = 45000
    check(result == answer) { "$result != $answer" }
}

private fun second() {
    val result = readFileAsString("Day01")
        .split("\n\n")
        .map { part -> part.lines().sumOf { it.toInt() } }
        .sortedDescending()
        .slice(0..2)
        .sum()
    println("Answer: $result")
}