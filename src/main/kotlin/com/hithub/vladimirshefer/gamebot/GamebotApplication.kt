package com.hithub.vladimirshefer.gamebot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GamebotApplication

fun main(args: Array<String>) {
	runApplication<GamebotApplication>(*args)
}
