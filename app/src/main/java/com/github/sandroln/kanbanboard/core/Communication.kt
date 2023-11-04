package com.github.sandroln.kanbanboard.core

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

interface Communication {

    interface Update<T : Any> : Mapper.Unit<T>

    interface Observe<T : Any> {
        fun observe(owner: LifecycleOwner, observer: Observer<T>) = Unit
    }

    interface Mutable<T : Any> : Update<T>, Observe<T>

    abstract class Abstract<T : Any>(private val liveData: MutableLiveData<T>) : Mutable<T> {

        override fun map(source: T) {
            liveData.value = source
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) =
            liveData.observe(owner, observer)
    }

    abstract class Single<T : Any> : Abstract<T>(SingleLiveEvent())

    abstract class Regular<T : Any> : Abstract<T>(MutableLiveData())
}

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }
}