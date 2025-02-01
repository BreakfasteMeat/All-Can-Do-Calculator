package com.example.all_can_do_calculator

import java.math.*
import java.text.DecimalFormat
import java.util.Stack

class MainCalculator {
    public lateinit var input : String
    public lateinit var result : String

    fun toStringList() : ArrayList<String>{
        val sBldr = StringBuilder()
        var ret = ArrayList<String>()
        for(c in input){
            if(c.isWhitespace()){
                continue
            } else if(c.isDigit() || c == '.' || c == 'E'){
                sBldr.append(c)
            } else if(c.isOp()){
                if(sBldr.isNotEmpty()){
                    if (sBldr.last() == '.') sBldr.deleteCharAt(sBldr.length)
                    ret.add(sBldr.toString())
                }
                sBldr.clear()
                ret.add(c.toString())
            } else if(c == '('){
                ret.add(c.toString())
            } else if(c == ')'){
                if(sBldr.last() == '.') sBldr.deleteCharAt(sBldr.length)
                ret.add(sBldr.toString())
                sBldr.clear()
                ret.add(c.toString())
            }
        }
        if(sBldr.isNotEmpty()) ret.add(sBldr.toString())
        return ret
    }
    fun calculate(){
        val postfix = infixToPostfix(toStringList())
        val st = Stack<String>()
        try {
            for (str in postfix) {
                if (str.first().isDigit()) {
                    st.push(str)
                } else if (str.first().isOp()) {
                    val b = st.pop().toBigDecimal()
                    val a = st.pop().toBigDecimal()
                    val precision = 10
                    val roundingMode = RoundingMode.HALF_UP
                    var res = BigDecimal.ZERO
                    when (str.first()) {
                        '+' -> res = a + b
                        '-' -> res = a - b
                        '÷' -> res = a.divide(b,precision,roundingMode)
                        '×' -> res = a * b
                    }
                    st.push(res.toString())
                }
            }
        } catch (e : Exception){
            result = "SOMETHING WENT HORRIBLY WRONG"
            return
        }
        val res : BigDecimal = st.pop().toBigDecimal().stripTrailingZeros()
        val formattedRes = if(res.abs() >= BigDecimal("1E10")){
            DecimalFormat("0.#####E0").format(res)
        } else {
            res.toPlainString()
        }
        result = formattedRes
    }
    fun infixToPostfix(infix : ArrayList<String>) :  ArrayList<String>{
        val postfix : ArrayList<String> = ArrayList()
        val stack = Stack<String>()
        var prevWasOp = true

        for(vals in infix){
            if(vals.first().isDigit()){
                postfix.add(vals)
                prevWasOp = false
            } else if(vals.first() == '('){
                stack.push(vals)
                prevWasOp = true
            } else if(vals.first() == ')'){
                while (stack.peek().first() != '('){
                    postfix.add(stack.pop())
                }
                stack.pop()
                prevWasOp = false
            } else if(vals.first().isOp()){
                if (vals == "-" && prevWasOp) {  // Detect unary minus
                    postfix.add("0")  // Add 0 before unary minus
                }

                while (stack.isNotEmpty() &&
                    vals.first().opPrec() <= stack.peek().first().opPrec() &&
                    stack.peek().first() != '(') {
                    postfix.add(stack.pop())
                }
                stack.push(vals)
                prevWasOp = true
            }
        }
        while(stack.isNotEmpty()){
            postfix.add(stack.pop())
        }
        return postfix
    }
}