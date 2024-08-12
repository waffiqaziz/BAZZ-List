package com.waffiq.bazz_list.ui.notes

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_list.R
import com.waffiq.bazz_list.databinding.ActivityDetailNoteBinding
import com.waffiq.bazz_list.domain.model.Note
import com.waffiq.bazz_list.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_list.utils.helper.DbResult
import com.waffiq.bazz_list.utils.helper.Helpers.formatTimestamp
import java.text.SimpleDateFormat
import java.util.Date


class DetailNoteActivity : AppCompatActivity() {

  private lateinit var binding: ActivityDetailNoteBinding
  private lateinit var detailNoteViewModel: DetailNoteViewModel

  private lateinit var dataExtra: Note
  private lateinit var desc: String

  // flag helper
  private var isUpdate = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailNoteBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val factory = ViewModelFactory.getInstance(this)
    detailNoteViewModel = ViewModelProvider(this, factory)[DetailNoteViewModel::class.java]

    setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)

    onBackPressedDispatcher.addCallback(
      this /* lifecycle owner */,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          insertNote()
          finish()
        }
      })


    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

    getDataExtra()
    setupRichText()
    showData()
    getTotalCharacters()
    setupToolbarRichText()
  }

  private fun getDataExtra() {
    // check if intent hasExtra
    if (intent.hasExtra(EXTRA_NOTE)) {
      dataExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(EXTRA_NOTE, Note::class.java)
          ?: error("No DataExtra")
      } else {
        @Suppress("DEPRECATION")
        intent.getParcelableExtra(EXTRA_NOTE) ?: error("No DataExtra")
      }
    } else finish()
  }

  // show data or blank data if added
  private fun showData() {
    if (dataExtra.id == 0 && dataExtra.dateModified == 0L) {
      isUpdate = false
      dateNow()
    } else {
      isUpdate = true
      binding.tvDateNow.text = formatTimestamp(dataExtra.dateModified)
      binding.etDescription.html = dataExtra.description
      binding.etTitle.setText(dataExtra.title)
    }

    // observe
    detailNoteViewModel.dbResult.observe(this) {
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

  private fun dateNow() {
    val formatter = SimpleDateFormat("EEEE, MMMM dd yyyy  |  HH:mm ", java.util.Locale.getDefault())
    val date = Date()
    binding.tvDateNow.text = formatter.format(date)
  }

  private fun getTotalCharacters() {
    // get total character before typing
    binding.etDescription.setOnTextChangeListener { _ ->
      binding.etDescription.setOnJSDataListener {
        desc = it
        binding.tvTotalCharacters.text = getString(
          if (getTotalChar(it) == 1) R.string.character
          else R.string.characters,
          getTotalChar(it).toString()
        )
      }
      binding.etDescription.html
    }
  }

  private fun getTotalChar(s: String?): Int? {
    return s?.trim()?.replace(" ", "")?.replace("\n", "")?.length
  }

  private fun setupRichText() {
    binding.etDescription.LoadFont("Nunito Sans Regular", "font/nunito_sans_regular.ttf")
  }

  private fun setupToolbarRichText() {
    binding.apply {
      etDescription.apply {
        actionUndo.setOnClickListener { undo() }
        actionRedo.setOnClickListener { redo() }
        actionBold.setOnClickListener { toggleBold() }
        actionItalic.setOnClickListener { toggleItalic() }
        actionStrikethrough.setOnClickListener { toggleStrikeThrough() }
        actionUnderline.setOnClickListener { toggleUnderline() }
        actionHeading1.setOnClickListener { setHeading(1) }
        actionHeading2.setOnClickListener { setHeading(2) }
        actionHeading3.setOnClickListener { setHeading(3) }
        actionAlignLeft.setOnClickListener { setAlignLeft() }
        actionAlignCenter.setOnClickListener { setAlignCenter() }
        actionAlignRight.setOnClickListener { setAlignRight() }
        actionInsertBullets.setOnClickListener { setBullets() }
        actionInsertNumbers.setOnClickListener { setNumbers() }
        actionInsertCheckbox.setOnClickListener { insertCheckbox() }
        actionInsertLink.setOnClickListener {
          insertLink(
            "https://github.com/wasabeef",
            "https://github.com/wasabeef",
            "wasabeef"
          )
        }
      }

//      findViewById<View>(R.id.action_subscript).setOnClickListener { mEditor.setSubscript() }
//      findViewById<View>(R.id.action_superscript).setOnClickListener { mEditor.setSuperscript() }
    }
  }

  private fun insertNote() {
    // insert when title or description is filled
    if ((binding.etTitle.text.isNotEmpty() || binding.etTitle.text.isNotBlank())
      && (binding.etDescription.html?.isNotEmpty() == true || binding.etDescription.html?.isNotBlank() == true)
      && !isUpdate
    ) {
      // add note
      val note = Note(
        id = 0,
        title = binding.etTitle.text.toString(),
        description = desc,
        dateModified = System.currentTimeMillis(),
        hide = false,
      )
      detailNoteViewModel.insertNote(note)
    } else if (isUpdate) {
      // update
      val note = Note(
        id = dataExtra.id,
        title = binding.etTitle.text.toString(),
        description = desc,
        dateModified = System.currentTimeMillis(),
        hide = false,
      )
      detailNoteViewModel.updateNote(note)
    }
  }

  private fun showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
  }

  // implement back button
  override fun onSupportNavigateUp(): Boolean {
    insertNote()
    finish()
    return true
  }

  companion object {
    const val EXTRA_NOTE = "NOTE"
  }
}