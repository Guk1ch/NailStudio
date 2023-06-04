package egorka.artemiyev.naildesignconstructor.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.databinding.MyListItemBinding
import egorka.artemiyev.naildesignconstructor.model.MyDesign

class MyListAdapter(
    private val context: Context,
    private val list: MutableList<MyDesign>,
    private val onLongClickDelete: LongClickDelete
) :
    RecyclerView.Adapter<MyListAdapter.MyListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {
        return MyListViewHolder(
            MyListItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        Glide.with(holder.binding.imagePicture)
            .load(list[position].image)
            .into(holder.binding.imagePicture)
        Glide.with(holder.binding.imgNail)
            .load(list[position].form)
            .into(holder.binding.imgNail)
        holder.binding.imgNail.setColorFilter(list[position].tint)

        holder.binding.root.setOnLongClickListener {
            MaterialDialog(context).apply {
                title(text = context.getString(R.string.do_you_want_to_delete_design))
                message(text = context.getString(R.string.design_will_be_deleted_forever))
                positiveButton(text = context.getString(R.string.ok)) {
                    onLongClickDelete.click(list[position])
                }
            }.show {  }

            true
        }
    }

    override fun getItemCount(): Int = list.size

    interface LongClickDelete {
        fun click(data: MyDesign)
    }

    class MyListViewHolder(val binding: MyListItemBinding) : ViewHolder(binding.root)
}