package egorka.artemiyev.naildesignconstructor.view.adapter

import android.content.Context
import android.graphics.Point
import android.view.Display
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.databinding.GalleryViewBinding
import egorka.artemiyev.naildesignconstructor.model.FireImageModel
import egorka.artemiyev.naildesignconstructor.view.activities.MainActivity


class GalleryAdapter(val context: Context, private val list: MutableList<FireImageModel>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    private lateinit var display: Display

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(GalleryViewBinding.inflate(LayoutInflater.from(context), parent,false))
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = list[position]
        display = (context as MainActivity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        with(holder.binding){
            imgToFavorite.setImageResource(if (item.isInFavorite) R.drawable.heart_full else R.drawable.heart_border)
            img.layoutParams.width = (width / 3)
            imgToFavorite.setOnClickListener {
                item.isInFavorite = !item.isInFavorite
                imgToFavorite.setImageResource(if (item.isInFavorite) R.drawable.heart_full else R.drawable.heart_border)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    class GalleryViewHolder(val binding: GalleryViewBinding) : ViewHolder(binding.root)
}