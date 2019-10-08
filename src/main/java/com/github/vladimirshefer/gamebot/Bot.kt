package com.github.vladimirshefer.gamebot

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

import javax.security.auth.login.LoginException

import com.github.vladimirshefer.gamebot.TOKEN

lateinit var DISCORD: JDA

fun main(args: Array<String>) {
    DISCORD = JDABuilder(TOKEN)
            .addEventListeners(Bot())
            .setActivity(Activity.playing("Type !ping"))
            .build()
}

class Bot : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val msg = event.message

        if (msg.contentRaw == "% status") {
            val channel = event.channel
            val time = System.currentTimeMillis()
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                    .queue { response /* => Message */ -> response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue() }
        }
        if (msg.contentRaw.startsWith("%goto")) {
            val targetChannelName = msg.contentRaw.substring(5).trim()
            val channel = event.channel
            channel.sendMessage("Going to")
                    .queue { response ->
                        response.editMessageFormat(targetChannelName)
                                .queue()
                    }
        }
    }
}
