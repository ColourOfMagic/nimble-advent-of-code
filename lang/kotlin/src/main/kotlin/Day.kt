abstract class Day(
    val fileName: String,
    val firstTestAnswer: Long? = null,
    val secondTestAnswer: Long? = null
) {

    val inputString: String
        get() = readFileAsString(fileName)

    val inputTestString: String
        get() = readFileAsString("$fileName-test")

    abstract fun fistPart(input: String): Long
    abstract fun secondPart(input: String): Long
}