package egorka.artemiyev.naildesignconstructor.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import egorka.artemiyev.naildesignconstructor.databinding.DemoItemBinding

class DemoListAdapter(val context: Context, private val list: List<String>) : RecyclerView.Adapter<DemoListAdapter.DemoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        return DemoViewHolder(DemoItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
        holder.binding.imageView.setOnLongClickListener {

            false
        }
    }

    override fun getItemCount(): Int = 16

    class DemoViewHolder(val binding: DemoItemBinding) : ViewHolder(binding.root)
}