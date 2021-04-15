package com.fourelements.routes

import com.fourelements.database.RecipeDao
import com.fourelements.database.Recipes
import com.fourelements.models.NewRecipe
import com.fourelements.models.Recipe
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.recipeRouting(recipeDao: RecipeDao) {
    route("/recipe") {
        get {
            call.respond(recipeDao.getAll())
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
                "Missing or malformed id", status = HttpStatusCode.BadRequest
            )
            val item = recipeDao.get(id) ?: return@get call.respondText(
                "Missing or malformed id", status = HttpStatusCode.NotFound
            )
            call.respond(item)
        }

        post {
            val recipe = call.receive<NewRecipe>()
            call.respond(HttpStatusCode.Created, recipeDao.add(recipe))
        }

        put {
            val recipe = call.receive<NewRecipe>()
            val result = recipeDao.update(recipe)
            if (result == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, result)
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText(
                "Missing or malformed id", status = HttpStatusCode.BadRequest
            )
            if (recipeDao.delete(id)) {
                call.respondText("Recipe removed correctly", status = HttpStatusCode.OK)
            } else {
                call.respondText("Recipe not found", status = HttpStatusCode.NotFound)
            }
        }

    }
}

fun Application.registerCustomerRoutes(recipeDao: RecipeDao) {
    routing {
        recipeRouting(recipeDao)
    }
}