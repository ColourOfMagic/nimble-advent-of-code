package utils

import java.io.File

fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

fun readFileAsString(name: String) = File("data", "$name.txt")
    .readText()