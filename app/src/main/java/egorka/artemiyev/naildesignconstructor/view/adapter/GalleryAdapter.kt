package egorka.artemiyev.naildesignconstructor.view.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.GalleryViewBinding
import egorka.artemiyev.naildesignconstructor.model.MasterWork
import egorka.artemiyev.naildesignconstructor.model.MasterWorkList
import egorka.artemiyev.naildesignconstructor.model.utils.Case
import egorka.artemiyev.naildesignconstructor.view.activities.MainActivity


class GalleryAdapter(val context: Context, private val list: MasterWorkList, private val longClick: LongClick) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    private lateinit var display: Display
    private val listFavorite = getListFavorite()

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
            Glide.with(img)
                .load(item.image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        imgNotSupported.visibility = View.VISIBLE
                        progressGallery.visibility = View.GONE
                        return false
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progressGallery.visibility = View.GONE
                        return false
                    }
                })
                .into(img)

            img.layoutParams.width = (width / 3)
            var isInFavorite = checkContains(item, listFavorite)
            imgToFavorite.setImageResource(if (isInFavorite) R.drawable.heart_full else R.drawable.heart_border)
            imgToFavorite.setOnClickListener {
                if (!isInFavorite){
                    listFavorite.add(item)
                    imgToFavorite.setImageResource(R.drawable.heart_full)
                }
                else {
                    listFavorite.remove(item)
                    imgToFavorite.setImageResource(R.drawable.heart_border)
                }
                isInFavorite = !isInFavorite
                App.dm.setListFavorite(Gson().toJson(listFavorite))
            }
            img.setOnLongClickListener {
                longClick.onLongClick(item)
                true
            }
        }
    }

    override fun getItemCount(): Int = list.size

    class GalleryViewHolder(val binding: GalleryViewBinding) : ViewHolder(binding.root)

    private fun checkContains(item: MasterWork, list: MasterWorkList) : Boolean{
        return list.contains(item)
    }
    private fun getListFavorite() : MasterWorkList{
        return App.dm.getListFavorite()
    }
    interface LongClick{
        fun onLongClick(data: MasterWork)
    }
}