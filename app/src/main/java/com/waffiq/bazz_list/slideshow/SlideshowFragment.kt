package com.waffiq.bazz_list.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_list.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

  private var _binding: FragmentSlideshowBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val slideshowViewModel =
      ViewModelProvider(this)[SlideshowViewModel::class.java]

    _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textSlideshow
    slideshowViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}