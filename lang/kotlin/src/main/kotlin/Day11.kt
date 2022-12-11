class Day11 : Day(
    "Day11",
    firstTestAnswer = 10605,
    secondTestAnswer = 2713310158L
) {

    override fun fistPart(input: String): Int {
        val monkeys = buildMonkeys(input)
        val score = mutableMapOf<Int, Int>()
        repeat(20) { _ ->
            monkeys.forEach { (index, monkey) ->
                score[index] = (score[index] ?: 0) + monkey.items.size
                monkey.items.forEach { currentItem ->
                    val worryLevel = monkey.inspectItem(currentItem) / 3
                    monkey.transmitItem(worryLevel, monkeys)
                }
                monkey.items.clear()
            }
        }

        return score.values.sortedDescending().run { get(0) * get(1) }
    }

    override fun secondPart(input: String): Long {
        val monkeys = buildMonkeys(input)
        val score = mutableMapOf<Int, Long>()
        val commonDivider = monkeys.values.map { it.divider }.fold(1) { acc, next -> acc * next }
        repeat(10_000) { ind ->
            monkeys.forEach { (index, monkey) ->
                score[index] = (score[index] ?: 0) + monkey.items.size
                monkey.items.forEach { currentItem ->
                    val worryLevel = monkey.inspectItem(currentItem).mod(commonDivider).toLong()
                    monkey.transmitItem(worryLevel, monkeys)
                }
                monkey.items.clear()
            }
        }
        return score.values.sortedDescending().run { get(0) * get(1) }
    }

    private fun buildMonkeys(input: String) = buildMap {
        parseRecords(input).forEach { (monkeyNumber, monkey) -> put(monkeyNumber, monkey) }
    }

    private fun parseRecords(input: String) =
        input
            .split("\n\n")
            .map(String::lines)
            .map(::recordToMonkey)

    private fun recordToMonkey(record: List<String>): Pair<Int, Monkey> {
        val monkeyNumber = record[0].let { it[it.length - 2].toString().toInt() }
        val monkey = Monkey(
            items = record[1].split(':').last().split(',')
                .map { it.trim().toLong() }
                .let { ArrayDeque(it) },
            operation = record[2].split(' ').let { it[it.size - 2] to it[it.size - 1] },
            divider = record[3].split(' ').last().toInt(),
            trueNum = record[4].split(' ').last().toInt(),
            falseNum = record[5].split(' ').last().toInt()
        )
        return monkeyNumber to monkey
    }

    class Monkey(
        val items: ArrayDeque<Long>,
        val operation: Pair<String, String>, // * to 19
        val divider: Int,
        val trueNum: Int,
        val falseNum: Int
    ) {

        fun inspectItem(num: Long): Long {
            val parameter = operation.second.toLongOrNull()
            return when (operation.first) {
                "*" -> if (parameter != null) num * parameter else num * num
                "+" -> if (parameter != null) num + parameter else num + num
                else -> throw NotImplementedError()
            }
        }

        fun transmitItem(worryLevel: Long, monkeys: Map<Int, Monkey>) =
            if (worryLevel % divider == 0L) {
                monkeys[trueNum]?.items?.addLast(worryLevel)
            } else {
                monkeys[falseNum]?.items?.addLast(worryLevel)
            }
    }
}