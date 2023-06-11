package br.com.cvj.veritytest.util.extension

fun <T>Collection<T>?.whenIsEmpty(callback: () -> Unit): Collection<T> {
    if (this?.isEmpty() == true) callback()
    return this ?: emptyList()
}

fun <T>Collection<T>?.whenIsNotEmpty(callback: (collection: Collection<T>) -> Unit): Collection<T> {
    if (!this.isNullOrEmpty()) callback(this)
    return this ?: emptyList()
}