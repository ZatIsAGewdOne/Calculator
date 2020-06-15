package com.playground.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var result: EditText? = null
    var newNumber: EditText? = null
    var displayOperation: TextView? = null

    var operand1: Double? = null
    var pendingOperation: String = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)
        displayOperation = findViewById(R.id.operation)

        val numberKeyArray = listOf(
            findViewById<Button>(R.id.button0), findViewById(R.id.button1), findViewById(R.id.button2),
            findViewById(R.id.button3), findViewById(R.id.button4), findViewById(R.id.button5),
            findViewById(R.id.button6), findViewById(R.id.button7), findViewById(R.id.button8),
            findViewById(R.id.button9), findViewById(R.id.button_dot)
        )

        val operationKeysArray = listOf(
            findViewById<Button>(R.id.button_eq), findViewById(R.id.button_div), findViewById(R.id.button_mult),
            findViewById(R.id.button_min), findViewById(R.id.button_add)
        )

        val listener = View.OnClickListener {
            val b = it as Button
            newNumber?.append(b.text).toString()
        }

        numberKeyArray.forEach {
            it.setOnClickListener(listener)
        }

        val opListener = View.OnClickListener {
            val b = it as Button
            val op = b.text.toString()
            val value = newNumber?.text.toString()
            try {
                val doubleValue = java.lang.Double.valueOf(value)
                performOperation(doubleValue, op)
            } catch (e: NumberFormatException) {
                newNumber?.setText("")
            }
            pendingOperation = op
            displayOperation?.text = pendingOperation
        }

        operationKeysArray.forEach { it.setOnClickListener(opListener) }

        findViewById<Button>(R.id.button_neg).setOnClickListener {
            val stringValue = newNumber?.text.toString()
            if (stringValue.isEmpty()) {
                newNumber?.append("-").toString()
            } else {
                try {
                    var doubleValue = java.lang.Double.valueOf(stringValue)
                    doubleValue *= -1
                    newNumber?.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    newNumber?.setText("")
                }
            }
        }
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }
            when(pendingOperation) {
                "=" -> operand1 = value
                "/" -> {
                    operand1 = if (value == 0.toDouble()) {
                        0.0
                    } else {
                        operand1!! / value
                    }
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result?.setText(operand1.toString())
        newNumber?.setText("")
    }

    private fun setupNumberKeys() {

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pendingOperation = savedInstanceState.getString("operation")!!
        displayOperation?.text = savedInstanceState.getString("operation")
        operand1 = savedInstanceState.getDouble("operand")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("operation", displayOperation?.text.toString())
        if (operand1 != null) {
            outState.putDouble("operand", operand1!!)
        }
        super.onSaveInstanceState(outState)
    }
}