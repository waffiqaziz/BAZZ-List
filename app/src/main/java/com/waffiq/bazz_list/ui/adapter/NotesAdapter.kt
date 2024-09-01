package com.waffiq.bazz_list.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_list.R
import com.waffiq.bazz_list.databinding.ItemGridBinding
import com.waffiq.bazz_list.domain.model.Note
import com.waffiq.bazz_list.utils.helper.Helpers.formatTimestampCard

class NotesAdapter(
  private val onItemLongClick: (Note) -> Unit, // callback for long press
  private val onItemSelected: (Note, Boolean) -> Unit,
  private val onNoteClick: (Note) -> Unit // callback for normal click handling
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
      val context = binding.root.context
      data = note

      binding.tvDescription.text =
        HtmlCompat.fromHtml(note.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
          .ifEmpty { ContextCompat.getString(context, R.string.no_description) }
      binding.tvTitle.text =
        note.title.ifEmpty { ContextCompat.getString(context, R.string.no_title) }
      binding.tvDate.text = formatTimestampCard(note.dateModified)


      // handle change background color
      binding.container.setCardBackgroundColor(
        if (selectedItems.contains(note)) ContextCompat.getColor(context, R.color.gray_100)
        else Color.WHITE
      )

      // open detail page or selected item
      binding.container.setOnClickListener {
        if (isSelectionMode) toggleSelection(note)
        else onNoteClick(note) // Call the normal click callback
      }

      binding.container.setOnLongClickListener {
        if (!isSelectionMode) {
          isSelectionMode = true
          toggleSelection(note)
          onItemLongClick(note)
        }
        true
      }
    }

    private fun toggleSelection(note: Note) {
      if (selectedItems.contains(note)) {
        selectedItems.remove(note)
        onItemSelected(note, false)
      } else {
        selectedItems.add(note)
        onItemSelected(note, true)
      }
      notifyItemChanged(adapterPosition)
    }
  }

  fun clearSelection() {
    val previousSelectedItems = selectedItems.toList()
    selectedItems.clear()
    isSelectionMode = false

    // update item based on index
    previousSelectedItems.forEach { note ->
      val index = listNote.indexOf(note)
      if (index != -1) {
        notifyItemChanged(index)
      }
    }
  }

  fun deleteSelectedItems() {
    val itemsToDelete = selectedItems.toList()
    selectedItems.clear()
    isSelectionMode = false

    itemsToDelete.forEach { note ->
      val index = listNote.indexOf(note)
      if (index != -1) {
        listNote.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(
          index,
          listNote.size
        ) // Notify range change to handle the shifting items
      }
    }
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
      oldList[oldItemPosition].id == newList[newItemPosition].id
        && oldList[oldItemPosition].description == newList[newItemPosition].description
        && oldList[oldItemPosition].title == newList[newItemPosition].title
  }
}
