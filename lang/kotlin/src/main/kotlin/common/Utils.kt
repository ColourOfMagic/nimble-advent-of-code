package common

import java.io.File

fun readFileAsString(name: String) = File("data", "$name.txt").readText()