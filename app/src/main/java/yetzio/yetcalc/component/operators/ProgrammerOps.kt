package yetzio.yetcalc.component.operators

import com.ezylang.evalex.EvaluationException
import com.ezylang.evalex.Expression
import com.ezylang.evalex.data.EvaluationValue
import com.ezylang.evalex.operators.AbstractOperator
import com.ezylang.evalex.operators.InfixOperator
import com.ezylang.evalex.operators.OperatorIfc.*
import com.ezylang.evalex.operators.PrefixOperator
import com.ezylang.evalex.parser.Token

const val OPERATOR_PRECEDENCE_SHIFT = OPERATOR_PRECEDENCE_MULTIPLICATIVE

@InfixOperator(precedence = OPERATOR_PRECEDENCE_AND, operandsLazy = true)
class InfixAndOperator : AbstractOperator() {
    @Throws(EvaluationException::class)
    override fun evaluate(
        expression: Expression, operatorToken: Token?, vararg operands: EvaluationValue
    ): EvaluationValue {
        val leftValue = expression.evaluateSubtree(operands[0].expressionNode).stringValue
        val rightValue = expression.evaluateSubtree(operands[1].expressionNode).stringValue

        return expression.convertValue(leftValue.toLong(10) and rightValue.toLong(10))
    }
}

@InfixOperator(precedence = OPERATOR_PRECEDENCE_OR, operandsLazy = true)
class InfixOrOperator : AbstractOperator() {
    @Throws(EvaluationException::class)
    override fun evaluate(
        expression: Expression, operatorToken: Token?, vararg operands: EvaluationValue
    ): EvaluationValue {
        val leftValue = expression.evaluateSubtree(operands[0].expressionNode).stringValue
        val rightValue = expression.evaluateSubtree(operands[1].expressionNode).stringValue

        return expression.convertValue(leftValue.toLong(10) or rightValue.toLong(10))
    }
}

@PrefixOperator
class PrefixNotOperator : AbstractOperator() {
    override fun evaluate(
        expression: Expression, operatorToken: Token, vararg operands: EvaluationValue
    ): EvaluationValue {
        val value = operands[0].stringValue

        val result = value.toLong().inv()

        return expression.convertValue(result)
    }
}

@InfixOperator(precedence = OPERATOR_PRECEDENCE_AND, operandsLazy = true)
class InfixNandOperator : AbstractOperator() {
    @Throws(EvaluationException::class)
    override fun evaluate(
        expression: Expression, operatorToken: Token?, vararg operands: EvaluationValue
    ): EvaluationValue {
        val leftValue = expression.evaluateSubtree(operands[0].expressionNode).stringValue
        val rightValue = expression.evaluateSubtree(operands[1].expressionNode).stringValue

        val res = (leftValue.toLong(10) and rightValue.toLong(10)).inv()

        return expression.convertValue(res)
    }
}

@InfixOperator(precedence = OPERATOR_PRECEDENCE_OR, operandsLazy = true)
class InfixNorOperator : AbstractOperator() {
    @Throws(EvaluationException::class)
    override fun evaluate(
        expression: Expression, operatorToken: Token?, vararg operands: EvaluationValue
    ): EvaluationValue {
        val leftValue = expression.evaluateSubtree(operands[0].expressionNode).stringValue
        val rightValue = expression.evaluateSubtree(operands[1].expressionNode).stringValue

        val res = (leftValue.toLong(10) or rightValue.toLong(10)).inv()

        return expression.convertValue(res)
    }
}

@InfixOperator(precedence = OPERATOR_PRECEDENCE_OR, operandsLazy = true)
class InfixXorOperator : AbstractOperator() {
    @Throws(EvaluationException::class)
    override fun evaluate(
        expression: Expression, operatorToken: Token?, vararg operands: EvaluationValue
    ): EvaluationValue {
        val leftValue = expression.evaluateSubtree(operands[0].expressionNode).stringValue
        val rightValue = expression.evaluateSubtree(operands[1].expressionNode).stringValue

        val res = leftValue.toLong(10) xor rightValue.toLong(10)

        return expression.convertValue(res)
    }
}

