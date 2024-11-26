package yetzio.yetcalc.component

import com.ezylang.evalex.Expression
import com.ezylang.evalex.config.ExpressionConfiguration
import com.ezylang.evalex.operators.OperatorIfc
import yetzio.yetcalc.component.operators.InfixAndOperator
import yetzio.yetcalc.component.operators.InfixLeftShiftOperator
import yetzio.yetcalc.component.operators.InfixNandOperator
import yetzio.yetcalc.component.operators.InfixNorOperator
import yetzio.yetcalc.component.operators.InfixOrOperator
import yetzio.yetcalc.component.operators.InfixRightShiftOperator
import yetzio.yetcalc.component.operators.InfixRotateLeftOperator
import yetzio.yetcalc.component.operators.InfixRotateRightOperator
import yetzio.yetcalc.component.operators.InfixUnsignedRightShiftOperator
import yetzio.yetcalc.component.operators.InfixXNorOperator
import yetzio.yetcalc.component.operators.InfixXorOperator
import yetzio.yetcalc.component.operators.PrefixNotOperator
import yetzio.yetcalc.enums.NumberSystem
import java.util.Map
import kotlin.math.roundToLong

class ProgrammerCalculator {

    val configuration: ExpressionConfiguration = ExpressionConfiguration.defaultConfiguration()
        .withAdditionalOperators(
            Map.entry<String, OperatorIfc>("AND", InfixAndOperator()),
            Map.entry<String, OperatorIfc>("OR", InfixOrOperator()),
            Map.entry<String, OperatorIfc>("NOT", PrefixNotOperator()),
            Map.entry<String, OperatorIfc>("NAND", InfixNandOperator()),
            Map.entry<String, OperatorIfc>("NOR", InfixNorOperator()),
            Map.entry<String, OperatorIfc>("XOR", InfixXorOperator()),
            Map.entry<String, OperatorIfc>("XNOR", InfixXNorOperator()),
            Map.entry<String, OperatorIfc>("LSH", InfixLeftShiftOperator()),
            Map.entry<String, OperatorIfc>("RSH", InfixRightShiftOperator()),
            Map.entry<String, OperatorIfc>("URSH", InfixUnsignedRightShiftOperator()),
            Map.entry<String, OperatorIfc>("RoL", InfixRotateLeftOperator()),
            Map.entry<String, OperatorIfc>("RoR", InfixRotateRightOperator()),
        )

    fun calculate(expr: String, numSys: NumberSystem): String{
        try{
            var currentexpr = expr
            if (numSys == NumberSystem.HEX) {
                currentexpr = currentexpr.replace(Regex("\\b([A-Fa-f0-9]+)\\b")) {
                    it.value.toLong(16).toString()
                }
            } else if (numSys == NumberSystem.OCT) {
                currentexpr = currentexpr.replace(Regex("\\b([0-7]+)\\b")) {
                    it.value.toLong(8).toString()
                }
            } else if (numSys == NumberSystem.BIN) {
                currentexpr = currentexpr.replace(Regex("\\b([0-1]+)\\b")) {
                    it.value.toLong(2).toString()
                }
            }

            val expression = Expression(currentexpr, configuration)
            val res = expression.evaluate().stringValue.toDouble().roundToLong()

            return res.toString(numSys.radix).uppercase()
        }
        catch(e: Exception){
            println("PGCalc exception")
            e.printStackTrace()
            return "NaN"
        }
    }
}