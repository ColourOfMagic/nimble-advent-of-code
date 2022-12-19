import PointType.AIR
import PointType.EXT_AIR
import PointType.LAVA

private typealias Map3D = Array<Array<Array<PointType>>>
private typealias LayerFunc = (Int, Int, Int) -> PointType

class Day18 : Day(
    "Day18",
    firstTestAnswer = 64,
    secondTestAnswer = 58
) {

    override fun fistPart(input: String): Int {
        val map = createMap(input)

        var count = 0
        map.scan { current, previous ->
            when (current) {
                LAVA -> if (previous != LAVA) count++
                else -> if (previous == LAVA) count++
            }
        }
        return count
    }

    override fun secondPart(input: String): Int {
        val map = createMap(input)

        map.floodFill(Point(y = 0, x = 0, z = 0))

        var count = 0
        map.scan { current, previous ->
            when (current) {
                LAVA -> if (previous == EXT_AIR) count++
                EXT_AIR -> if (previous == LAVA) count++
                AIR -> {}
            }
        }
        return count
    }

    private fun createMap(input: String): Map3D {
        val points = points(input)

        val (minX, maxX) = points.minMax { x }
        val (minY, maxY) = points.minMax { y }
        val (minZ, maxZ) = points.minMax { z }

        val sizeX = maxX - minX + 3 // min size = dif + 1, array size = min size + 2
        val sizeY = maxY - minY + 3
        val sizeZ = maxZ - minZ + 3

        // map => y, x, z
        return Array(sizeY) { Array(sizeX) { Array(sizeZ) { AIR } } }.also { map ->
            points.forEach { map[it.y - minY + 1][it.x - minX + 1][it.z - minZ + 1] = LAVA } // set points
        }
    }

    private fun scanLayer(
        d1: Int,
        d2: Int,
        d3: Int,
        previous: LayerFunc,
        current: LayerFunc,
        block: (PointType, PointType) -> Unit
    ) =
        repeat(d1) { c1 ->
            repeat(d2) { c2 ->
                repeat(d3) { c3 ->
                    block(current(c1, c2, c3), previous(c1, c2, c3))
                }
            }
        }

    private fun Map3D.scan(block: (PointType, PointType) -> Unit) {
        val ySize = size
        val xSize = first().size
        val zSize = first().first().size

        val previousZ: LayerFunc = { z, y, x -> if (z - 1 >= 0) this[y][x][z - 1] else AIR }
        val previousY: LayerFunc = { y, x, z -> if (y - 1 >= 0) this[y - 1][x][z] else AIR }
        val previousX: LayerFunc = { x, y, z -> if (x - 1 >= 0) this[y][x - 1][z] else AIR }

        scanLayer(zSize, ySize, xSize, { z, y, x -> this[y][x][z] }, previousZ, block)
        scanLayer(ySize, xSize, zSize, { y, x, z -> this[y][x][z] }, previousY, block)
        scanLayer(xSize, ySize, zSize, { x, y, z -> this[y][x][z] }, previousX, block)
    }

    private fun Map3D.floodFill(firstPoint: Point) {
        val stack = ArrayDeque<Point>()
        stack.addFirst(firstPoint)

        while (stack.isNotEmpty()) {
            val point = stack.removeFirst()
            this[point.y][point.x][point.z] = EXT_AIR
            getAround(point)
                .filter { this[it.y][it.x][it.z] == AIR }
                .forEach { stack.addFirst(it) }
        }
    }

    private fun Map3D.getAround(p: Point) =
        listOf(
            Point(y = p.y + 1, x = p.x, z = p.z),
            Point(y = p.y - 1, x = p.x, z = p.z),
            Point(y = p.y, x = p.x + 1, z = p.z),
            Point(y = p.y, x = p.x - 1, z = p.z),
            Point(y = p.y, x = p.x, z = p.z + 1),
            Point(y = p.y, x = p.x, z = p.z - 1),
        )
            .filterNot {
                it.y < 0 || it.x < 0 || it.z < 0
                        || it.y > size - 1 || it.x > first().size - 1 || it.z > first().first().size - 1
            }

    private fun points(input: String) =
        input.lines()
            .map { line ->
                line.split(',')
                    .map(String::toInt)
                    .let { Point(it[0], it[1], it[2]) }
            }

    private fun List<Point>.minMax(block: Point.() -> Int): Pair<Int, Int> =
        map { it.block() }.let { it.min() to it.max() }

    private class Point(val y: Int, val x: Int, val z: Int)

}

private enum class PointType { EXT_AIR, AIR, LAVA }