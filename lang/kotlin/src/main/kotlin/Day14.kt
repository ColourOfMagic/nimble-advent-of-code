private typealias Coordinates = List<List<Point>>
private typealias CaveMap = Array<Array<Char?>>

class Day14 : Day(
    "Day14",
    firstTestAnswer = 24,
    secondTestAnswer = 93
) {

    override fun fistPart(input: String) =
        coordinates(input)
            .let { coordinates ->
                val (maxX, maxY) = coordinates.maxCoordinates()

                val map = Array(maxY + 2) { arrayOfNulls<Char?>(maxX + 2) }

                coordinates.forEach { map.buildCaves(it) }

                dropAllItems(map) { y, x ->
                    if (y == map.size - 1) {
                        false
                    } else {
                        map[y][x] = 'o'
                        true
                    }
                }

                map.sumOf { it.count { it == 'o' } }
            }

    override fun secondPart(input: String) =
        coordinates(input)
            .let { coordinates ->
                val (maxX, maxY) = coordinates.maxCoordinates()

                val mass = Array(maxY + 3) { arrayOfNulls<Char?>(maxX + 400 + 2) }

                coordinates.forEach { mass.buildCaves(it) }

                (mass.first().indices).forEach { mass[maxY + 2][it] = '#' } // add floor

                dropAllItems(mass) { y, x ->
                    mass[y][x] = 'o'
                    !(x == SPAWN_POINT_X && y == SPAWN_POINT_Y) // not a spawn point
                }

                mass.sumOf { it.count { it == 'o' } }
            }

    private fun Coordinates.maxCoordinates(): Pair<Int, Int> {
        val maxBy: Coordinates.(block: Point.() -> Int) -> Int = { block ->
            maxOfOrNull { list -> list.maxOfOrNull { it.block() }!! }!!
        }

        return maxBy { x } to maxBy { y }
    }

    private fun coordinates(input: String) =
        input.lines()
            .map { line ->
                line
                    .split(" -> ")
                    .map { point -> point.split(",").run { Point(first().toInt(), last().toInt()) } }
            }

    private fun dropAllItems(map: CaveMap, block: (Int, Int) -> Boolean) {
        var continueDrop: Boolean
        do {
            continueDrop = skipSpaceAndDrop(map, block)
        } while (continueDrop)
    }

    private fun skipSpaceAndDrop(map: CaveMap, block: (Int, Int) -> Boolean): Boolean {
        val dY = map.indexOfFirst { it[SPAWN_POINT_X] != null } - 1
        return drop(map, SPAWN_POINT_X, dY, block)
    }

    private fun drop(map: CaveMap, dx: Int, dy: Int, block: (Int, Int) -> Boolean): Boolean = with(map.with(dx, dy)) {
        while (y != map.size - 1 && anySpaceUnder) {
            when {
                emptyUnder -> y++
                emptyLeft -> {
                    x--
                    y++
                }

                emptyRight -> {
                    x++
                    y++
                }
            }
        }
        return block(y, x)
    }

    private fun CaveMap.buildCaves(line: List<Point>) {
        line.reduce { p1, p2 ->
            if (p1.x == p2.x) {
                drawVerticalLine(x = p1.x, yRange = if (p1.y < p2.y) p1.y..p2.y else p2.y..p1.y)
            } else {
                drawHorizontalLine(y = p1.y, xRange = if (p1.x < p2.x) p1.x..p2.x else p2.x..p1.x)
            }
            p2
        }
    }

    private fun CaveMap.drawHorizontalLine(y: Int, xRange: IntRange) = xRange.forEach { x -> this[y][x] = '#' }

    private fun CaveMap.drawVerticalLine(x: Int, yRange: IntRange) = yRange.forEach { y -> this[y][x] = '#' }

    private class Position(val map: CaveMap, var x: Int, var y: Int) {

        val emptyUnder get() = map[y + 1][x] == null

        val emptyLeft get() = map[y + 1][x - 1] == null

        val emptyRight get() = map[y + 1][x + 1] == null

        val anySpaceUnder get() = emptyUnder || emptyLeft || emptyRight

    }

    private fun CaveMap.with(x: Int, y: Int) = Position(this, x, y)

    companion object {

        private const val SPAWN_POINT_X = 500
        private const val SPAWN_POINT_Y = 0
    }
}

private class Point(val x: Int, val y: Int)
