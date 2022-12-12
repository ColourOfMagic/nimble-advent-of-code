class Day12 : Day(
    "Day12",
    firstTestAnswer = 31,
    secondTestAnswer = 29
) {

    override fun fistPart(input: String) =
        riverMap(input)
            .let { map ->
                val routeMap = createRouteMap(map)
                val firstPoint = map.findElement(FIRST_POINT).single()

                routeMap[firstPoint.y][firstPoint.x]!!
            }

    override fun secondPart(input: String) =
        riverMap(input)
            .let { map ->
                val routeMap = createRouteMap(map)
                val aPoints = map.findElement('a')

                aPoints.mapNotNull { routeMap[it.y][it.x] }.min()
            }

    private fun riverMap(input: String) = input.lines().map { it.toList() }

    private fun List<List<Char>>.nearest(point: Point) = buildList<Point> {
        if (point.x - 1 >= 0) add(toPoint(point.x - 1, point.y))
        if (point.y - 1 >= 0) add(toPoint(point.x, point.y - 1))
        if (point.x + 1 < this@nearest.first().size) add(toPoint(point.x + 1, point.y))
        if (point.y + 1 < this@nearest.size) add(toPoint(point.x, point.y + 1))
    }

    private fun List<List<Char>>.toPoint(x: Int, y: Int) = Point(this[y][x], x, y)

    private fun createRouteMap(map: List<List<Char>>): Array<Array<Int?>> {
        val routeMap = Array<Array<Int?>>(map.size) { arrayOfNulls<Int?>(map.first().size) }
        val lastPoint = map.findElement(LAST_POINT).single()

        map.markUpAround(lastPoint, 0, routeMap)
        return routeMap
    }

    private fun List<List<Char>>.markUpAround(point: Point, num: Int = 0, routeMap: Array<Array<Int?>>) {
        routeMap[point.y][point.x] = num

        nearest(point)
            .filter { el -> el.height >= point.height - 1 }
            .forEach {
                val mark = routeMap[it.y][it.x]
                if (mark == null || mark > num + 1) markUpAround(it, num + 1, routeMap)
            }
    }

    private fun List<List<Char>>.findElement(element: Char): List<Point> =
        mutableListOf<Point>().also { elements ->
            forEachIndexed { y, e ->
                e.forEachIndexed { x, el ->
                    if (el == element) elements.add(Point(element, x, y))
                }
            }
        }

    class Point(private val char: Char, val x: Int, val y: Int) {

        val height: Int
            get() = when (char) {
                FIRST_POINT -> 'a'.code - 1
                LAST_POINT -> 'z'.code + 1
                else -> char.code
            }
    }

    companion object {

        private const val FIRST_POINT = 'S'
        private const val LAST_POINT = 'E'
    }
}