
fun main(args: Array<String>) {
    val fruit = arrayOf("Apple", "Banana", "Cherry", "Blueberry", "Pomegranate")
    val index = arrayOf(1, 3, 4,2)

    var x = 0;
    while (x < 4) {
        var y: Int
        y = index[x]
        println("Fruit = ${fruit[y]}")
        x = x +1
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
            Rectangle(1, 1))
    for (r in 0 .. rectangles.size  - 1) {
        rectangles[r].width = (r +1) * 3
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
    var nullable  = null
//    nullable = "not-possible"
    var stringNullable : String? = null

    println("Elvis operator (stringNullable?.length ?: -1) returned: ${stringNullable?.length ?: -1}")
     getSomeString()?.let {
        stringNullable = it
    }
    println("Elvis operator (stringNullable?.length ?: -1) returned: ${stringNullable?.length ?: -1}")
    // Use a safe cast (as?) to avoid getting a ClassCastException.
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
        optionsParam[(Math.random()*optionsParam.size).toInt()];

fun printResult(userChoice: String, gameChoice: String) {
    val result: String

    if (userChoice == gameChoice) result = "Tie!"

    else if (userChoice == "Rock" && gameChoice == "Scissors"
            || userChoice == "Paper" && gameChoice == "Rock"
            || userChoice =="Scissors" && gameChoice =="Paper") result = "you win!"
    else result = "You lose"

    println("You chose $userChoice, I chose $gameChoice. $result")
}
