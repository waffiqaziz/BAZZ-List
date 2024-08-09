package com.waffiq.bazz_list.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.style.*
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatEditText
import android.text.SpannableStringBuilder
import android.text.Spanned
import com.waffiq.bazz_list.R

class CustomNoteEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

  init {
    setupListeners()
  }

  private fun setupListeners() {
    this.setOnFocusChangeListener { _, hasFocus ->
      if (hasFocus) {
        // Show the custom toolbar
        // You can use a callback or a listener to communicate with your activity/fragment
      } else {
        // Hide the custom toolbar
      }
    }

    this.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
      override fun afterTextChanged(s: Editable?) {}
    })
  }

  fun addBullet() {
    val bulletSpan = BulletSpan(20)
    val spannable = SpannableStringBuilder()
    spannable.append("\n• ").setSpan(bulletSpan, 0, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    insertSpannable(spannable)
  }

  fun addCheckbox() {
    val checkbox = "[ ] "
    insertText(checkbox)
  }

  fun toggleBold() {
    toggleSpan(StyleSpan(android.graphics.Typeface.BOLD))
  }

  fun toggleItalic() {
    toggleSpan(StyleSpan(android.graphics.Typeface.ITALIC))
  }

  fun toggleUnderline() {
    toggleSpan(UnderlineSpan())
  }

  fun toggleHighlighter() {
    val colorSpan = BackgroundColorSpan(ContextCompat.getColor(context, R.color.teal_700))
    toggleSpan(colorSpan)
  }

  private fun toggleSpan(span: Any) {
    val start = selectionStart
    val end = selectionEnd
    if (start < end) {
      val spannable = text as SpannableStringBuilder
      val spans = spannable.getSpans(start, end, span::class.java)
      if (spans.isNotEmpty()) {
        spannable.removeSpan(spans[0])
      } else {
        spannable.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
      }
    }
  }

  private fun insertText(text: String) {
    val start = selectionStart
    val end = selectionEnd
    this.text?.replace(start.coerceAtMost(end), start.coerceAtLeast(end), text, 0, text.length)
  }

  private fun insertSpannable(spannable: SpannableStringBuilder) {
    val start = selectionStart
    val end = selectionEnd
    this.text?.replace(Math.min(start, end), Math.max(start, end), spannable)
  }
}
