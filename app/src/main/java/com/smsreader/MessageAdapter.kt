package com.smsreader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smsreader.data.Messages
import com.smsreader.databinding.ItemViewBinding

class MessageAdapter : RecyclerView.Adapter<DataViewHolder>() {

    private var list: List<Messages> = ArrayList()

    fun setData(data: List<Messages>) {
        list = data
        notifyDataSetChanged()
    }

    fun addNewMessage(msg: Messages) {
        list = listOf(msg).plus(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding: ItemViewBinding =
            ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}

class DataViewHolder(private val itemBinding: ItemViewBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var message: Messages
    fun bind(dataItem: Messages) {
        try {
            message = dataItem
            itemBinding.message.text = "Message: ${message.message}"
            itemBinding.date.text = "Date: ${message.date}"
            itemBinding.price.text = "Price: ${message.cost}"
            itemBinding.created.text = "Create On: ${message.created.toString()}"

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}