package egorka.artemiyev.naildesignconstructor.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import egorka.artemiyev.naildesignconstructor.databinding.NailImageViewBinding
import egorka.artemiyev.naildesignconstructor.model.NailImage

class NailImageAdapter(private val context: Context, private val list: NailImage, val tint: String) : RecyclerView.Adapter<NailImageAdapter.NailImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NailImageViewHolder {
        return NailImageViewHolder(NailImageViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: NailImageViewHolder, position: Int) {
        Glide.with(holder.binding.root)
            .load(list[position].image)
            .into(holder.binding.root)
        holder.binding.root.setColorFilter(Color.parseColor(tint))
    }

    override fun getItemCount(): Int = list.size

    class NailImageViewHolder(val binding: NailImageViewBinding) : ViewHolder(binding.root)
}