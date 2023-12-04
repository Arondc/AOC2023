package de.startat.aoc2023

class Day1 {
    private val literalReplacements = sequenceOf(
        "zero" to 0,
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    fun part1() {
        calculateSolution(false)
    }

    fun part2() {
        calculateSolution(true)
    }

    private fun calculateSolution(replaceLiterals: Boolean) {
        val input: String = day1Input1

        val sum = input.lines().map { l ->
            println(l)
            findAllIndexedNumbers(l, replaceLiterals)
        }.map { numbers ->
            "${numbers.first().second}${numbers.last().second}".toInt()
        }.reduce { sum, number -> sum + number }

        println()
        println("Die Summe ist $sum")

    }

    private fun findAllIndexedNumbers(
        l: String, replaceLiterals: Boolean
    ): List<Pair<Int, Int>>
    {
        val normalIndexedNumbers = l.mapIndexedNotNull { i, c ->
            when {
                c.isDigit() -> i to c.digitToInt()
                else -> null
            }
        }
        println("\tnormal numbers: $normalIndexedNumbers")
        return if (replaceLiterals) {
            (findLiterals(l) + normalIndexedNumbers).sortedBy { it.first }
        } else {
            normalIndexedNumbers
        }
    }


    private fun findLiterals(line: String): List<Pair<Int, Int>> {

        val occurrences = literalReplacements.flatMap { repl ->
            Regex(repl.first).findAll(line).map { result -> result.range.first to repl.second }
        }.toList()
        println("\tliteral numbers: $occurrences")
        return occurrences
    }


}