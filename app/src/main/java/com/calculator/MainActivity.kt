package com.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.core.view.children
import com.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var firstNumber = ""
    private var currentNumber = ""
    private var currentOperator = ""
    private var result = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.apply {
            layoutMain.children.filterIsInstance<Button>().forEach { button ->
                button.setOnClickListener {
                    val buttonText = button.text.toString()

                    when {

                        buttonText.matches(Regex("[0-9]")) -> {
                            if (currentOperator.isEmpty()) {
                                firstNumber += buttonText
                                tvResult.text = firstNumber
                            } else {
                                currentNumber += buttonText
                                tvResult.text = currentNumber
                            }
                        }

                        buttonText.matches(Regex("[+\\-*/]")) -> {
                            if (firstNumber.isNotEmpty()) {
                                currentOperator = buttonText
                                currentNumber = ""
                                tvFormula.text = "$firstNumber $currentOperator"
                                tvResult.text = "0"
                            }
                        }

                        buttonText == "=" -> {
                            if (firstNumber.isNotEmpty() && currentNumber.isNotEmpty() && currentOperator.isNotEmpty()) {
                                tvFormula.text = "$firstNumber $currentOperator $currentNumber"
                                result = evaluateExpression(firstNumber, currentNumber, currentOperator)
                                firstNumber = result
                                currentNumber = ""
                                currentOperator = ""
                                tvResult.text = result
                            }
                        }

                        buttonText == "." -> {
                            if (currentOperator.isEmpty()) {
                                if (!firstNumber.contains(".")) {
                                    firstNumber += if (firstNumber.isEmpty()) "0$buttonText" else buttonText
                                    tvResult.text = firstNumber
                                }
                            } else {
                                if (!currentNumber.contains(".")) {
                                    currentNumber += if (currentNumber.isEmpty()) "0$buttonText" else buttonText
                                    tvResult.text = currentNumber
                                }
                            }
                        }

                        buttonText.equals("C", ignoreCase = true) -> {
                            firstNumber = ""
                            currentNumber = ""
                            currentOperator = ""
                            result = ""
                            tvResult.text = "0"
                            tvFormula.text = ""
                        }
                    }
                }
            }
        }
    }

    private fun evaluateExpression(firstNumber: String, secondNumber: String, operator: String): String {
        val num1 = firstNumber.toDoubleOrNull() ?: return "Error"
        val num2 = secondNumber.toDoubleOrNull() ?: return "Error"

        return when (operator) {
            "+" -> (num1 + num2).toString()
            "-" -> (num1 - num2).toString()
            "*" -> (num1 * num2).toString()
            "/" -> if (num2 != 0.0) (num1 / num2).toString() else "Error"
            else -> "Error"
        }
    }
}
