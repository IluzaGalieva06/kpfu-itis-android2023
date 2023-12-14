package com.example.optional.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.databinding.FragmentMainBinding
import com.example.optional.adapter.ThingsAdapter
import com.example.optional.utils.ThingsRepository


class MainFragment: Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var newsAdapter: ThingsAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView: RecyclerView = binding.rv


        val inputNumber = arguments?.getInt(INPUT_NUMBER_KEY) ?: 0
        var newsList = ThingsRepository.getNewsList(inputNumber)
        newsAdapter = ThingsAdapter(newsList, inputNumber, requireContext())

        recyclerView.adapter = newsAdapter


    }


    companion object {

        private const val INPUT_NUMBER_KEY = "input_number"

        fun newInstance(inputNumber: Int): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            args.putInt(INPUT_NUMBER_KEY, inputNumber)
            fragment.arguments = args
            return fragment
        }
    }
}