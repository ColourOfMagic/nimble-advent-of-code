class Day10 : Day(
    "Day10",
    firstTestAnswer = 13140,
    secondTestAnswer = expectedCrtResult
) {

    override fun fistPart(input: String) =
        instructions(input)
            .let { instructions ->
                var tick = 0
                var result = 0
                val steps = listOf(20, 60, 100, 140, 180, 220)
                runInstructionTicks(instructions) { register ->
                    tick++
                    if (tick in steps) result += tick * register
                }
                result
            }

    override fun secondPart(input: String) =
        instructions(input)
            .let { instructions ->
                var tick = 0
                val steps = listOf(40, 80, 120, 160, 200, 240)
                val crtScreen = StringBuilder()
                var position = 0

                runInstructionTicks(instructions) { register ->
                    tick++
                    if (position in listOf(register - 1, register, register + 1)) {
                        crtScreen.append("#")
                    } else {
                        crtScreen.append(".")
                    }
                    position++

                    if (tick in steps) {
                        crtScreen.append('\n')
                        position = 0
                    }
                }

                crtScreen.dropLast(1).toString() // CRT screen except last \n
            }

    private fun instructions(input: String) = input.lines().map { it.split(" ") }

    private fun runInstructionTicks(instructions: List<List<String>>, tick: (Int) -> Unit) {
        var register = 1
        instructions.forEach { command ->
            when (command.first()) {
                "addx" -> repeat(2) { tick(register) }.also { register += command.last().toInt() }
                "noop" -> repeat(1) { tick(register) }
            }
        }
    }

    companion object {

        private val expectedCrtResult = """
            ##..##..##..##..##..##..##..##..##..##..
            ###...###...###...###...###...###...###.
            ####....####....####....####....####....
            #####.....#####.....#####.....#####.....
            ######......######......######......####
            #######.......#######.......#######.....
        """.trimIndent()
    }
}