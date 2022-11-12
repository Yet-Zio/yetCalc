package yetzio.yetcalc.component

enum class CalcMode(val str: String?){
    FIRSTMODE("1ST"),
    SECONDMODE("2ND"),
    THIRDMODE("3RD"),
    FOURTHMODE("4TH"),
    FIFTHMODE("5TH"),
    SIXTHMODE("6TH"),
    SEVENTHMODE("7TH"),
    EIGHTMODE("8TH"),
    NINTHMODE("9TH");

    companion object{
        fun getMode(modestr: String?): CalcMode{
            when(modestr){
                FIRSTMODE.str -> {
                    return FIRSTMODE
                }
                SECONDMODE.str -> {
                    return SECONDMODE
                }
                THIRDMODE.str -> {
                    return THIRDMODE
                }
                FOURTHMODE.str -> {
                    return FOURTHMODE
                }
                FIFTHMODE.str -> {
                    return FIFTHMODE
                }
                SIXTHMODE.str -> {
                    return SIXTHMODE
                }
                SEVENTHMODE.str -> {
                    return SEVENTHMODE
                }
                EIGHTMODE.str -> {
                    return EIGHTMODE
                }
                NINTHMODE.str -> {
                    return NINTHMODE
                }
                else -> {
                    return FIRSTMODE
                }
            }
        }
    }
}

enum class AngleMode(val str: String?){
    DEGREE("DEG"),
    RADIAN("RAD");

    companion object{
        fun getMode(modestr: String?): AngleMode{
            when(modestr){
                DEGREE.str -> {
                    return DEGREE
                }
                RADIAN.str -> {
                    return RADIAN
                }
                else -> {
                    return RADIAN
                }
            }
        }
    }
}