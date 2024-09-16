// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.waffiq.bazz_list.detailnotes

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.waffiq.bazz_list.MyApplication
import com.waffiq.bazz_list.R
import com.waffiq.bazz_list.core.domain.model.Note
import com.waffiq.bazz_list.core.ui.ViewModelFactory
import com.waffiq.bazz_list.core.utils.helper.DbResult
import com.waffiq.bazz_list.core.utils.helper.Helpers.dateNow
import com.waffiq.bazz_list.core.utils.helper.Helpers.formatTimestamp
import com.waffiq.bazz_list.core.utils.helper.Helpers.setupStatusBar
import com.waffiq.bazz_list.databinding.ActivityDetailNoteBinding
import org.wordpress.android.util.ToastUtils
import org.wordpress.aztec.Aztec
import org.wordpress.aztec.AztecExceptionHandler
import org.wordpress.aztec.AztecText
import org.wordpress.aztec.IHistoryListener
import org.wordpress.aztec.ITextFormat
import org.wordpress.aztec.plugins.BackgroundColorButton
import org.wordpress.aztec.plugins.CssBackgroundColorPlugin
import org.wordpress.aztec.plugins.CssUnderlinePlugin
import org.wordpress.aztec.toolbar.IAztecToolbarClickListener
import org.wordpress.aztec.util.AztecLog
import org.xml.sax.Attributes
import javax.inject.Inject

