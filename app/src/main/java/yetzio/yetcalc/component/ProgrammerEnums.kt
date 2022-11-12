package yetzio.yetcalc.component

enum class NumberSystem(val radix: Int) {
    HEX(16), DEC(10), OCT(8), BIN(2)
}

enum class Operator {
    AND, OR, NOT, NAND, NOR, XOR, LSH, RSH, ADD, SUB, MUL, DIV
}