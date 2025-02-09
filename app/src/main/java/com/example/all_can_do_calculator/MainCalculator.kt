package com.example.all_can_do_calculator

import java.math.*
import java.text.DecimalFormat
import java.util.LinkedList
import java.util.Stack

class MainCalculator {
    lateinit var input : String
    lateinit var result : String
    var history : LinkedList<String> = LinkedList()
    var isError : Boolean = false

    fun addToHistory(){
        println(input + " = " + result)
        if(!isError){
            history.add(input + " = " + result)
            if(history.size >= 7) history.removeFirst()
        }
    }
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
                    '÷' -> {
                        try {
                            res = a.divide(b,precision,roundingMode)
                        } catch (e : ArithmeticException){
                            result = "Can't Divide by Zero"
                            isError = true
                            return
                        }
                    }
                    '×' -> res = a * b
                    '^' -> res = a.pow(b.toInt())
                }
                st.push(res.toString())
            }
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