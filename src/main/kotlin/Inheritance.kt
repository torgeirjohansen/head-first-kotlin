class Inheritance {

}

abstract class Animal : Roamable {
    abstract val image: String
    abstract val food: String
    abstract val habitat: String
    var hunger = 10

    abstract fun makeNoise()

    abstract fun eat()

    override fun roam() {
        println("animal roaming")
    }

    fun sleep() {
        println("animal sleeping")
    }

}
abstract class Canine : Animal() {

    override fun roam() {
        println("Canine roaming")
        
    }
}

class Wolf : Canine() {
    override val image = "wolf image"
    override val food = "Wolf food"
    override val habitat = "Wolf habitat"

    override fun makeNoise() {
        println("howwwwwwl")
    }

    override fun eat() {
        println("The wolf is eating $food")
    }
}

interface Roamable {
    fun roam()
}

class Vehicle : Roamable {
    override fun roam() {
        println("The vehicle is roaming")
    }

}