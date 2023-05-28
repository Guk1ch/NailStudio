package egorka.artemiyev.naildesignconstructor.view.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Point
import android.view.Display
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import egorka.artemiyev.naildesignconstructor.databinding.ColorViewBinding
import egorka.artemiyev.naildesignconstructor.view.activities.MainActivity

class ColorAdapter(val context: Context, private val list: List<String>) : RecyclerView.Adapter<ColorAdapter.ColorHolder>() {
    private lateinit var display: Display

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        return ColorHolder(ColorViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        val card = holder.binding.root
        display = (context as MainActivity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        card.layoutParams.width = (width / 6)
        card.layoutParams.height = (width / 6)
        card.setCardBackgroundColor(Color.parseColor(list[position]))

        card.setOnClickListener { Toast.makeText(context, list[position], Toast.LENGTH_SHORT).show() }
    }

    override fun getItemCount(): Int = 18

    class ColorHolder(val binding: ColorViewBinding) : ViewHolder(binding.root)
}