@InfixOperator(precedence = OPERATOR_PRECEDENCE_OR, operandsLazy = true)
class InfixXNorOperator : AbstractOperator() {
    @Throws(EvaluationException::class)
    override fun evaluate(
        expression: Expression, operatorToken: Token?, vararg operands: EvaluationValue
    ): EvaluationValue {
        val leftValue = expression.evaluateSubtree(operands[0].expressionNode).stringValue
        val rightValue = expression.evaluateSubtree(operands[1].expressionNode).stringValue

        val res = (leftValue.toLong(10) xor rightValue.toLong(10)).inv()

        return expression.convertValue(res)
    }
}

@InfixOperator(precedence = OPERATOR_PRECEDENCE_SHIFT, operandsLazy = true)
class InfixLeftShiftOperator : AbstractOperator() {
    @Throws(EvaluationException::class)
    override fun evaluate(
        expression: Expression, operatorToken: Token?, vararg operands: EvaluationValue
    ): EvaluationValue {
        val leftValue = expression.evaluateSubtree(operands[0].expressionNode).stringValue
        val rightValue = expression.evaluateSubtree(operands[1].expressionNode).stringValue

        val res = leftValue.toLong(10) shl rightValue.toLong(10).toInt()

        return expression.convertValue(res)
    }
}

@InfixOperator(precedence = OPERATOR_PRECEDENCE_SHIFT, operandsLazy = true)
class InfixRightShiftOperator : AbstractOperator() {
    @Throws(EvaluationException::class)
    override fun evaluate(
        expression: Expression, operatorToken: Token?, vararg operands: EvaluationValue
    ): EvaluationValue {
        val leftValue = expression.evaluateSubtree(operands[0].expressionNode).stringValue
        val rightValue = expression.evaluateSubtree(operands[1].expressionNode).stringValue

        val res = leftValue.toLong(10) shr rightValue.toLong(10).toInt()

        return expression.convertValue(res)
    }
}

@InfixOperator(precedence = OPERATOR_PRECEDENCE_SHIFT, operandsLazy = true)
class InfixUnsignedRightShiftOperator : AbstractOperator() {
    @Throws(EvaluationException::class)
    override fun evaluate(
        expression: Expression, operatorToken: Token?, vararg operands: EvaluationValue
    ): EvaluationValue {
        val leftValue = expression.evaluateSubtree(operands[0].expressionNode).stringValue
        val rightValue = expression.evaluateSubtree(operands[1].expressionNode).stringValue

        val res = leftValue.toLong(10) ushr rightValue.toLong(10).toInt()

        return expression.convertValue(res)
    }
}

@InfixOperator(precedence = OPERATOR_PRECEDENCE_SHIFT, operandsLazy = true)
class InfixRotateLeftOperator : AbstractOperator() {
    @Throws(EvaluationException::class)
    override fun evaluate(
        expression: Expression, operatorToken: Token?, vararg operands: EvaluationValue
    ): EvaluationValue {
        val leftValue = expression.evaluateSubtree(operands[0].expressionNode).stringValue.toLong(10)
        val rightValue = expression.evaluateSubtree(operands[1].expressionNode).stringValue.toInt(10)

        return expression.convertValue((leftValue shl rightValue) or (leftValue shr (64 - rightValue)))
    }
}

@InfixOperator(precedence = OPERATOR_PRECEDENCE_SHIFT, operandsLazy = true) // Same as shifts
class InfixRotateRightOperator : AbstractOperator() {
    @Throws(EvaluationException::class)
    override fun evaluate(
        expression: Expression, operatorToken: Token?, vararg operands: EvaluationValue
    ): EvaluationValue {
        val leftValue = expression.evaluateSubtree(operands[0].expressionNode).stringValue.toLong(10)
        val rightValue = expression.evaluateSubtree(operands[1].expressionNode).stringValue.toInt(10)

        return expression.convertValue((leftValue shr rightValue) or (leftValue shl (64 - rightValue)))
    }
}