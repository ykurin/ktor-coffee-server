package com.fourelements.database

import org.jetbrains.exposed.dao.id.IntIdTable

object Recipes : IntIdTable() {
    val title = varchar("title", 100)
    val description = varchar("description", 500)
}