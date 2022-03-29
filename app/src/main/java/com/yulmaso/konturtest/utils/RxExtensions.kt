package com.yulmaso.konturtest.utils

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableTransformer
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.internal.functions.Functions
import java.util.concurrent.atomic.AtomicBoolean


fun <T: Any> doOnFirst(consumer: Consumer<in T>): FlowableTransformer<T, T> {
    return FlowableTransformer { f: Flowable<T> ->
        Flowable.defer {
            val first = AtomicBoolean(true)
            f.doOnNext { t: T ->
                if (first.compareAndSet(true, false)) {
                    consumer.accept(t)
                }
            }
        }
    }
}

fun <T: Any> Flowable<T>.doOnFirst(consumer: Consumer<in T>): Flowable<T> {
    return this.compose(com.yulmaso.konturtest.utils.doOnFirst(consumer))
}