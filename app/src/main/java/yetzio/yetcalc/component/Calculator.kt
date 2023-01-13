package yetzio.yetcalc.component

import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function
import org.mariuszgromada.math.mxparser.mXparser

class Calculator{
    var angleMode = AngleMode.DEGREE
    var almostInt = true
    var canonInt =  false
    var precision = "Default precision"
    val m_history = History()
    val MAXREP = 9999999

    fun calculate(expr: String): String {
        mXparser.setUlpRounding(false)

        val ncr = Function("nCr(n, r) = nCk(n, r)")
        val npr = Function("nPr(n, r) = nPk(n, r)")

        when (precision) {
            "Default precision" -> mXparser.setDefaultEpsilon()
            "1e-60" -> mXparser.setEpsilon(1e-60)
            "1e-99" -> mXparser.setEpsilon(1e-99)
            "1e-323" -> mXparser.setEpsilon(1e-323)
        }

        if(almostInt)
            mXparser.setAlmostIntRounding(true)
        else
            mXparser.setAlmostIntRounding(false)

        if(canonInt)
            mXparser.setCanonicalRounding(true)
        else
            mXparser.setCanonicalRounding(false)

        if (angleMode == AngleMode.DEGREE) {
            mXparser.setDegreesMode()
        } else if (angleMode == AngleMode.RADIAN) {
            mXparser.setRadiansMode()
        }

        val grad = if (mXparser.checkIfDegreesMode())
            Function("grad(x) = x * (200/180)")
        else
            Function("grad(x) = x * (200/pi)")

        val e = Expression(expr, grad, npr, ncr)

        return if(e.calculate() > MAXREP){
            e.calculate().toString()
        }
        else if(e.calculate() % 1.0 == 0.0){
            Math.round(e.calculate()).toString()
        }
        else{
            e.calculate().toString()
        }
    }

    fun addToHistory(ex: String, res: String){
        m_history.addToDb(ex, res)
    }
}