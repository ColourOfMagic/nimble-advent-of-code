import kotlin.math.abs

class Day09 : Day(
    "Day09",
    firstTestAnswer = 13,
    secondTestAnswer = 1
) {

    override fun fistPart(input: String): Int {
        val tail = Point()

        val coordinates = mutableListOf<Pair<Int, Int>>(tail.x to tail.y)

        pointMilestones(input) { head ->
            tail.ifDistant(head)?.let {
                tail.moveTo(head)
                if (coordinates.none { (x, y) -> tail.x == x && tail.y == y }) coordinates.add(tail.x to tail.y)
            }
        }
        return coordinates.count()
    }

    override fun secondPart(input: String): Int {
        val ropePoints = (1..9).map { Point() } // 1..8 + tail point

        val coordinates = mutableListOf<Pair<Int, Int>>(ropePoints.last().x to ropePoints.last().y)

        pointMilestones(input) { head ->
            ropePoints.first().ifDistant(head)?.moveTo(head) // move first rope point

            ropePoints // move all rope
                .drop(1)
                .forEachIndexed { index, point -> point.ifDistant(ropePoints[index])?.moveTo(ropePoints[index]) }

            ropePoints // add tail coordinate to collection
                .last()
                .also { if (coordinates.none { (x, y) -> it.x == x && it.y == y }) coordinates.add(it.x to it.y) }
        }
        return coordinates.count()
    }

    private fun pointMilestones(input: String, block: (Point) -> Unit) {
        val head = Point()
        commands(input)
            .map { (dir, count) -> performCommand(dir, count, head, block) }
    }

    private fun Point.ifDistant(other: Point) = takeIf { abs(x - other.x) > 1 || abs(y - other.y) > 1 }

    private fun commands(input: String) =
        input
            .lines()
            .map { it.split(' ').run { first() to last().toInt() } }

    private fun performCommand(dir: String, count: Int, point: Point, block: (Point) -> Unit) =
        repeat(count) {
            when (dir) {
                "R" -> point.x++
                "L" -> point.x--
                "U" -> point.y++
                "D" -> point.y--
            }
            block(point)
        }

    class Point(var x: Int = 0, var y: Int = 0) {

        fun moveTo(other: Point) {
            val difX = other.x - x
            val difY = other.y - y

            when {
                abs(difX) > 0 && abs(difY) > 0 -> {
                    if (difX > 0) x++ else x--
                    if (difY > 0) y++ else y--
                }

                abs(difY) == 2 -> if (difY > 0) y++ else y--
                abs(difX) == 2 -> if (difX > 0) x++ else x--
            }
        }
    }
}