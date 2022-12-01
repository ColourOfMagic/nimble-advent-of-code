package utils

import java.io.File

fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

fun readInputAsString(name: String) = File("data", "$name.txt")
    .readText()