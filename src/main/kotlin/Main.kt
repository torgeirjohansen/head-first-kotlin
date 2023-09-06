import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val TOM_FILE = "src/main/kotlin/audio/toms.aiff"
const val CYMBAL_FILE = "src/main/kotlin/audio/crash_cymbal.aiff"
const val FLOOR_TOM_FILE = "src/main/kotlin/audio/floor_toms.aiff"
const val HIGH_HAT = "src/main/kotlin/audio/high_hat.aiff"
const val KICK_DRUM = "src/main/kotlin/audio/kick_drum.aiff"
const val SNARE = "src/main/kotlin/audio/snare.aiff"

fun main(args: Array<String>) {
    if (false) {
        val fruit = arrayOf("Apple", "Banana", "Cherry", "Blueberry", "Pomegranate")
        val index = arrayOf(1, 3, 4, 2)

        var x = 0;
        while (x < 4) {
            var y: Int
            y = index[x]
            println("Fruit = ${fruit[y]}")
            x = x + 1
        }

        // Chapter 3
//    val options = arrayOf("Rock", "Scissors", "Paper")
//    val gameChoice = getGameChoice(options)
//    val userChoice = getUserChoice(options)
//    printResult(userChoice, gameChoice)

        // Chapter 4
        val songOne = Song("Steve Harley", "Going Underground")
        val songTwo = Song("Metallica", "One")
        println("Title in caps is ${songOne.titleInCaps}")
        songTwo.play()
        songTwo.stop()
        songOne.play()

        val d = DrumKit(true, true)
        d.playTopHat()
        d.playSnare()
        d.hasSnare = false;
        d.playTopHat()
        d.playSnare()

        val myDog = Dog("Fido", 70, "Mixed")
        myDog.bark()
        myDog.weight = 77
        println("Weight in Kgs is ${myDog.weightInKgs}")
        myDog.weight = -2
        println("Weight is ${myDog.weight}")
        for (item in myDog.activities) {
            println("My dog enjoys $item")
        }

        val rectangles = arrayOf(
            Rectangle(1, 1),
            Rectangle(1, 1),
            Rectangle(1, 1),
            Rectangle(1, 1)
        )
        for (r in 0..rectangles.size - 1) {
            rectangles[r].width = (r + 1) * 3
            rectangles[r].height = r + 5
            print("Rectangle $r has area ${rectangles[r].area}")
            println("It is ${if (rectangles[r].isSquare) "" else "not "} a square")
        }

        // Chapter 6
        println("Chapter 6:")
        val r1 = Recipe("Chicken Bhuna", "Chicken")
        val r2 = Recipe("Chicken Bhuna", "Chicken")
        val r3 = r1.copy(title = "Copied Bhuna")
        println("r1 hashcode: ${r1.hashCode()}")
        println("r2 hashcode: ${r2.hashCode()}")
        println("r3 hashcode: ${r3.hashCode()}")
        println("r1.toString(): ${r1.toString()}")
        println("r1 == r2: ${r1 == r2}")
        println("r1 === r2: ${r1 === r2}")
        val (title, isVeg) = r1
        println("title = $title, and isVeg = $isVeg")

        //  Chapter 8 nullable types
        var nullable = null
//    nullable = "not-possible"
        var stringNullable: String? = null

        println("Elvis operator (stringNullable?.length ?: -1) returned: ${stringNullable?.length ?: -1}")
        getSomeString()?.let {
            stringNullable = it
        }
        println("Elvis operator (stringNullable?.length ?: -1) returned: ${stringNullable?.length ?: -1}")
        // Use a safe cast (as?) to avoid getting a ClassCastException.

        // Chapter 9
        var a: MutableList<String> = mutableListOf()
        a.add("Zero")
        a.add("Two")
        a.add(2, "Four")
        a.add(3, "Six")
        println(a)


        if (a.contains("Zero")) a.add("Eight")
        a.removeAt(0)
        println(a)

        if (a.indexOf("Four") != 4) a.add("Ten")
        println(a)

        if (a.contains("Zero")) a.add("Twelve")
        println(a)

        // Sets: not allowed to add duplicates:
        // As you learned in Chapter 7, the === operator checks whether two references
        // point to the same object, and the == operator checks whether the references
        // point to objects that should be considered equal
        // A Set, however, only uses these operators once it’s established that the two objects have matching hash code values.
        // This means that in order for a Set to work properly, equal objects must have matching hash codes

        val pets1 = listOf("cat", "dog", "fish", "fish")
        val pets2 = listOf("cat", "owl")
        val pets3 = listOf("dog", "dove", "dog", "dove")
        val pets4 = listOf("hedgehog")

        val setOfPets = pets1.toMutableSet()
        setOfPets.addAll(pets2)
        setOfPets.addAll(pets3)
        setOfPets.addAll(pets4)
        println("Number of pets are: " + setOfPets.size)

        val pets = pets1.toMutableList()
        pets.addAll(pets2)
        pets.addAll(pets3)
        pets.addAll(pets4)
        println("all pets. " + pets)

        println("total number of pets: " + pets.size)

        println("all pets sorted. " + pets.sort())

        // Chapter 10 generics
        val catFuzz = Cat("Cat Fuzz")
        val catKatsu = Cat("Katsu")
        val fishFinny = Fish("Finny")
        val catOwner = PetOwner(catFuzz)
        catOwner.add(catKatsu)

        //    When we prefix a generic type with out, we say that the generic type is covariant.
        //    In other words, it means that a subtype can be used in place of a supertype.

        // If a generic type is contravariant, it means that you can use a supertype in place of a subtype.
        // This is the opposite of covariance.
        // When we prefix a generic type with in, we say that the generic type is contravariant.
        // In other words, it means that a supertype can be used in place of a subtype

        // Chapter 11 Lambdas
        // If the final parameter of a function you want to call is a lambda,
        // as is the case with our convert function, you can move the lambda argument outside the function call’s parentheses.
        Lambdas().convert(20.0) { c -> c * 1.8 + 32 }

        val options = arrayOf("Red", "Amber", "Green")
        var crossWalk = options[(Math.random() * options.size).toInt()]
        println("crossWalk = $crossWalk")
        if (crossWalk == "Green") {
            println("walk!")
        }
        Lambdas.unless(crossWalk == "Green") {
            println("Stop")
        }

        val groceries = listOf(
            Grocery("Tomatoes", "Vegetable", "lb", 3.0, 3),
            Grocery("Mushrooms", "Vegetable", "lb", 4.0, 1),
            Grocery("Bagels", "Bakery", "Pack", 1.5, 2),
            Grocery("Olive oil", "Pantry", "Bottle", 6.0, 1),
            Grocery("Ice cream", "Frozen", "Pack", 3.0, 2),
        )
        println("Expensive ingredients:")
        Lambdas.search(groceries) { i: Grocery -> i.unitPrice > 5.0 }
        println("All vegetables:")
        Lambdas.search(groceries) { i: Grocery -> i.category == "Vegetable" }
        println("All packs:")
        Lambdas.search(groceries) { i: Grocery -> i.unit == "Pack" }

        // Chapter 12: lambdas and higher order
        // Instead of implementing Comparable in your own classes, however,
        // we think that using the minBy and maxBy functions is a better approach as they give you more flexibility.
        val vegetables = groceries.filter { v -> v.category == "Vegetable" }
        val groceryNames = groceries.map { g -> g.name }

        //If you want to use the first item in the collection as the initial value,
        // however, an alternative approach is to use the reduce function.
        // This function works in a similar way to fold, except that you don’t have to specify the initial value.
        // It automatically uses the first item in the collection as the initial value.
        val contest = Contest<Cat>()
        contest.addScore(catFuzz, 1)
        contest.addScore(catKatsu, 42)
        contest.getWinners()

        var sum = 0.0
        groceries.filter { it.category == "Vegetable" }.forEach { g -> sum += g.quantity * g.unitPrice }
        println("Sum: ${sum}")

        groceries.filter { it.unitPrice * it.quantity > 5.0 }.forEach { println(it.name) }

        groceries.groupBy { it.category }.forEach {
            println(it.key + " total cost")
            println(it.value.sumByDouble { it.unitPrice * it.quantity })
        }

        groceries.filterNot { it.unit == "Bottle" }.groupBy { it.unit }.forEach {
            println("   ${it.key}")
            it.value.forEach { println("        ${it.name}") }
        }

        // co routines

        // run in sequence
        val beats = Beats()

        runBlocking {
            launch { beats.playBeatsWithSuspend("x-x-x-x-x-x-", TOM_FILE) }
            beats.playBeatsWithSuspend("x-----x----", CYMBAL_FILE)
        }
    }
    

}

fun getSomeString(): String? {
    return "someString";
}

fun getUserChoice(options: Array<String>): String {
    var isValid = false
    var userChoice = ""
    while (!isValid) {
        print("Please enter one of the following")
        for (item in options) print(" $item")
        println(".")
        val userInput = readLine()
        if (userInput != null && userInput in options) {
            isValid = true
            userChoice = userInput
        }
        if (!isValid) println("You must enter a valid choice")
    }
    return userChoice
}

fun getGameChoice(optionsParam: Array<String>) =
    optionsParam[(Math.random() * optionsParam.size).toInt()];

fun printResult(userChoice: String, gameChoice: String) {
    val result: String

    if (userChoice == gameChoice) result = "Tie!"
    else if (userChoice == "Rock" && gameChoice == "Scissors"
        || userChoice == "Paper" && gameChoice == "Rock"
        || userChoice == "Scissors" && gameChoice == "Paper"
    ) result = "you win!"
    else result = "You lose"

    println("You chose $userChoice, I chose $gameChoice. $result")
}
