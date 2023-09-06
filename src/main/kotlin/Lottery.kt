class Lottery {

    fun pickWinner() {
        val playersWithNumbers = mapOf(
            "Torgeir" to arrayOf(4, 8, 13, 14, 15),
            "Sigurd" to arrayOf(1, 2, 3, 5, 6),
            "Arild" to arrayOf(36, 37, 38, 39, 40),
            "Johan" to arrayOf(21, 27),
            "Kristine" to arrayOf(19, 22, 30),
            "Svein-Erik" to arrayOf(7, 9, 10, 11),
            "Jørn" to arrayOf(23, 24, 25, 25)
        );

        val allNumbers = playersWithNumbers.flatMap { it.value.asList() }
        println("Alle solgte lodd: " + allNumbers);

        print("Velger vinnerlodd ")
        repeat(10) {
            print(".")
            Thread.sleep(250);
        }
        println(".");

        val winnerNumber = allNumbers.random();
        println("Vinner lodd er: $winnerNumber");

        val winner = playersWithNumbers.filter { it.value.contains(winnerNumber) }.keys.first()

        print("Vinneren er ....");
        repeat(5) {
            print(".")
            Thread.sleep(250);
        }
        println("$winner !!");
    }
}