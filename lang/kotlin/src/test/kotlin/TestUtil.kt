package test.kotlin

import java.io.File

internal fun readTestFileAsStringFromTest(name: String) = readFileAsStringFromTest("$name-test")

internal fun readFileAsStringFromTest(name: String) =
    File("../../data", "$name.txt").readText()