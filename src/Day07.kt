fun main() {
    fun log(message: Any?) {
        println(message)
    }

    class Dir(val name: String, val parent: Dir? = null) {
        val childDirs: MutableList<Dir> = mutableListOf()
        var fileSize: Int = 0
        val fileSizeRecursive: Int get() = fileSize + childDirs.sumOf { it.fileSizeRecursive }
        override fun toString(): String =
            "Dir(\"$name\", ${parent?.name}, $fileSize, $fileSizeRecursive, [${childDirs.joinToString { it.name }}])"
    }

    fun parts(input: List<String>, part: Int = 1): Int {
        val rootDir = Dir("/")
        var cwd: Dir = rootDir
        val dirs: MutableList<Dir> = mutableListOf()
        dirs.add(rootDir)

        input.forEach { cmd ->
//            log("INS $cmd")
            when {
                // 123 filename
                cmd[0].isDigit() -> {
                    val fileSize: Int = cmd.substringBefore(" ").toInt()
                    cwd.fileSize += fileSize
                }
                // dir dirName, don't care (we'll handle them on cd only)
                cmd.startsWith("dir ") -> {}
                // $ ls, don't care, we'll pick up the file sizes anyway
                cmd.startsWith("$ ls") -> {}
                // cd [/|..|dirname]
                cmd.startsWith("$ cd ") -> {
                    val dirName = cmd.substringAfter("$ cd ")
                    cwd = when (dirName) {
                        "/" -> rootDir
                        ".." -> (if (cwd.parent != null) cwd.parent else cwd)!!
                        else -> Dir(dirName, cwd).apply {
                            cwd.childDirs.add(this)
                            dirs.add(this)
                        }
                    }
                }
                // wtf
                else -> error("CMD $cmd")
            }
        }
//        log("DIRS $dirs")
        val smallerDirs = dirs.filter { it.fileSizeRecursive <= 100000 }.map { it.name }
//        log("SMALL ${smallerDirs.joinToString()}")

        return when (part) {
            1 -> dirs.filter { it.fileSizeRecursive <= 100000 }.sumOf { it.fileSizeRecursive }
            2 -> {
                val diskMax = 70000000
                val need  = 30000000
                val unused = diskMax - rootDir.fileSizeRecursive
                val missingSpace = need - unused
//                log("BIG ENUF ${dirs.filter { it.fileSizeRecursive > missingSpace }}")
                val delDir = dirs.filter { it.fileSizeRecursive > missingSpace }.map { it.fileSizeRecursive }.min()
//                log("DEL DIR size $delDir")
                delDir
            }
            else -> error("PART WUT? $part")
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test.txt")
    check(parts(testInput) == 95437)
    check(parts(testInput, 2) == 24933642)


    val input = readInput("Day07.txt")
    println(parts(input))
    check(parts(input) == 1444896)
    println(parts(input, 2))
    check(parts(input, 2) == 404395)
}
