import java.util.*

class Day16 : Day(
    "Day16",
    firstTestAnswer = 1651,
    secondTestAnswer = 1707
) {

    private lateinit var tunnelsMap: List<ValveInfo>
    private lateinit var routeMap: Array<DoubleArray>

    private lateinit var attemptStatistics: AbstractQueue<Pair<Set<Int>, Double>> // points path to total score

    override fun fistPart(input: String): Int {
        tunnelsMap = input.lines().mapIndexed { ind, line -> parseLine(line, ind) }
        routeMap = createFWMatrix(tunnelsMap)
        attemptStatistics = PriorityQueue { _, _ -> 0 }

        val firstPoint = tunnelsMap.first { it.name == "AA" }.num
        val availableValves = tunnelsMap.indices.filter { tunnelsMap[it].rate != 0 }.toSet()
        val time = 30
        val score = 0.0
        return recurseBest(firstPoint, availableValves, time, score, emptySet()).toInt()
    }

    override fun secondPart(input: String): Int {
        tunnelsMap = input.lines().mapIndexed { ind, line -> parseLine(line, ind) }
        routeMap = createFWMatrix(tunnelsMap)
        attemptStatistics = PriorityQueue { o1, o2 -> o2.second.compareTo(o1.second) }
        // Build all useful routes
        val firstPoint = tunnelsMap.first { it.name == "AA" }.num
        val availableValves = tunnelsMap.indices.filter { tunnelsMap[it].rate != 0 }.toSet()
        val time = 26
        val score = 0.0
        recurseBest(firstPoint, availableValves, time, score, emptySet())
        // Find max score of 2 non-intersecting routes
        var max = 0.0
        attemptStatistics.removeIf { it.second < 700 } // "fine-tuning": skip part of useless routes before comparing
        while (attemptStatistics.isNotEmpty()) {
            val path = attemptStatistics.poll()
            val maxOtherScore = attemptStatistics
                .filter { otherPath ->
                    ((path.second + otherPath.second) > max) && path.first.none { it in otherPath.first }
                }
                .maxByOrNull { it.second }?.second

            if (maxOtherScore != null) {
                val maxScore = path.second + maxOtherScore
                if (max < maxScore) max = maxScore
            }
        }

        return max.toInt()
    }

    private fun recurseBest(
        currentPoint: Int,
        availableValves: Set<Int>,
        time: Int,
        score: Double,
        path: Set<Int>
    ): Double {
        val max = availableValves
            .maxOf { point ->
                val lastTime = time - routeMap[currentPoint][point].toInt() - 1 // route time + open valve time

                if (lastTime <= 0) return@maxOf -1.0  // skip unreachable route

                val newScore = score + tunnelsMap[point].rate * lastTime
                val newPath = path.plus(point)
                attemptStatistics.add(newPath to newScore)

                if (availableValves.size == 1) {
                    newScore
                } else {
                    recurseBest(point, availableValves.filter { it != point }.toSet(), lastTime, newScore, newPath)
                }
            }

        return if (max < score) score else max
    }

    private fun parseLine(line: String, num: Int) =
        LINE_REGEX.matchEntire(line)
            ?.groupValues
            ?.run {
                ValveInfo(
                    name = get(1),
                    rate = get(2).toInt(),
                    num = num,
                    tunnels = get(4).split(",").map { it.trim() }.toSet()
                )
            }!!

    /* Build Floyd–Warshall matrix */
    private fun createFWMatrix(tunnelsMap: List<ValveInfo>): Array<DoubleArray> {
        val routeMap = Array(tunnelsMap.size) { DoubleArray(tunnelsMap.size) { Double.POSITIVE_INFINITY } }
        val valveSeq: (ValveInfo) -> List<Pair<Int, Int>> =
            { valve -> valve.tunnels.map { current -> Pair(valve.num, tunnelsMap.first { it.name == current }.num) } }
        tunnelsMap.forEach {
            valveSeq(it).forEach { (sour, dest) -> routeMap[sour][dest] = 1.0 }
        }

        for (k in routeMap.indices) {
            for (i in routeMap.indices) {
                for (j in routeMap.indices) {
                    if (routeMap[i][j] > routeMap[i][k] + routeMap[k][j]) {
                        routeMap[i][j] = routeMap[i][k] + routeMap[k][j]
                    }
                }
            }
        }

        return routeMap
    }

    private class ValveInfo(val name: String, val num: Int, val rate: Int, val tunnels: Set<String>)
    companion object {
        private val LINE_REGEX = Regex("""^Valve (.+) has flow rate=(\d+); .+(valves|valve) (.+)${'$'}""")
    }
}