class Day07 : Day(
    "Day07",
    firstTestAnswer = 95437,
    secondTestAnswer = 24933642
) {

    override fun fistPart(input: String) =
        buildStructure(input)
            .flatDirs()
            .map { it.size }
            .filter { it <= 100_000 }
            .sum()

    override fun secondPart(input: String) =
        buildStructure(input)
            .let { root ->
                val total = 70_000_000
                val unused = total - root.size
                val needed = 30_000_000 - unused
                root
                    .flatDirs()
                    .map { it.size }
                    .sorted()
                    .first { it > needed }
            }

    private fun buildStructure(input: String) =
        input.lines().drop(1).let { commands -> Dir(name = "/").also { buildStructure(it, commands) } }

    private fun buildStructure(dir: Dir, commands: List<String>) {
        var tempDir = dir
        commands.forEach { command ->
            val args = command.split(' ')
            when (args.first()) {
                "$" -> if (args[1] == "cd") {
                    tempDir = when {
                        args[2] == ".." -> tempDir.parent!!
                        else -> tempDir.getChildren(args[2])
                    }
                }

                "dir" -> tempDir.children.add(Dir(name = args.last(), parent = tempDir))

                else -> tempDir.children.add(File(name = args.last(), size = args.first().toInt()))
            }
        }
    }

    private fun Dir.flatDirs() = mutableListOf<Dir>().also { flatDir(it, this) }

    private fun flatDir(list: MutableList<Dir>, dir: Dir) {
        list.add(dir)
        dir.children.filterIsInstance<Dir>().forEach { flatDir(list, it) }
    }

    interface Element {

        var name: String
        val size: Int
    }

    class Dir(
        override var name: String = "",
        var parent: Dir? = null,
        val children: MutableList<Element> = mutableListOf()
    ) : Element {

        fun getChildren(dirName: String) = children.filterIsInstance<Dir>().first { it.name == dirName }

        override val size: Int get() = children.sumOf { it.size }
    }

    class File(override var name: String = "", override var size: Int = 0) : Element
}