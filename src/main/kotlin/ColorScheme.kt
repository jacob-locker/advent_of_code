import androidx.compose.ui.graphics.Color

enum class ColorScheme(val displayName: String, val colors: List<Color>) {
    WHITE_BLUE_GREY_BLK("White/Blue/Grey/Blk",
        listOf(
            Color(0xFF32292F),
            Color(0xFF575366),
            Color(0xFF6E7DAB),
            Color(0xFF5762D5),
            Color(0xFFD1E3DD)
        ).reversed()
    ),
    PINK_PURPLE_BLACK("Pink/Purple/Blk",
        listOf(
            Color(0xFF140617),
            Color(0xFF4F1759),
            Color(0xFF721782),
            Color(0xFF9B14B3),
            Color(0xFFE288F2)
        ).reversed()
    ),
    TAN_GREY_BLACK("Tan/Grey/Blk",
        listOf(
            Color(0xFF32213A),
            Color(0xFF383B53),
            Color(0xFF66717E),
            Color(0xFFD4D6B9),
            Color(0xFFD1CAA1)
        ).reversed()
    ),
    PURPLE_BLACK("Purple/Blk",
        listOf(
            Color(0xFF1A0A1C),
            Color(0xFF200624),
            Color(0xFF851594),
            Color(0xFF841D91),
            Color(0xFFC429D9)
        ).reversed()
    );

    companion object {
        val DEFAULT = WHITE_BLUE_GREY_BLK
    }

    override fun toString(): String {
        return displayName
    }
}