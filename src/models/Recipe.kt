package com.fourelements.models

data class Recipe(val id: Int, val title: String, val description: String)

data class NewRecipe(val id: Int?, val title: String, val description: String)
