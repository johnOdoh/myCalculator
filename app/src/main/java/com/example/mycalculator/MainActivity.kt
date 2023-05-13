package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var tvDisplay : TextView? = null
    private var isNumeric = false
    private var isNegative = false
    private var hasDot = false
    private val operatorList = mutableListOf<CharSequence>()
    private val hasDotStatus = mutableListOf<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)
    }

    fun addText(view : View){
//        Toast.makeText(this, "hello", Toast.LENGTH_LONG).show()
        tvDisplay?.append((view as Button).text)
        isNumeric = true
//        hasDot = false
    }
    fun addDot(view: View){
        if(isNumeric && !hasDot){ //check if the last value is numeric and there is not dot already
            tvDisplay?.append(".")
            isNumeric = false
            hasDot = true
        }else if(!isNumeric && !hasDot){ //puts 0 before the dot if the last value is not numeric
            tvDisplay?.append("0.")
            isNumeric = false
            hasDot = true
        }
    }
    fun clearDisplay(view: View){
        //return all values to default
        tvDisplay?.text = ""
        isNumeric = false
        hasDot = false
        isNegative = false
        operatorList.clear()
        hasDotStatus.clear()
    }
    fun backspace(view: View){
        if (tvDisplay?.text?.isEmpty() == false) { //check if the display is empty
            val lastChar = tvDisplay?.text?.last() //assign the value of the value to be removed to a variable
            tvDisplay?.text = tvDisplay?.text?.dropLast(1) //removes the last value form the display
            if(isSpecialChar(lastChar.toString())){ //if the deleted value is an operator
                operatorList.dropLast(1) //removes the operator from the operator list if lastchar is an operator
                hasDot = hasDotStatus.last() //assign the last value in the hasdotstatus list to hasdot
                hasDotStatus.dropLast(1) //drop the last value in the hasdotstatus list
                isNumeric = true
            }else if (lastChar == '.'){ //if the deleted value is a dot
                hasDot = false
                isNumeric = true
            }else isNumeric = tvDisplay?.text?.isEmpty() == false && tvDisplay?.text?.last()?.isDigit() == true
//            else if ((tvDisplay?.text?.isEmpty() == false && tvDisplay?.text?.last() != '.') || tvDisplay?.text?.isEmpty() == true) {
//                isNumeric = false
//                hasDot = false
//            }
        }
    }
    fun toggleNegative(view: View){
        if(!isNegative){
            val prevText = tvDisplay?.text
            tvDisplay?.text = "-"
            tvDisplay?.append(prevText)
            isNegative = true
        }else{
            tvDisplay?.text = tvDisplay?.text?.drop(1)
            isNegative = false
        }
    }
    fun specialChar(view: View){
        //check if the last character is numeric
        if(isNumeric) {
            addText(view)
            operatorList.add((view as Button).text) //add the operator to the operator list
            hasDotStatus.add(hasDot) //add the boolean value of the hasdot to the hasdotstatus list
            isNumeric = false
            hasDot = false
        }
    }
    fun equalsTO(view: View){
        // check if the textview is empty and the last char is not a special char
        if (tvDisplay?.text?.isEmpty() == false && !isSpecialChar(tvDisplay?.text?.last().toString())) {
            var displayText = tvDisplay?.text //assign the text on the display to a variable
            var negativePrefix = false
            if (displayText?.first() == '-') { //check if a -ve sigh is at the beginning of the display
                negativePrefix = true
                displayText = displayText.drop(1) //drop the -ve sigh so it wont affect the coming split method
            }
            val textArray = displayText?.split('+', '-', '*', '/')?.toMutableList() //split the text using the possible operators
            // add the +ve sigh in the beginning of the operator list so it can have the same length as the textarray
            operatorList.add(0, "+")
            var solution = 0.0
            for (i in textArray?.indices!!) {
                if (i == 0 && negativePrefix) textArray[i] = "-"+textArray[i] //add the -ve sign back to operation if it exists
                solution = convert(solution, textArray[i].toDouble(), operatorList[i]) // use the convert() to run the arithemetic operation
            }
            //return the values to default
            operatorList.clear()
            hasDotStatus.clear()
            hasDot = true
            isNumeric = true
            isNegative = solution.toString().first() == '-' //check if the solution contain a -ve sigh at the beginning and assign the value to isNegative
            tvDisplay?.text = solution.toString()
        }
    }
    private fun isSpecialChar(char : String) : Boolean{
        return char.contains(Regex("[+*/-]"))
    }
    private fun convert(value1 : Double, value2 : Double, operator : CharSequence) : Double {
        return when(operator){
            "+" -> value1 + value2
            "-" -> value1 - value2
            "*" -> value1 * value2
            "/" -> value1 / value2
            else -> 0.0
        }
    }
}
