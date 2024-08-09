package com.waffiq.bazz_list.ui.notes

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

  // flag helper
  private var isUpdate = false
  private var isBulletMode = false

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
    showData()
    getTotalCharacters()
    editTextListener()
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
      binding.etDescription.setText(dataExtra.description)
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
    binding.tvTotalCharacters.text = getString(
      if (getTotalChar(binding.etDescription.text) == 1) R.string.character
      else R.string.characters,
      getTotalChar(binding.etDescription.text).toString()
    )

    // get total character when before typing
    binding.etDescription.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, star: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, star: Int, count: Int, after: Int) {
        binding.tvTotalCharacters.text = getString(
          if (getTotalChar(s) == 1) R.string.character
          else R.string.characters,
          getTotalChar(s).toString()
        )
      }

      override fun afterTextChanged(p0: Editable?) {}
    })
  }

  private fun getTotalChar(s: CharSequence?): Int? {
    return s?.toString()?.trim()?.replace(" ", "")?.replace("\n", "")?.length
  }

  private fun editTextListener() {
    binding.btnBullet.setOnClickListener {
      isBulletMode = !isBulletMode
      binding.btnBullet.isActivated = !binding.btnBullet.isActivated

      if (binding.btnBullet.isActivated) {
        binding.btnBullet.background =
          ContextCompat.getDrawable(this, R.drawable.button_background_pressed)
      } else {
        binding.btnBullet.background =
          ContextCompat.getDrawable(this, R.drawable.button_background_pressed)
      }
    }

    binding.etDescription.setOnFocusChangeListener { _, hasFocus ->
      if (hasFocus) binding.customToolbar.visibility = View.VISIBLE
      else binding.customToolbar.visibility = View.GONE
    }

    // Add bullet when bullet button is clicked
    binding.etDescription.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

      override fun afterTextChanged(editable: Editable?) {
        if (isBulletMode) {
          val selectionStart = binding.etDescription.selectionStart
          val selectionEnd = binding.etDescription.selectionEnd

          if (selectionStart == selectionEnd) {
            // Check if the user pressed Enter key
            if (editable?.endsWith("\n") == true) {
              // new line character and add a bullet
              editable.replace(selectionStart - 1, selectionEnd, "\n ")
              addBulletToDescription()
            }
          }
        }
      }
    })
  }

  private fun addBulletToDescription() {
    val bullet = "\u2022 "  // Unicode for bullet
    val start = binding.etDescription.selectionStart
    val end = binding.etDescription.selectionEnd

    if (start == end) {
      // Insert bullet at the current cursor position
      binding.etDescription.text.insert(start, "$bullet ")
    } else {
      // Replace the selected text with bullet
      binding.etDescription.text
        .replace(start, end, "$bullet ${binding.etDescription.text.substring(start, end)}")
    }

    // Move cursor to the end of the inserted bullet
    binding.etDescription.setSelection(start + bullet.length + 1)
  }

  private fun insertNote() {
    if ((binding.etTitle.text.isNotEmpty() || binding.etDescription.text.isNotEmpty())
      && (binding.etTitle.text.isNotBlank() || binding.etDescription.text.isNotBlank())
      && !isUpdate
    ) {
      val note = Note(
        id = 0,
        title = binding.etTitle.text.toString(),
        description = binding.etDescription.text.toString(),
        dateModified = System.currentTimeMillis(),
        hide = false,
      )
      detailNoteViewModel.insertNote(note)
    } else if (isUpdate) {
      val note = Note(
        id = dataExtra.id,
        title = binding.etTitle.text.toString(),
        description = binding.etDescription.text.toString(),
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