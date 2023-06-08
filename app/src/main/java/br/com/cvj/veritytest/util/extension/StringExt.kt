package br.com.cvj.veritytest.util.extension

import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.toPatternMatcherWithRegex(regex: String, pattern: Int = Pattern.MULTILINE): Matcher? {
    val patternCompile = Pattern.compile(regex, pattern)
    return patternCompile.matcher(this)
}