package com.xhhold.flutter_record

import kotlinx.coroutines.withTimeout
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

suspend fun <T> withCoroutine(
    timeMillis: Long = 10000, block: Continuation<T>.() -> Unit
): Result<T> {
    return try {
        Result.success(withTimeout(timeMillis) {
            suspendCoroutine(block)
        })
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
