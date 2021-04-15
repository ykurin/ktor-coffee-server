package com.fourelements.database

import com.fourelements.database.DatabaseManager.dbQuery
import com.fourelements.models.NewRecipe
import com.fourelements.models.Recipe
import org.jetbrains.exposed.sql.*

class RecipeDao {
    suspend fun getAll(): List<Recipe> = dbQuery {
        Recipes.selectAll().map { toModel(it) }
    }

    suspend fun get(id: Int): Recipe? = dbQuery {
        Recipes.select { Recipes.id eq id }.mapNotNull { toModel(it) }.singleOrNull()
    }

    suspend fun add(recipe: NewRecipe): Recipe {
        var id = 0
        dbQuery {
            id = (Recipes.insert {
                it[title] = recipe.title
                it[description] = recipe.description
            } get Recipes.id).value
        }
        return get(id)!!
    }

    suspend fun update(recipe: NewRecipe): Recipe? {
        return if (recipe.id == null) {
            add(recipe)
        } else {
            dbQuery {
                Recipes.update({ Recipes.id eq recipe.id }) {
                    it[title] = recipe.title
                    it[description] = recipe.description
                }
            }
            get(recipe.id)
        }
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        Recipes.deleteWhere { Recipes.id eq id } > 0
    }

    private fun toModel(row: ResultRow) =
        Recipe(
            id = row[Recipes.id].value,
            title = row[Recipes.title],
            description = row[Recipes.description],
        )
}