package com.rumman.securefilenotes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.rumman.securefilenotes.BR
import com.rumman.securefilenotes.data.models.FileModel
import com.rumman.securefilenotes.databinding.RowItemsBinding

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>(),Filterable {

    private var allNotes : ArrayList<FileModel> = ArrayList()
    private var allNotesFiltered : ArrayList<FileModel> = ArrayList()

    class ViewHolder(private var binding: RowItemsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindObject(data : FileModel){
            binding.setVariable(BR.fileModel,data)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding : RowItemsBinding = RowItemsBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindObject(allNotesFiltered[position])
    }

    override fun getItemCount(): Int {
        return allNotesFiltered.size
    }

    fun setData(data : ArrayList<FileModel>){
        allNotes = data
        allNotesFiltered = allNotes
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) allNotesFiltered = allNotes else {
                    val filteredList = ArrayList<FileModel>()
                    allNotes
                        .filter {
                            (it.title.contains(constraint!!)) or
                                    (it.notes.contains(constraint))

                        }
                        .forEach { filteredList.add(it) }
                    allNotesFiltered = filteredList

                }
                return FilterResults().apply { values = allNotesFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                allNotesFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<FileModel>
                notifyDataSetChanged()
            }
        }
    }
}