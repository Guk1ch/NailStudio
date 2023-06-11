package egorka.artemiyev.naildesignconstructor.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import egorka.artemiyev.naildesignconstructor.databinding.ClientRecordBinding
import egorka.artemiyev.naildesignconstructor.model.SqlClient
import egorka.artemiyev.naildesignconstructor.model.utils.Case

class ClientTextAdapter(val context: Context, val list: ArrayList<SqlClient>, private val click: ClickClient) : RecyclerView.Adapter<ClientTextAdapter.TextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        return TextViewHolder(ClientRecordBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.binding.root.text = list[position].fullName
        holder.binding.root.setOnClickListener {
            Case.clientToRecord = list[position]
            click.clickClient()
        }
    }

    override fun getItemCount(): Int = list.size

    interface ClickClient{
        fun clickClient()
    }

    class TextViewHolder(val binding: ClientRecordBinding) : ViewHolder(binding.root)
}