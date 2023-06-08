package br.com.cvj.veritytest.util
import br.com.cvj.veritytest.util.extension.toPatternMatcherWithRegex

@Suppress("UNUSED")
object StringUtil {
    fun removeCharsAZ(text: String) = text.toPatternMatcherWithRegex("[\\[a-zA-Z]")

    fun getFirstSequenceOfNumbersAsString(text: String?): String {
        var numbersAsString = ""

        text?.forEachIndexed { i, char ->
            if (char.isDigit()) {
                numbersAsString += char
            }

            if (text.elementAtOrNull(i)?.isDigit() == false) {
                return numbersAsString
            }
        }

        return numbersAsString
    }
}