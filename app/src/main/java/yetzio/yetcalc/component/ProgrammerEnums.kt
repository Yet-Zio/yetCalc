package yetzio.yetcalc.component

enum class NumberSystem(val radix: Int) {
    HEX(16), DEC(10), OCT(8), BIN(2)
}

enum class Operator(val str: String?){
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    NAND("NAND"),
    NOR("NOR"),
    XOR("XOR"),
    LSH("Lsh"),
    RSH("Rsh"),
    ADD("+"),
    SUB("-"),
    MUL("×"),
    DIV("/");
}