// Only data defined in the constructor will partake in toString(), equals() and hashcode()

data class Recipe(
    val title: String,
    val mainIngredient: String,
    val isVegetarian: Boolean = false,
    val difficulty: String = "Easy")