class DetailNoteActivity : AppCompatActivity(),
  IAztecToolbarClickListener,
  IHistoryListener,
  View.OnTouchListener {

  private lateinit var binding: ActivityDetailNoteBinding

  @Inject
  lateinit var factory: ViewModelFactory

  private val detailNoteViewModel: DetailNoteViewModel by viewModels { factory }

  private lateinit var dataExtra: Note

  private lateinit var aztec: Aztec
  private lateinit var invalidateOptionsHandler: Handler
  private lateinit var invalidateOptionsRunnable: Runnable

  // flag helper
  private var isUpdate = false

  private var mIsKeyboardOpen = false
  private var mHideActionBarOnSoftKeyboardUp = false

  override fun onCreate(savedInstanceState: Bundle?) {
    (application as MyApplication).appComponent.inject(this)
    super.onCreate(savedInstanceState)
    binding = ActivityDetailNoteBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupStatusBar()
    setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)

    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        mIsKeyboardOpen = false
        showActionBarIfNeeded()

        // Disable the callback temporarily to allow the system to handle the back pressed event. This usage
        // breaks predictive back gesture behavior and should be reviewed before enabling the predictive back
        // gesture feature.
        isEnabled = false
        onBackPressedDispatcher.onBackPressed()
        isEnabled = true
      }
    })

    // Setup hiding the action bar when the soft keyboard is displayed for narrow viewports
    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
      && !resources.getBoolean(R.bool.is_large_tablet_landscape)
    ) {
      mHideActionBarOnSoftKeyboardUp = true
    }

    // set navigation back button as insert
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

    setupAztec()
    getDataExtra()
    showData()
    getTotalCharacters()
  }

  // region ACTION BAR
  private fun hideActionBarIfNeeded() {
    if (supportActionBar != null
      && !isHardwareKeyboardPresent()
      && mHideActionBarOnSoftKeyboardUp
      && mIsKeyboardOpen
      && supportActionBar?.isShowing == true
    ) {
      supportActionBar?.hide()
    }
  }

  private fun isHardwareKeyboardPresent(): Boolean {
    val config = resources.configuration
    var returnValue = false
    if (config.keyboard != Configuration.KEYBOARD_NOKEYS) {
      returnValue = true
    }
    return returnValue
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.detail_menu, menu)
    return super.onCreateOptionsMenu(menu)
  }

  private fun showActionBarIfNeeded() {
    if (actionBar != null && supportActionBar?.isShowing == false) {
      supportActionBar?.show()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.undo ->
        if (aztec.visualEditor.visibility == View.VISIBLE) {
          aztec.visualEditor.undo()
        } else {
          aztec.sourceEditor?.undo()
        }

      R.id.redo ->
        if (aztec.visualEditor.visibility == View.VISIBLE) {
          aztec.visualEditor.redo()
        } else {
          aztec.sourceEditor?.redo()
        }

      // back button on action bar as insert
      android.R.id.home -> {
        insertNote()
        finish()
      }

      else -> super.onOptionsItemSelected(item)
    }
    return true
  }

  override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
    menu?.findItem(R.id.redo)?.isEnabled = aztec.visualEditor.history.redoValid()
    menu?.findItem(R.id.undo)?.isEnabled = aztec.visualEditor.history.undoValid()
    return super.onPrepareOptionsMenu(menu)
  }
  // endregion ACTION BAR

  private fun setupAztec() {
    aztec = Aztec.with(binding.etDescription, binding.source, binding.formattingToolbar, this)
      .setOnTouchListener(this)
      .setHistoryListener(this)

    binding.etDescription.enableSamsungPredictiveBehaviorOverride()
    binding.etDescription.externalLogger = object : AztecLog.ExternalLogger {
      override fun log(message: String) {}
      override fun logException(tr: Throwable) {}
      override fun logException(tr: Throwable, message: String) {}
    }

    aztec.visualEditor.enableCrashLogging(object : AztecExceptionHandler.ExceptionHandlerHelper {
      override fun shouldLog(ex: Throwable): Boolean {
        return true
      }
    })
    aztec.visualEditor.setCalypsoMode(false)
    aztec.sourceEditor?.setCalypsoMode(false)

    aztec.visualEditor.setBackgroundSpanColor(
      ContextCompat.getColor(
        this,
        org.wordpress.aztec.R.color.blue_dark
      )
    )
    aztec.addPlugin(CssUnderlinePlugin())
    aztec.addPlugin(CssBackgroundColorPlugin())
    aztec.addPlugin(BackgroundColorButton(binding.etDescription))

    invalidateOptionsHandler = Handler(Looper.getMainLooper())
    invalidateOptionsRunnable = Runnable { invalidateOptionsMenu() }
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
      binding.tvDateNow.text = dateNow()
    } else {
      isUpdate = true
      binding.tvDateNow.text = formatTimestamp(dataExtra.dateModified)
      aztec.visualEditor.fromHtml(dataExtra.description)
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

  private fun getTotalCharacters() {
    binding.etDescription.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // No action needed before text is changed
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val totalChar = getTotalChar(s)
        binding.tvTotalCharacters.text = getString(
          if (totalChar == 1) R.string.character
          else R.string.characters,
          totalChar.toString()
        )
      }

      override fun afterTextChanged(s: Editable?) {
        // No action needed after text is changed
      }
    })
  }

  private fun getTotalChar(s: CharSequence?): Int {
    return s?.toString()?.trim()?.replace(" ", "")?.replace("\n", "")?.length ?: 0
  }

  private fun insertNote() {
    // insert when title or description is filled
    if ((binding.etTitle.text.isNotEmpty() || binding.etDescription.text.isNotEmpty())
      && (binding.etTitle.text.isNotBlank() || binding.etDescription.text.isNotBlank())
      && !isUpdate
    ) {
      // add note
      val note = Note(
        id = 0,
        title = binding.etTitle.text.toString(),
        description = binding.etDescription.toFormattedHtml(),
        dateModified = System.currentTimeMillis(),
        hide = false,
      )
      detailNoteViewModel.insertNote(note)
    } else if (isUpdate) {
      // update
      val note = Note(
        id = dataExtra.id,
        title = binding.etTitle.text.toString(),
        description = binding.etDescription.toFormattedHtml(),
        dateModified = System.currentTimeMillis(),
        hide = false,
      )
      detailNoteViewModel.updateNote(note)
    }
  }

  private fun showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
  }

  // region UNDO REDO
  override fun onUndoEnabled() {
    invalidateOptionsHandler.removeCallbacks(invalidateOptionsRunnable)
    invalidateOptionsHandler.postDelayed(
      invalidateOptionsRunnable,
      resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
    )
  }

  override fun onRedoEnabled() {
    invalidateOptionsHandler.removeCallbacks(invalidateOptionsRunnable)
    invalidateOptionsHandler.postDelayed(
      invalidateOptionsRunnable,
      resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
    )
  }

  override fun onUndo() {
  }

  override fun onRedo() {}
  // endregion UNDO REDO

  override fun onToolbarCollapseButtonClicked() {}

  override fun onToolbarExpandButtonClicked() {}

  override fun onToolbarFormatButtonClicked(format: ITextFormat, isKeyboardShortcut: Boolean) {
//    ToastUtils.showToast(this, format.toString())
  }

  override fun onToolbarHeadingButtonClicked() {}

  override fun onToolbarHtmlButtonClicked() {
    val uploadingPredicate = object : AztecText.AttributePredicate {
      override fun matches(attrs: Attributes): Boolean {
        return attrs.getIndex("uploading") > -1
      }
    }

    val mediaPending = aztec.visualEditor.getAllElementAttributes(uploadingPredicate).isNotEmpty()

    if (mediaPending) {
      ToastUtils.showToast(this, org.wordpress.aztec.R.string.media_upload_dialog_message)
    } else {
      aztec.toolbar.toggleEditorMode()
    }
  }

  override fun onToolbarListButtonClicked() {}

  override fun onToolbarMediaButtonClicked(): Boolean {
    return false
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouch(view: View, event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_UP) {
      // If the WebView or EditText has received a touch event, the keyboard will be displayed and the action bar
      // should hide
      mIsKeyboardOpen = true
      hideActionBarIfNeeded()
    }
    return false
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)

    // Toggle action bar auto-hiding for the new orientation
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
      && !resources.getBoolean(R.bool.is_large_tablet_landscape)
    ) {
      mHideActionBarOnSoftKeyboardUp = true
      hideActionBarIfNeeded()
    } else {
      mHideActionBarOnSoftKeyboardUp = false
      showActionBarIfNeeded()
    }
  }

  override fun onPause() {
    super.onPause()
    mIsKeyboardOpen = false
  }

  override fun onResume() {
    super.onResume()
    showActionBarIfNeeded()
  }

  override fun onDestroy() {
    super.onDestroy()
    aztec.visualEditor.disableCrashLogging()
  }

  companion object {
    const val EXTRA_NOTE = "NOTE"
  }
}