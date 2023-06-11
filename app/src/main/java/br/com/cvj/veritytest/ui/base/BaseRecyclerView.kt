package br.com.cvj.veritytest.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface IBindable<T> {
    fun bind(data: T)
}

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), IBindable<T> {
    abstract fun bindData(item: T)
}

abstract class BaseRecyclerViewAdapter<T>(private val items: MutableList<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract fun getViewHolder(view: View): BaseViewHolder<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getLayoutRes(), parent, false)
        return getViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as IBindable<T>).bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    abstract fun getLayoutRes(): Int
}


