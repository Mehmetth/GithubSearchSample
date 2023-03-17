package com.mehmetpetek.githubsample.ui.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.mehmetpetek.githubsample.R
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AutoClearedFragmentValue<T : Any> : ReadWriteProperty<Fragment, T>, LifecycleObserver {
    private var _value: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        _value
            ?: throw IllegalStateException(thisRef.context?.getString(R.string.try_exception_mesage))

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        thisRef.viewLifecycleOwner.lifecycle.removeObserver(this)
        _value = value
        thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        _value = null
    }
}