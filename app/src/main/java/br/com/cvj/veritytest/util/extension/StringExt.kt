package br.com.cvj.veritytest.util.extension

import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.fromHtml
import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.toPatternMatcherWithRegex(regex: String, pattern: Int = Pattern.MULTILINE): Matcher? {
    val patternCompile = Pattern.compile(regex, pattern)
    return patternCompile.matcher(this)
}


fun String?.whenIsNotEmpty(callback: (text: String) -> Unit): String {
    if (this == null) return ""

    if (isNotEmpty()) {
        callback(this)
    }

    return this
}

fun String?.whenIsEmpty(callback: () -> Unit): String {
    if (this == null) return ""

    if (this.isEmpty() || this.isBlank()) {
        callback()
    }
    return this
}

fun String.styled(): Spanned {
    return fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
}
