package com.waffiq.bazz_list.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_list.databinding.FragmentTasksBinding
import com.waffiq.bazz_list.utils.helper.OnFabClickListener

class TasksFragment : Fragment(), OnFabClickListener {

  private var _binding: FragmentTasksBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val galleryViewModel =
      ViewModelProvider(this)[TaskViewModel::class.java]

    _binding = FragmentTasksBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.tvTask
    galleryViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onFabClick() {
    Toast.makeText(context, "FAB clicked in FragmentTask", Toast.LENGTH_SHORT).show()
  }
}