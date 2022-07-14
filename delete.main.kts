#!/usr/bin/env kotlin
@file:DependsOn("dev.kord:kord-core:0.8.0-M15")

import dev.kord.core.Kord
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

if (args.isEmpty()) {
    println("Use ./delete.main.kts TOKEN")
    exitProcess(1)
}

val token = args[0]

runBlocking {
    var guildCommands = 0
    val kord = Kord(token) {
        stackTraceRecovery = true
    }
    val app = kord.getApplicationInfo()
    println("App: ${app.name} (${app.id})")
    val commands = kord.getGlobalApplicationCommands().toList()
    for (command in commands) {
        println("Deleting command ${command.id}")
        command.delete()
    }
    val guilds = kord.guilds.toList()
    guilds.forEach {
        for (command in kord.getGuildApplicationCommands(
            it.id
        ).toList()) {
            guildCommands++
            println("Deleting guild command ${command.id} on guild ${it.name} (${it.id})")
            command.delete()
        }
    }

    println("Deleted ${commands.size} global commands")
    println("Deleted $guildCommands guild commands on ${guilds.size} guilds")
}
