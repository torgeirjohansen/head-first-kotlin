class Lambdas() {
    fun convert(x: Double, converter: (Double)  -> Double ) : Double {
        val result = converter(x)
        println("$x is converted to $result")
        return result
    }

    companion object {
        fun unless(b: Boolean, function: () -> Unit) {
            if (!b) {
                function.invoke()
            }
        }

        fun search(groceries: List<Grocery>, function: (Grocery) -> Boolean) {
            for (l in groceries) {
                if (function(l)) {
                    println(l.name)
                }
            }
        }
    }
}