package com.waffiq.bazz_list.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_list.R
import com.waffiq.bazz_list.databinding.ItemGridBinding
import com.waffiq.bazz_list.domain.model.Note
import com.waffiq.bazz_list.ui.notes.DetailNoteActivity
import com.waffiq.bazz_list.utils.helper.Helpers.formatTimestampCard

class NotesAdapter(
  private val onItemLongClick: (Note) -> Unit,
  private val onItemSelected: (Note, Boolean) -> Unit
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
  private val listNote = ArrayList<Note>()
  private val selectedItems = mutableSetOf<Note>()
  var isSelectionMode = false
    private set

  fun setNote(itemStory: List<Note>) {
    val diffCallback = DiffCallback(this.listNote, itemStory)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listNote.clear()
    this.listNote.addAll(itemStory)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listNote[position])
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(
        holder.itemView.context,
        android.R.anim.fade_in
      )
    )
  }

  override fun getItemCount() = listNote.size

  inner class ViewHolder(private var binding: ItemGridBinding) :
    RecyclerView.ViewHolder(binding.root) {
    lateinit var data: Note

    fun bind(note: Note) {
      data = note

//      if(note.title.isNotEmpty()){
//        binding.tvDescription.text = note.description
//        binding.tvTitle.text = note.title
//      }else {
//        val newlineIndex = note.description.indexOf("\n")
//        val spannableString = SpannableString(note.description)
//
//        if (newlineIndex != -1) {
//          // Bold the first line
//          spannableString.setSpan(
//            StyleSpan(Typeface.BOLD),
//            0,
//            newlineIndex,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//          )
//        } else {
//          // Bold the entire text if there's no newline
//          spannableString.setSpan(
//            StyleSpan(Typeface.BOLD),
//            0,
//            note.description.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//          )
//        }
//
//        binding.tvTitle.text = spannableString
//      }

      binding.tvDescription.text = note.description
      binding.tvTitle.text = note.title
      binding.tvDate.text = formatTimestampCard(note.dateModified)


//      // delete multiple and single
//      val context = binding.root.context
//      binding.container.setCardBackgroundColor(
//        if (selectedItems.contains(note)) ContextCompat.getColor(context, R.color.gray_100)
//        else Color.WHITE
//      )
//
//      binding.container.setOnClickListener {
//        if (selectedItems.contains(note)) {
//          selectedItems.remove(note)
//          onItemSelected(note, false)
//        } else {
//          selectedItems.add(note)
//          onItemSelected(note, true)
//        }
//        notifyItemChanged(adapterPosition)
//      }
//
//      binding.container.setOnLongClickListener {
//        onItemLongClick(note)
//        true
//      }

      // open detail note
      binding.container.setOnClickListener {
        val intent = Intent(it.context, DetailNoteActivity::class.java)
        intent.putExtra(DetailNoteActivity.EXTRA_NOTE, note)
        it.context.startActivity(intent)
      }
    }
  }

  fun deleteSelectedItems() {
    listNote.removeAll(selectedItems)
    selectedItems.clear()
    notifyDataSetChanged()
  }

  inner class DiffCallback(
    private val oldList: List<Note>,
    private val newList: List<Note>
  ) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      (oldList[oldItemPosition].id == newList[newItemPosition].id
        && oldList[oldItemPosition].description == newList[newItemPosition].description)
  }
}