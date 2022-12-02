abstract class Day(
    val fileName: String,
    val firstTestAnswer: Long? = null,
    val secondTestAnswer: Long? = null
) {

    private val inputString: String
        get() = readFileAsString(fileName)

    fun runFirstPart() {
        fistPart(inputString)
    }

    abstract fun fistPart(input: String): Long
    abstract fun secondPart(input: String): Long
}