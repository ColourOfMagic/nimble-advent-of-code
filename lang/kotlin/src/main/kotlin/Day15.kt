import kotlin.math.abs

class Day15 : Day(
    "Day15",
    firstTestAnswer = 26,
    secondTestAnswer = 56000011L
) {

    private val searchYLines = ArrayDeque(listOf(10, 2000000)) // first - test, second - real
    private val limits = ArrayDeque(listOf(20, 4000000))


    override fun fistPart(input: String) = input.lines().let { lines ->
        val beaconsInLine = mutableListOf<Long>()
        val searchY = searchYLines.removeFirst().toLong()
        val sensorRanges = mutableListOf<LongRange>()

        lines.map { line ->
            val (sX, sY, bX, bY) = getNums(line).map { it.toLong() }
            val s = Point(sX, sY)
            val b = Point(bX, bY)
            if (b.y == searchY) beaconsInLine.add(b.x)
            s to distance(s, b)
        }
            .forEach { (sensor, r) ->
                val d = r * 2 + 1
                if (searchY in sensor.y - r..sensor.y + r) {
                    val searchD = d - abs(sensor.y - searchY) * 2
                    val searchR = (searchD - 1) / 2
                    sensorRanges.add(sensor.x - searchR..sensor.x + searchR)
                }
            }

        (sensorRanges.minOf { it.first }..sensorRanges.maxOf { it.last })
            .count { sensorRanges.any { el -> it in el } && it !in beaconsInLine }
    }

    override fun secondPart(input: String): Long =
        input
            .lines()
            .map { line ->
                val (sX, sY, bX, bY) = getNums(line).map { it.toLong() }
                val s = Point(sX, sY)
                val b = Point(bX, bY)
                s to distance(s, b)
            }
            .let { points ->
                val acfs = mutableListOf<Long>()
                val bcfs = mutableListOf<Long>()
                val scope = 0..limits.removeFirst()

                points.forEach { (point, r) ->
                    acfs.add(point.y - point.x + r + 1)
                    acfs.add(point.y - point.x - r - 1)
                    bcfs.add(point.x + point.y + r + 1)
                    bcfs.add(point.x + point.y - r - 1)
                }

                acfs.forEach { acf ->
                    bcfs.forEach { bcf ->
                        val p = Point((bcf - acf) / 2, (acf + bcf) / 2)
                        if (p.between(scope)) {
                            if (points.none { (point, r) -> distance(p, point) <= r })
                                return p.x * 4000000 + p.y
                        }
                    }
                }
                throw NoSuchElementException()
            }

    private fun getNums(data: String): List<String> = Regex("[-\\d]+").findAll(data).map { it.value }.toList()

    private fun distance(p1: Point, p2: Point) = abs(p1.x - p2.x) + abs(p1.y - p2.y)

    class Point(val x: Long, val y: Long) {

        fun between(scope: IntRange) = scope.run { first < x && x < last && first < y && y < last }
    }
}
