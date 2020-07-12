package com.ijikod.droplet.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.ijikod.droplet.Application.DropletApp
import com.ijikod.droplet.R

/**
 * An abstract Base View model to share common behavior and additionally clear navigation reference upon invalidation.
 */
abstract class BaseViewModel<View> : ViewModel() {
    private var view: View? = null

    /**
     * This method must be called by the UI to attach navigation to be monitored by the substituted view model to respond to UI specific event changes.
     * @param navigator reference to navigation
     */
    open fun attachView(view: View, lifecycleOwner: LifecycleOwner) {
        this.view = view
    }

    /**
     * @returns current navigator if attached
     * @throws NullPointerException if not attached.
     */
    protected fun getView(): View {
        if (view == null)
            throw NullPointerException(DropletApp.appContext.getString(R.string.view_null_exception))

        return view!!
    }

    override fun onCleared() {
        view = null
    }
}