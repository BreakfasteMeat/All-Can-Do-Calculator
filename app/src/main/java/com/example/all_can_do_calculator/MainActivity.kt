package com.example.all_can_do_calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

//EXTENSION FUNCTIONS
fun Char.isOp() : Boolean{
    return this == '+' || this == '-' || this == '÷' || this == '×'
}
fun Char.opPrec() : Int{
    when(this){
        '÷' -> return 2
        '×' -> return 2
        '-' -> return 1
        '+' -> return 1
    }
    return -1
}
class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

}