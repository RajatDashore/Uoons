package com.uoons.india.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
abstract class BaseRecyclerAdapter<T : ViewDataBinding, V : Any, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder(createDataBinding(parent, viewType), parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, position, 1)
    }

    abstract fun onBindViewHolder(holder: VH, position: Int, type: Int)

    abstract fun onCreateViewHolder(viewDataBinding: T, parent: ViewGroup, viewType: Int): VH

    @LayoutRes
    abstract fun getLayoutId(viewType: Int): Int

    private fun createDataBinding(parent: ViewGroup, viewType: Int): T {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayoutId(viewType),
            parent,
            false
        )
    }

    abstract inner class BaseViewHolder(mViewDataBinding: T) :
        RecyclerView.ViewHolder(mViewDataBinding.root) {
        var viewDataBinding: T
            internal set

        abstract val bindingVariable: Int
        abstract val viewModel: V

        init {
            this.viewDataBinding = mViewDataBinding
        }

        open fun initHolder(){}

        open fun bindTo(position: Int) {}

        fun bindToDataVM(vm: Int, objecct: Any) {
            viewDataBinding.setVariable(vm, objecct)
            viewDataBinding.executePendingBindings()
        }
    }

    fun onItemClicked(position: Int) {}
    fun onItemRemoved(position: Int) {}
    fun onItemDetail(position: Int) {}
    fun onItemCancelled(position: Int) {}
    fun onItemRefreshed(position: Int) {}
    fun onItemEvent(vararg event: Any) {}
}