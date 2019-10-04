package com.hithub.vladimirshefer.gamebot

import com.github.seratch.jslack.Slack
import java.time.ZonedDateTime

class ChatTest {

    private val botToken = System.getenv("SLACK_BOT_USER_TEST_OAUTH_ACCESS_TOKEN")
    private val slack = Slack.getInstance()
    private val channelName = "#random"

    fun chatPostMessage() {
        val response = slack.methods(botToken).chatPostMessage {
            it.channel(channelName)
                    .text("Searchable Log of All Conversation and Knowledge https://twitter.com/stewart/status/780906639301812225")
                    .unfurlMedia(true)
        }
    }

    fun chatPostEphemeral() {
        val channelId = slack.methods(botToken).channelsList { it.limit(100) }
                .channels
                .filter { it.name == "random" }
                .map { it.id }
                .first()

        val memberIds = slack.methods().channelsInfo { it.token(botToken).channel(channelId) }.channel.members

        val user = slack.methods(botToken).usersList { it.limit(100) }
                .members
                .filter { !it.isBot && !it.isDeleted && memberIds.contains(it.id) }
                .first()

        val response = slack.methods(botToken).chatPostEphemeral {
            it.channel(channelName)
                    .user(user.id)
                    .text("Hey ${user.name}, this message is only visible to you")
        }
    }

    fun chatScheduleMessage() {
        val now = ZonedDateTime.now()
        val response = slack.methods(botToken).chatScheduleMessage {
            it.text("This is a scheduled message at ${now}")
                    .channel(channelName)
                    .postAt(now.toEpochSecond().toInt() + 10) // will be posted in 10 seconds
        }
    }

    fun thread() {
        val postResponse = slack.methods(botToken).chatPostMessage {
            it.channel(channelName).text("This is a thread")
        }

        val response = slack.methods(botToken).chatPostMessage {
            it.channel(channelName)
                    .threadTs(postResponse.message.ts)
                    .text("This is a reply in the thread")
                    .replyBroadcast(true)
        }
    }

    fun chatDelete() {
        val postResponse = slack.methods(botToken).chatPostMessage {
            it.channel(channelName).text("Message to be deleted soon...")
        }

        val response = slack.methods(botToken).chatDelete {
            it.channel(postResponse.channel).ts(postResponse.message.ts)
        }
    }
}