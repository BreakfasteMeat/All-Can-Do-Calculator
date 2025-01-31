package com.example.all_can_do_calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnAssignActions()
    }

    private fun btnAssignActions(){
        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)
        val btn3 = findViewById<Button>(R.id.btn3)
        val btn4 = findViewById<Button>(R.id.btn4)
        val btn5 = findViewById<Button>(R.id.btn5)
        val btn6 = findViewById<Button>(R.id.btn6)
        val btn7 = findViewById<Button>(R.id.btn7)
        val btn8 = findViewById<Button>(R.id.btn8)
        val btn9 = findViewById<Button>(R.id.btn9)
        val numButtons = listOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)

        val textView = findViewById<TextView>(R.id.txtvwEquation)
        numButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                textView.append((index + 1).toString())
            }
        }

        findViewById<Button>(R.id.btnBackspace).setOnClickListener{
            if(textView.text.toString().isNotEmpty())
            textView.text = textView.text.toString().substring(0,textView.text.toString().length - 1)
        }
        findViewById<Button>(R.id.btnDiv).setOnClickListener{
            textView.append("÷")
        }
        findViewById<Button>(R.id.btnMin).setOnClickListener{
            textView.append("-")
        }
        findViewById<Button>(R.id.btnMul).setOnClickListener{
            textView.append("×")
        }
        //TODO("CREATE ADD BUTTON")
        //findViewById<Button>(R.id.btnAllClear).setOnClickListener{
        //    textView.text = ""
        //}
        findViewById<Button>(R.id.btnAllClear).setOnClickListener{
            textView.text = ""
        }
    }

}