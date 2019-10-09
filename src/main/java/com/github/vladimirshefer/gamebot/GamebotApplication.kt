package com.github.vladimirshefer.gamebot

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@JvmField
val TOKEN = "NjMxMjUyNjY3NDQ4MTY0Mzgy.XZ0KDA.7nN58ZVwF__5_o4_Eodg1tOKW1Q"

fun main(args: Array<String>) {
	runApplication<GamebotApplication>(*args)
}

@SpringBootApplication
class GamebotApplication {

	@Bean
	fun javaDiscordApi(listeners: Array<ListenerAdapter>): JDA {
		return JDABuilder(TOKEN)
				.addEventListeners(*listeners)
				.setActivity(Activity.playing("Role games"))
				.build()
	}

}
