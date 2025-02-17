package com.example.all_can_do_calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import org.w3c.dom.Text
import java.math.BigDecimal
import java.util.LinkedList

class MainCalculatorFragment : Fragment(R.layout.fragment_main_calculator) {

    val mainCalculator : MainCalculator = MainCalculator()
    var calculatedResult : Boolean = false;
    lateinit var txtvwEquation : TextView;
    lateinit var txtvwRecentHistory : TextView
    var navView : NavigationView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAssignActions(view)
        navView = activity?.findViewById(R.id.nav_view)
    }

    private fun updateRecentHistory(){
        txtvwRecentHistory.text = mainCalculator.history.joinToString("\n")
    }

    private fun btnAssignActions(view : View){

        val btn1 = view.findViewById<Button>(R.id.btn1)
        val btn2 = view.findViewById<Button>(R.id.btn2)
        val btn3 = view.findViewById<Button>(R.id.btn3)
        val btn4 = view.findViewById<Button>(R.id.btn4)
        val btn5 = view.findViewById<Button>(R.id.btn5)
        val btn6 = view.findViewById<Button>(R.id.btn6)
        val btn7 = view.findViewById<Button>(R.id.btn7)
        val btn8 = view.findViewById<Button>(R.id.btn8)
        val btn9 = view.findViewById<Button>(R.id.btn9)
        val numButtons = listOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)

        txtvwRecentHistory = view.findViewById<TextView>(R.id.txtvwRecentHistory)
        txtvwEquation = view.findViewById<TextView>(R.id.txtvwEquation)
        numButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                updateText(txtvwEquation,(index + 1).toString())
            }
        }

        view.findViewById<Button>(R.id.btnBackspace).setOnClickListener{
            checkError(txtvwEquation)
            if(txtvwEquation.text.toString().isNotEmpty())
                txtvwEquation.text = txtvwEquation.text.toString().substring(0,txtvwEquation.text.toString().length - 1)
        }
        view.findViewById<Button>(R.id.btnDiv).setOnClickListener{
            if(txtvwEquation.text.isEmpty()){
                return@setOnClickListener
            }
            if(txtvwEquation.text.toString().last() == '.' || txtvwEquation.text.toString().last().isOp())
                txtvwEquation.text = txtvwEquation.text.toString().substring(0,txtvwEquation.text.toString().length - 1)
            updateText(txtvwEquation,"÷")
        }
        view.findViewById<Button>(R.id.btnMin).setOnClickListener{
            if(txtvwEquation.text.isEmpty()){
                return@setOnClickListener
            }
            if(txtvwEquation.text.toString().last() == '.' || txtvwEquation.text.toString().last().isOp())
                txtvwEquation.text = txtvwEquation.text.toString().substring(0,txtvwEquation.text.toString().length - 1)
            updateText(txtvwEquation,"-")
        }
        view.findViewById<Button>(R.id.btnMul).setOnClickListener{
            if(txtvwEquation.text.isEmpty()){
                return@setOnClickListener
            }
            if(txtvwEquation.text.toString().last() == '.' || txtvwEquation.text.toString().last().isOp())
                txtvwEquation.text = txtvwEquation.text.toString().substring(0,txtvwEquation.text.toString().length - 1)
            updateText(txtvwEquation,"×")
        }
        view.findViewById<Button>(R.id.btnAdd).setOnClickListener{
            if(txtvwEquation.text.isEmpty()){
                return@setOnClickListener
            }
            if(txtvwEquation.text.toString().last() == '.' || txtvwEquation.text.toString().last().isOp())
                txtvwEquation.text = txtvwEquation.text.toString().substring(0,txtvwEquation.text.toString().length - 1)
            updateText(txtvwEquation,"+")
        }
        view.findViewById<Button>(R.id.btn0).setOnClickListener{
            updateText(txtvwEquation,"0")
        }
        view.findViewById<Button>(R.id.btn00).setOnClickListener{
            updateText(txtvwEquation,"00")
        }
        view.findViewById<Button>(R.id.btnDecPoint).setOnClickListener{
            for(chr in txtvwEquation.text.toString().reversed()){
                if(chr == '.') {
                    return@setOnClickListener
                }
                if(chr.isOp()){
                    break
                }
                if(mainCalculator.isError){
                    txtvwEquation.text = ""
                    return@setOnClickListener
                }
            }
            txtvwEquation.append(".")
        }
        view.findViewById<Button>(R.id.btnEquals).setOnClickListener {
            if(txtvwEquation.text.toString().isEmpty()) return@setOnClickListener
            mainCalculator.input = txtvwEquation.text.toString()
            try {
                mainCalculator.calculate()
                txtvwEquation.text = mainCalculator.result
            } catch (e : RuntimeException){
                txtvwEquation.text = "Error"
            }
            calculatedResult = true
        }
        view.findViewById<Button>(R.id.btnAllClear).setOnClickListener {
            updateText(txtvwEquation,"")
        }
        view.findViewById<Button>(R.id.btnOpenPar).setOnClickListener {
            updateText(txtvwEquation,"(")
        }
        view.findViewById<Button>(R.id.btnClosePar).setOnClickListener {
            updateText(txtvwEquation,")")
        }
        view.findViewById<Button>(R.id.btnPercent).setOnClickListener{
            var num = ""
            var ctr = 0
            for(chr in txtvwEquation.text.toString().reversed()){
                if(chr.isOp() || chr == '(' || chr == ')') break;
                ctr++
                num = chr.toString() + num
            }

            var number = num.toBigDecimal()
            number = number.multiply(BigDecimal("0.01"))
            txtvwEquation.text = txtvwEquation.text.toString().substring(0,txtvwEquation.text.toString().length - ctr)
            println(number)
            updateText(txtvwEquation,number.toString())
        }
        view.findViewById<Button>(R.id.btnExponent).setOnClickListener{
            updateText(txtvwEquation,"^")
        }


        view.findViewById<ImageButton>(R.id.imgbtnDrawerOpen).setOnClickListener {
            navView?.visibility = if(navView?.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }
    private fun updateText(textView : TextView, string : String) {
        if(calculatedResult){
            calculatedResult = false
            mainCalculator.addToHistory()
            updateRecentHistory()
            if(string.isEmpty()){
                textView.setText("")
                return
            } else if(string.first().isDigit()){
                textView.text = ""
            }
        }
        checkError(textView)
        if(string.isEmpty()) textView.setText("")
        else textView.append(string)
    }
    private fun checkError(textView: TextView){
        if (mainCalculator.isError) {
            textView.text = ""
            mainCalculator.isError = false
        }
    }
}