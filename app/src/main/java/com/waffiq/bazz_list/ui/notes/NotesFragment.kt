package com.waffiq.bazz_list.ui.notes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.waffiq.bazz_list.R
import com.waffiq.bazz_list.databinding.FragmentNotesBinding
import com.waffiq.bazz_list.domain.model.Note
import com.waffiq.bazz_list.ui.adapter.NotesAdapter
import com.waffiq.bazz_list.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_list.utils.helper.DbResult
import com.waffiq.bazz_list.utils.helper.FabController
import com.waffiq.bazz_list.utils.helper.OnFabClickListener

class NotesFragment : Fragment(), OnFabClickListener {

  private var _binding: FragmentNotesBinding? = null
  private val binding get() = _binding!!

  private lateinit var notesViewModel: NotesViewModel
  private lateinit var notesAdapter: NotesAdapter

  private val selectedItems = mutableSetOf<Note>()
  private var isMultiSelect = false
  private var deleteMenuItem: MenuItem? = null
  private var cancelMenuItem: MenuItem? = null

  private var fabController: FabController? = null

  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is FabController) {
      fabController = context
    } else {
      throw RuntimeException("$context must implement FabController")
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentNotesBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val factory = ViewModelFactory.getInstance(requireContext())
    notesViewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]

    setupOptionMenu()
    showNotes()
    swipeRefresh()
    return root
  }

  private fun setupOptionMenu() {
    // Enable options menu in fragment
    requireActivity().addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.main, menu)
        deleteMenuItem = menu.findItem(R.id.action_delete)
        cancelMenuItem = menu.findItem(R.id.action_cancel)
        updateMenuItemsVisibility()
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
          R.id.action_delete -> {
            deleteSelectedItems()
            true
          }

          R.id.action_cancel -> {
            cancelSelectionMode()
            true
          }

          else -> false
        }
      }
    }, viewLifecycleOwner)
  }

  private fun getNotes() {
    notesViewModel.notes.observe(viewLifecycleOwner) {
      notesAdapter.setNote(it)
    }
  }

  private fun swipeRefresh() {
    binding.swipeRefresh.setOnRefreshListener {
      getNotes()
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun showNotes() {
    // setup recyclerview and adapter
    notesAdapter = NotesAdapter(
      ::onItemLongClick,
      ::onItemSelected,
      ::onNoteClick
    )
    val staggeredGridLayoutManager =
      StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    binding.rvNotes.layoutManager = staggeredGridLayoutManager
    binding.rvNotes.adapter = notesAdapter
    getNotes()

    // Observe any function
    notesViewModel.dbResult.observe(viewLifecycleOwner) {
      it.getContentIfNotHandled().let { dbResult ->
        when (dbResult) {
          is DbResult.Success -> showToast(
            dbResult.message ?: getString(R.string.operation_successful)
          )

          is DbResult.Error -> showToast(dbResult.errorMessage)
          else -> {}
        }
      }
    }
  }

  private fun onItemLongClick(note: Note) {
    isMultiSelect = true
    onItemSelected(note, true)
    updateMenuItemsVisibility()
  }

  private fun onItemSelected(note: Note, isSelected: Boolean) {
    if (isSelected) selectedItems.add(note)
    else selectedItems.remove(note)

    updateMenuItemsVisibility()
  }

  // open detail activity
  private fun onNoteClick(note: Note) {
    val intent = Intent(requireContext(), DetailNoteActivity::class.java)
    intent.putExtra(DetailNoteActivity.EXTRA_NOTE, note)
    startActivity(intent)
  }

  private fun deleteSelectedItems() {
    val notesToDelete = selectedItems.toList()
    notesViewModel.deleteMultipleNotes(notesToDelete)
    notesAdapter.deleteSelectedItems()
    selectedItems.clear()
    isMultiSelect = false
    updateMenuItemsVisibility()
  }

  private fun cancelSelectionMode() {
    selectedItems.clear()
    isMultiSelect = false
    notesAdapter.clearSelection()
    updateMenuItemsVisibility()
  }

  // show or hide cancel, delete action and FAB
  private fun updateMenuItemsVisibility() {
    deleteMenuItem?.isVisible = isMultiSelect
    cancelMenuItem?.isVisible = isMultiSelect
    deleteMenuItem?.isVisible = selectedItems.isNotEmpty()

    if (isMultiSelect) fabController?.hideFab()
    else fabController?.showFab()
  }

  private fun showToast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
  }

  // add note
  override fun onFabClick() {
    startActivity(
      Intent(requireContext(), DetailNoteActivity::class.java).putExtra(
        DetailNoteActivity.EXTRA_NOTE, Note(0, "", "", 0L, false)
      )
    )
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}