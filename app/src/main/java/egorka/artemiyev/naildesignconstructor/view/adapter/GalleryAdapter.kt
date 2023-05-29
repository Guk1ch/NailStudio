package egorka.artemiyev.naildesignconstructor.view.adapter

import android.content.Context
import android.graphics.Point
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.app.App
import egorka.artemiyev.naildesignconstructor.databinding.GalleryViewBinding
import egorka.artemiyev.naildesignconstructor.model.FireImageModel
import egorka.artemiyev.naildesignconstructor.model.utils.Case
import egorka.artemiyev.naildesignconstructor.view.activities.MainActivity
import java.lang.reflect.Type


class GalleryAdapter(val context: Context, private val list: MutableList<FireImageModel>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
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
            if (Case.idListGallery == 2) imgToFavorite.visibility = View.GONE
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
        }
    }

    override fun getItemCount(): Int = list.size

    class GalleryViewHolder(val binding: GalleryViewBinding) : ViewHolder(binding.root)

    private fun checkContains(item: FireImageModel, list: MutableList<FireImageModel>) : Boolean{
        return list.contains(item)
    }
    private fun getListFavorite() : MutableList<FireImageModel>{
        var emptyFavorite = mutableListOf<FireImageModel>()
        val serializedObject: String = App.dm.getListFavorite()
        val type = object : TypeToken<List<FireImageModel?>?>() {}.type
        emptyFavorite = Gson().fromJson(serializedObject, type)

        return emptyFavorite
    }
}