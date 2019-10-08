package com.github.vladimirshefer.gamebot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@JvmField
val TOKEN = "NjMxMjUyNjY3NDQ4MTY0Mzgy.XZ0KDA.7nN58ZVwF__5_o4_Eodg1tOKW1Q"

@SpringBootApplication
class GamebotApplication

fun main(args: Array<String>) {
	runApplication<GamebotApplication>(*args)
}
