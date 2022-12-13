import common.readFileAsString

abstract class Day(
    val fileName: String,
    val firstTestAnswer: Any? = null,
    val secondTestAnswer: Any? = null
) {

    val inputString: String
        get() = readFileAsString(fileName)

    val inputTestString: String
        get() = readFileAsString("$fileName-test")

    abstract fun fistPart(input: String): Any
    abstract fun secondPart(input: String): Any
}