package egorka.artemiyev.naildesignconstructor.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import egorka.artemiyev.naildesignconstructor.databinding.DemoItemBinding
import egorka.artemiyev.naildesignconstructor.model.MasterWorkList

class DemoListAdapter(val context: Context, private val list: MasterWorkList) : RecyclerView.Adapter<DemoListAdapter.DemoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        return DemoViewHolder(DemoItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
        Glide.with(holder.binding.imageView)
            .load(list[position].image)
            .into(holder.binding.imageView)
    }

    override fun getItemCount(): Int = 16

    class DemoViewHolder(val binding: DemoItemBinding) : ViewHolder(binding.root)
}