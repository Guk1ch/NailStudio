package egorka.artemiyev.naildesignconstructor.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.afollestad.materialdialogs.MaterialDialog
import egorka.artemiyev.naildesignconstructor.R
import egorka.artemiyev.naildesignconstructor.databinding.RecordItemBinding
import egorka.artemiyev.naildesignconstructor.model.RecordModel
import egorka.artemiyev.naildesignconstructor.view.activities.MainActivity

class RecordAdapter(val context: Context, val list: List<RecordModel>) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(RecordItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
//        val item = list[position]
        with(holder.binding){
            root.setOnLongClickListener {
                val dialog = MaterialDialog(context as MainActivity)
                    .title(text = context.getString(R.string.end_record))
                    .message(text = nameTxt.text.toString() + " опущен?")
                    .positiveButton(text = context.getString(R.string.yes)) {
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

    override fun getItemCount(): Int = 16

    class RecordViewHolder(val binding: RecordItemBinding) : ViewHolder(binding.root)
}