package de.startat.aoc2023

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class Aoc2023Application : CommandLineRunner {
	override fun run(vararg args: String?) {
		Day2().star1()
		Day2().star2()
		exitProcess(0)
	}
}

fun main(args: Array<String>) {
	runApplication<Aoc2023Application>(*args)
}
