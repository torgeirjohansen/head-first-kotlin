class Song(private val artist: String, title: String ) {

    var titleInCaps: String = title
        set(value) {
            if (field.isNotEmpty()) field = value
        }
        get() {
            return field.uppercase()
        }

    fun play() {
        println("Playing the song $titleInCaps by $artist")
    }

    fun stop() {
        println("Stopped playing $titleInCaps")
    }
}