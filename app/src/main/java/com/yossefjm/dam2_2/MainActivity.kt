package com.yossefjm.dam2_2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {

    private lateinit var textIn: TextView
    private lateinit var textOut: TextView

    private var inputStr = ""
    private var result = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establecer la vista de contenido de la actividad como el diseño XML "activity_main"
        setContentView(R.layout.activity_main)

        // Inicializar las variables textIn y textOut con las vistas TextView correspondientes
        textIn = findViewById(R.id.textIn)
        textOut = findViewById(R.id.textOut)

        // Crear un array de identificadores de botones que serán utilizados posteriormente
        val buttons = arrayOf(
            R.id.uno, R.id.dos, R.id.tres, R.id.cuatro, R.id.cinco,
            R.id.seis, R.id.siete, R.id.ocho, R.id.nueve, R.id.cero,
            R.id.suma, R.id.resta, R.id.multi, R.id.divi, R.id.coma,
            R.id.percen, R.id.backspace, R.id.igual, R.id.mas, R.id.backspaceAll
        )

        // Configurar un OnClickListener para cada botón en el array
        for (buttonId in buttons) {
            // Buscar la vista del botón por su identificador
            val button = findViewById<Button>(buttonId)
            // Asignar un OnClickListener que llama a la función onButtonClick(button) cuando se hace clic en el botón
            button.setOnClickListener(::onButtonClick)
        }
    }

    private fun onButtonClick(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()

        when (button.id) {
            R.id.backspaceAll -> {
                if (inputStr.isNotEmpty()) {
                    inputStr = ""
                }
            }
            R.id.backspace -> {
                if (inputStr.isNotEmpty()) {
                    inputStr = inputStr.substring(0, inputStr.length - 1)
                }
            }
            R.id.multi -> {
                if(inputStr.isNotEmpty()) {
                    inputStr += "*"
                }
            }
            R.id.divi -> {
                if(inputStr.isNotEmpty()) {
                    inputStr += "/"
                }
            }

            R.id.igual -> {
                try {
                    calcular(inputStr)

                } catch (e: Exception) {
                    result = "Error"
                }

            }
            else -> inputStr += buttonText
        }

        // Actualizar el contenido de las vistas textIn y textOut
        textIn.text = inputStr
        textOut.text = result
    }

    // Función para calcular el resultado de la operación introducida
    private fun calcular(inputStr: String) {
        try {
            // Utilizamos una expresión regular para separar los números y operadores
            val pattern = Pattern.compile("(-?\\d+(\\.\\d+)?)|([-+*/])")
            // Utilizamos un Matcher para encontrar los números y operadores
            val tokens = pattern.matcher(inputStr)
            // Creamos dos listas para almacenar los números y operadores
            val stackNumbers = mutableListOf<Double>()
            val stackOperators = mutableListOf<String>()

            // Iteramos sobre los tokens encontrados
            while (tokens.find()) {
                val token = tokens.group()
                if (token.matches(Regex("-?\\d+(\\.\\d+)?"))) {
                    val number = token.toDouble()
                    stackNumbers.add(number)
                } else {
                    while (stackOperators.isNotEmpty() && precedence(stackOperators.last()) >= precedence(token)) {
                        performOperation(stackNumbers, stackOperators)
                    }
                    stackOperators.add(token)
                }
            }

            while (stackOperators.isNotEmpty()) {
                performOperation(stackNumbers, stackOperators)
            }

            result = stackNumbers.first().toString()
        } catch (e: Exception) {
            result = "Error"
        }
    }

    private fun precedence(operator: String): Int {
        return when (operator) {
            "+", "-" -> 1
            "*", "/" -> 2
            else -> 0
        }
    }


    private fun performOperation(numbers: MutableList<Double>, operators: MutableList<String>) {
        val operator = operators.removeAt(operators.size - 1)
        val number2 = numbers.removeAt(numbers.size - 1)
        val number1 = numbers.removeAt(numbers.size - 1)

        when (operator) {
            "+" -> numbers.add(number1 + number2)
            "-" -> numbers.add(number1 - number2)
            "*" -> numbers.add(number1 * number2)
            "/" -> numbers.add(number1 / number2)
        }
    }



}
