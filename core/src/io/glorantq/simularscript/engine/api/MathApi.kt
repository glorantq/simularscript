package io.glorantq.simularscript.engine.api

import org.luaj.vm2.LuaDouble
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.LibFunction
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction
import java.util.*
import kotlin.collections.ArrayList

/**
 * Math API for Lua scripts
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 */

class MathApi : TwoArgFunction() {
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library: LuaTable = LuaValue.tableOf()
        library.set("vec2", Vec2())
        library.set("abs", Abs())
        library.set("ceil", Ceil())
        library.set("cos", Cos())
        library.set("deg", Deg())
        library.set("exp", Exp())
        library.set("floor", Floor())
        library.set("fmod", Fmod())
        library.set("frexp", Frexp())
        library.set("huge", LuaDouble.POSINF)
        library.set("ldexp", Ldexp())
        library.set("max", Max())
        library.set("min", Min())
        library.set("modf", Modf())
        library.set("pi", Math.PI)
        library.set("pow", Pow())
        val r = LuaRandom()
        library.set("random", r)
        library.set("randomseed", Randomseed(r))
        library.set("rad", Rad())
        library.set("sin", Sin())
        library.set("sqrt", Sqrt())
        library.set("tan", Tan())
        library.set("choose", Choose(r))
        library.set("avg", Avg())
        library.set("sum", Sum())
        env.set("Math", library)
        return library
    }

    /**
     * Class to calculate the sum of a sequence
     *
     * @author Gerber Lóránt Viktor
     * @since 3.0-beta2
     */
    private class Sum : VarArgFunction() {
        override fun invoke(args: Varargs): Varargs {
            if(args.narg() < 1) {
                argerror("numbers")
                return LuaValue.NONE
            }

            val kotlinArgs: ArrayList<Double> = arrayListOf()
            (1..args.narg()).mapTo(kotlinArgs) { args.checkdouble(it) }
            return LuaValue.valueOf(kotlinArgs.asSequence().sum())
        }
    }

    /**
     * Class to calculate the average of a sequence
     *
     * @author Gerber Lóránt Viktor
     * @since 3.0-beta2
     */
    private class Avg : VarArgFunction() {
        override fun invoke(args: Varargs): Varargs {
            if(args.narg() < 1) {
                argerror("numbers")
                return LuaValue.NONE
            }

            val kotlinArgs: ArrayList<Double> = arrayListOf()
            (1..args.narg()).mapTo(kotlinArgs) { args.checkdouble(it) }
            return LuaValue.valueOf(kotlinArgs.asSequence().average())
        }
    }

    /**
     * Class used to make vectors
     *
     * @author Gerber Lóránt Viktor
     * @since 3.0-beta1
     * @see LuaVector2
     */
    private class Vec2 : TwoArgFunction() {
        override fun call(arg1: LuaValue, arg2: LuaValue): LuaValue = LuaVector2(arg1.checkdouble(), arg2.checkdouble())
    }

    private abstract class UnaryOp : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue = LuaValue.valueOf(call(arg.checkdouble()))

        protected abstract fun call(d: Double): Double
    }

    private abstract class BinaryOp : TwoArgFunction() {
        override fun call(x: LuaValue, y: LuaValue): LuaValue =
                LuaValue.valueOf(call(x.checkdouble(), y.checkdouble()))

        protected abstract fun call(x: Double, y: Double): Double
    }

    private class Abs : UnaryOp() {
        override fun call(d: Double): Double = Math.abs(d)
    }

    private class Ceil : UnaryOp() {
        override fun call(d: Double): Double = Math.ceil(d)
    }

    private class Cos : UnaryOp() {
        override fun call(d: Double): Double = Math.cos(d)
    }

    private class Deg : UnaryOp() {
        override fun call(d: Double): Double = Math.toDegrees(d)
    }

    private class Floor : UnaryOp() {
        override fun call(d: Double): Double = Math.floor(d)
    }

    private class Rad : UnaryOp() {
        override fun call(d: Double): Double = Math.toRadians(d)
    }

    private class Sin : UnaryOp() {
        override fun call(d: Double): Double = Math.sin(d)
    }

    private class Sqrt : UnaryOp() {
        override fun call(d: Double): Double = Math.sqrt(d)
    }

    private class Tan : UnaryOp() {
        override fun call(d: Double): Double = Math.tan(d)
    }

    private class Exp : UnaryOp() {
        override fun call(d: Double): Double = Math.exp(d)
    }

    private class Fmod : BinaryOp() {
        override fun call(x: Double, y: Double): Double {
            val q = x / y
            return x - y * if (q >= 0) Math.floor(q) else Math.ceil(q)
        }
    }

    private class Ldexp : BinaryOp() {
        override fun call(x: Double, y: Double): Double = x * java.lang.Double.longBitsToDouble(y.toLong() + 1023 shl 52)
    }

    private class Pow : BinaryOp() {
        override fun call(x: Double, y: Double): Double = Math.pow(x, y)
    }

    private class Frexp : VarArgFunction() {
        override fun invoke(args: Varargs): Varargs {
            val x = args.checkdouble(1)
            if (x == 0.0) return LuaValue.varargsOf(LuaValue.ZERO, LuaValue.ZERO)
            val bits = java.lang.Double.doubleToLongBits(x)
            val m = ((bits and (-1L shl 52).inv()) + (1L shl 52)) * if (bits >= 0) .5 / (1L shl 52) else -.5 / (1L shl 52)
            val e = (((bits shr 52).toInt() and 0x7ff) - 1022).toDouble()
            return LuaValue.varargsOf(LuaValue.valueOf(m), LuaValue.valueOf(e))
        }
    }

    private class Max : VarArgFunction() {
        override fun invoke(args: Varargs): Varargs {
            var m = args.checkdouble(1)
            var i = 2
            val n = args.narg()
            while (i <= n) {
                m = Math.max(m, args.checkdouble(i))
                ++i
            }
            return LuaValue.valueOf(m)
        }
    }

    private class Min : VarArgFunction() {
        override fun invoke(args: Varargs): Varargs {
            var m = args.checkdouble(1)
            var i = 2
            val n = args.narg()
            while (i <= n) {
                m = Math.min(m, args.checkdouble(i))
                ++i
            }
            return LuaValue.valueOf(m)
        }
    }

    private class Modf : VarArgFunction() {
        override fun invoke(args: Varargs): Varargs {
            val x = args.checkdouble(1)
            val intPart = if (x > 0) Math.floor(x) else Math.ceil(x)
            val fracPart = x - intPart
            return LuaValue.varargsOf(LuaValue.valueOf(intPart), LuaValue.valueOf(fracPart))
        }
    }

    private class LuaRandom : LibFunction() {
        var random = Random()
        override fun call(): LuaValue = LuaValue.valueOf(random.nextDouble())

        override fun call(a: LuaValue): LuaValue {
            val m = a.checkint()
            if (m < 1) LuaValue.argerror(1, "interval is empty")
            return LuaValue.valueOf(1 + random.nextInt(m))
        }

        override fun call(a: LuaValue, b: LuaValue): LuaValue {
            val m = a.checkint()
            val n = b.checkint()
            if (n < m) LuaValue.argerror(2, "interval is empty")
            return LuaValue.valueOf(m + random.nextInt(n + 1 - m))
        }

    }

    private class Randomseed(val random: LuaRandom) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            val seed = arg.checklong()
            random.random = Random(seed)
            return LuaValue.NONE
        }
    }

    private class Choose(private val random: LuaRandom) : VarArgFunction() {
        override fun invoke(args: Varargs): Varargs {
            if(args.narg() < 1) {
                argerror("actual arguments")
                return LuaValue.varargsOf(arrayOf(LuaValue.NONE))
            }

            val kotlinArgs: ArrayList<LuaValue> = arrayListOf()
            (1..args.narg()).mapTo(kotlinArgs) { args.arg(it) }

            return LuaValue.varargsOf(arrayOf(kotlinArgs[random.random.nextInt(kotlinArgs.size)]))
        }
    }
}