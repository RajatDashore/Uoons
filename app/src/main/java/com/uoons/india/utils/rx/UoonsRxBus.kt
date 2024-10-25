package com.uoons.india.utils.rx

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
object UoonsRxBus {

    private val publisher = PublishSubject.create<Any>()
    private val refferal = PublishSubject.create<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    fun refferalCode(event: Any) {
        refferal.onNext(event)
    }

    // Listen should return an Observable and not the publisher
    // Using ofType we filter only events that match that class type
    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)

    fun <T> refferalListen(eventType: Class<T>): Observable<T> = refferal.ofType(eventType)
}