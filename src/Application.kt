package com.fourelements

import com.fourelements.database.DatabaseManager
import com.fourelements.database.RecipeDao
import com.fourelements.database.Recipes
import com.fourelements.routes.registerCustomerRoutes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.network.tls.certificates.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CallLogging)
    install(DefaultHeaders)

    install(ContentNegotiation) {
        jackson()
    }

    val db = DatabaseManager.create()
    Database.connect(db)
    transaction {
        SchemaUtils.create(Recipes)
    }

    registerCustomerRoutes(RecipeDao())
}

