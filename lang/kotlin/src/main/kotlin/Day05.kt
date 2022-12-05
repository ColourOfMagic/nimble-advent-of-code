import java.util.Stack

typealias Crates = MutableMap<Int, Stack<Char>>

class Day05 : Day(
    "Day05",
    firstTestAnswer = "CMZ",
    secondTestAnswer = "MCD"
) {

    override fun fistPart(input: String) =
        input
            .asCraneInput()
            .let { (crates, cranePlan) ->
                val stacks = fillCrates(crates)

                cranePlan
                    .craneCommands()
                    .map { (count, from, to) -> stacks.move9000(from, to, count) }

                stacks.topLayer()
            }

    override fun secondPart(input: String) =
        input
            .asCraneInput()
            .let { (crates, cranePlan) ->
                val stacks = fillCrates(crates)

                cranePlan
                    .craneCommands()
                    .map { (count, from, to) -> stacks.move9001(from, to, count) }

                stacks.topLayer()
            }

    private fun String.asCraneInput() = split("\n\n").run { first() to last() }

    private fun String.craneCommands() = lines().map(::splitToParams)

    private fun Crates.topLayer() = values.map { it.pop() }.toCharArray().concatToString()

    private fun splitToParams(parameters: String) =
        parameters
            .split(' ')
            .let { params -> Triple(params[1].toInt(), params[3].toInt(), params[5].toInt()) }

    private fun Crates.move9000(from: Int, to: Int, count: Int) {
        this[from]
            ?.popSeveral(count)
            ?.map { this[to]?.add(it) }
    }

    private fun Crates.move9001(from: Int, to: Int, count: Int) {
        this[from]
            ?.popSeveral(count)
            ?.reversed()
            ?.map { this[to]?.add(it) }
    }

    private fun fillCrates(crates: String): Crates {
        val crateStacks = mutableMapOf<Int, Stack<Char>>()
        val lines = crates.lines()
        val counters = lines.last().crateCharPositions()
        lines.reversed().drop(1).map { line ->
            counters.mapIndexed { index, position ->
                val crate = line.getOrNull(position).takeIf { it != ' ' }
                if (crate != null) crateStacks.addToStacks(index + 1, crate)
            }
        }
        return crateStacks
    }

    private fun String.crateCharPositions() = split("   ").map { it.trim() }.map { indexOf(it) }

    private fun Crates.addToStacks(position: Int, crate: Char) {
        getOrPut(position, ::Stack).add(crate)
    }

    private fun Stack<Char>.popSeveral(count: Int) = buildList<Char> { repeat(count) { add(pop()) } }
}