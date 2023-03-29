class Contest<T: Pet> {
    var scores: MutableMap<T, Int> = mutableMapOf()

    fun addScore(t: T, score: Int = 0) {
        if (score >= 0) scores.put(t, score)
    }

    fun getWinners(): Set<T> {
        val highScore = scores.maxBy { s -> s.value }.value
        val winners = scores.filter { it.value == highScore}.keys
        winners.forEach{println("Winner: ${it.name}")}
        return winners
    }
}