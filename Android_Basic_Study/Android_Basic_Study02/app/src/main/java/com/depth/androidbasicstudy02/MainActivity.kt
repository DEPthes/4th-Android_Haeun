package com.depth.androidbasicstudy02

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.depth.androidbasicstudy02.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn0.setOnClickListener {
            Log.d("Btn", "Button clicked")
        }

        val numberButtons = listOf(
            binding.btn0 to "0",
            binding.btn1 to "1",
            binding.btn2 to "2",
            binding.btn3 to "3",
            binding.btn4 to "4",
            binding.btn5 to "5",
            binding.btn6 to "6",
            binding.btn7 to "7",
            binding.btn8 to "8",
            binding.btn9 to "9",
            binding.btnPlus to "+",
            binding.btnSubtraction to "-",
            binding.btnMultiply to "*",
            binding.btnDivide to "/"
        )

        numberButtons.forEach { (button, value) ->
            button.setOnClickListener {
                Log.d("ButtonClick", "Button clicked with value: $value")  // 로그 추가
                binding.tvCalculate.append(value)  // TextView에 텍스트 추가
            }
        }

        binding.btnEqual.setOnClickListener {
            val input = binding.tvCalculate.text.toString()

            // 입력이 비어있지 않은지 확인
            if (input.isNotEmpty()) {
                val tokens = Regex("""\d+|\+|\-|\*|\/""").findAll(input).map { it.value }.toList()

                try {
                    // 수식을 평가
                    val result = evaluateExpression(tokens)
                    binding.tvResult.text = result.toString()
                } catch (e: Exception) {
                    binding.tvResult.text = "Error"
                }
            }
        }

        // Clear 버튼 클릭 리스너
        binding.btnCancel.setOnClickListener {
            binding.tvCalculate.text = ""
            binding.tvResult.text = ""
        }
    }

    // 수식 계산 함수
    fun evaluateExpression(tokens: List<String>): Double {
        val numbers = mutableListOf<Double>()
        val operators = mutableListOf<String>()

        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]
            when (token) {
                "+", "-", "*", "/" -> operators.add(token)
                else -> numbers.add(token.toDouble())
            }
            i++
        }

        // 1단계: *, / 먼저 처리
        var index = 0
        while (index < operators.size) {
            val op = operators[index]
            if (op == "*" || op == "/") {
                val result = if (op == "*") {
                    numbers[index] * numbers[index + 1]
                } else {
                    numbers[index] / numbers[index + 1]
                }
                numbers[index] = result
                numbers.removeAt(index + 1)
                operators.removeAt(index)
            } else {
                index++
            }
        }

        // 2단계: +, - 처리
        index = 0
        while (index < operators.size) {
            val op = operators[index]
            val result = if (op == "+") {
                numbers[index] + numbers[index + 1]
            } else {
                numbers[index] - numbers[index + 1]
            }
            numbers[index] = result
            numbers.removeAt(index + 1)
            operators.removeAt(index)
        }

        return numbers.first()
    }
}
