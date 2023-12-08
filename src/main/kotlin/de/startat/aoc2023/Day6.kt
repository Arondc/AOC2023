package de.startat.aoc2023

import kotlin.math.sqrt

data class Race(val raceTime:Long, val bestDistance : Long)

class Day6 {
    fun star1(){
        var numberOfResults = 1
        numberOfResults *= simulateRace(Race(46,214))
        numberOfResults *= simulateRace(Race(80,1177))
        numberOfResults *= simulateRace(Race(78,1402))
        numberOfResults *= simulateRace(Race(66,1024))
        println("Die Lösung für Stern 1 ist $numberOfResults")
    }
    fun star2(){
        val numberOfResults = simulateRace(Race(46807866,214117714021024))
        println("Die Lösung für Stern 2 ist $numberOfResults")
    }

    private fun simulateRace(race : Race): Int {
        val loadingTimes = findLowestAndHighestLoadingTime(race)
        var corrector = 0 //I count one possibility too much if the right border is exactly at a value without fractional part
        if(loadingTimes.second.toInt().toDouble().equals(loadingTimes.second)){
            corrector = 1
        }
        val possibilities = loadingTimes.second.toInt() - loadingTimes.first.toInt() - corrector
        println("Es gibt $possibilities Möglichkeiten den Rekord zu brechen für $race")
        return possibilities
    }

    private fun findLowestAndHighestLoadingTime(race: Race): Pair<Double, Double> {
        val a : Double = (-1).toDouble()
        val b : Double = race.raceTime.toDouble()
        val c : Double = (-1*race.bestDistance).toDouble()
        val x1 = (-b+ sqrt(b*b - 4*a*c))/(2*a)
        val x2 = (-b- sqrt(b*b - 4*a*c))/(2*a)
        return Pair(x1,x2)
    }
}

val day6testInput = """
    Time:      7  15   30
    Distance:  9  40  200
""".trimIndent()

val day6input = """
    Time:        46     80     78     66
    Distance:   214   1177   1402   1024
""".trimIndent()