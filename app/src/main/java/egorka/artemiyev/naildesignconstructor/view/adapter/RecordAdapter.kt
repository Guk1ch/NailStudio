package egorka.artemiyev.naildesignconstructor.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.afollestad.materialdialogs.MaterialDialog
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.databinding.RecordItemBinding
import egorka.artemiyev.naildesignconstructor.model.Record
import egorka.artemiyev.naildesignconstructor.model.RecordsList
import egorka.artemiyev.naildesignconstructor.view.activities.MainActivity

class RecordAdapter(val context: Context, val list: List<Record>, private val onClick: OnClick) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(RecordItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding){
            nameTxt.text = item.idClientNavigation!!.fullName
            dateTxt.text = item.date
            timeTxt.text = item.time
            root.setOnLongClickListener {
                val dialog = MaterialDialog(context as MainActivity)
                    .title(text = context.getString(R.string.end_record))
                    .message(text = "Запись для ${nameTxt.text} проведена?")
                    .positiveButton(text = context.getString(R.string.yes)) {
                        onClick.click(item, position)
                        it.cancel()
                    }
                    .negativeButton(text = context.getString(R.string.no)) {
                        it.cancel()
                    }
                    .show {  }
                true
            }
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnClick{
        fun click(data: Record, position: Int)
    }
    class RecordViewHolder(val binding: RecordItemBinding) : ViewHolder(binding.root)
}