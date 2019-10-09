package com.github.vladimirshefer.gamebot

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.GenericMessageEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class UserCommandHandler : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val member = event.message.guild.getMember(event.message.author)!!
        val messageLine = event.message.contentRaw
        val commands = messageLine.split("\\w+")
        val guildName = commands[0]
        val guild = event.jda.getGuildsByName(guildName, false)
                .apply {
                    if (size > 1) {
                        event.log("Существует несколько гильдий (серверов) с именем '$guildName'.")
                    }
                }

        if (messageLine == "ping") {
            val time = System.currentTimeMillis()
            event.channel.sendMessage("pong!").queue()
        }

        if (messageLine.startsWith("%goto")) {
            val targetChannelName = messageLine.substring(5).trim()
            val targetChannel = event.message.guild.getTextChannelsByName(targetChannelName, true)
                    .firstOrNull()

            if (targetChannel == null) {
                event.log("@${member.effectiveName}, канал '$targetChannelName' не найден")
                return
            }

            val topicTags = targetChannel.topic.parseTopicTags()
            
            if (!topicTags.contains("traversable")) {
                event.log("Канал закрыт для перемещения")
                return
            }

            targetChannel.putPermissionOverride(member)
                    .setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ATTACH_FILES)
                    .queue()

            event.log("@${member.effectiveName} исчез в направлении $targetChannelName - $guildName")
        }
    }
}

fun GenericMessageEvent.log(message: String) {
    println(message)
    return channel.sendMessage(message).queue()
}

fun String?.parseTopicTags(): List<String> =
        if (this == null) {
            emptyList()
        } else {
            this
                    .split('\n')
                    .filter { it[0] != '#' }
                    .flatMap {
                        it.split(Regex.fromLiteral("[^a-zA-Z_]+"))
                    }
        }

//event.channel.sendMessage("Going to")
//.queue { response ->
//    response.editMessageFormat("@${member.effectiveName} исчез в направлении $targetChannelName")
//            .queue()
//}