import utils.readFileAsString

abstract class Day(
    private val fileName: String,
    firstTest: Long = throw NotImplementedError(),
    secondTest: Long = throw NotImplementedError()
) {

    protected val inputString: String
        get() = readFileAsString(fileName)

    abstract fun fist(): Long;
}