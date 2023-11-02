package com.example.kpfu_itis_android2023.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.adapter.NewsAdapter
import com.example.kpfu_itis_android2023.databinding.FragmentNewsBinding
import com.example.kpfu_itis_android2023.model.NewsDataModel
import com.example.kpfu_itis_android2023.util.NewsDataRepository


class NewsFragment : Fragment(R.layout.fragment_news), NewsAdapter.NewsItemClickListener {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private var newsList = NewsDataRepository().allQuestions
    private var newsAdapter: NewsAdapter? = null

    override fun onNewsItemClicked(news: NewsDataModel) {

        val selectedItem = news.newsTitle


        val infoFragment = InfoFragment.newInstance(selectedItem)


        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_activity_container, infoFragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView: RecyclerView = binding.rv


        val inputNumber = arguments?.getInt(INPUT_NUMBER_KEY) ?: 0

        val filteredList = newsList.take(inputNumber)
        newsAdapter = NewsAdapter(this, fragmentManager = parentFragmentManager)
        newsAdapter?.setItems(filteredList)


        if (inputNumber <= 12) {
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = newsAdapter


        } else {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerView.adapter = newsAdapter
        }

    }


    companion object {

        private const val INPUT_NUMBER_KEY = "input_number"

        fun newInstance(inputNumber: Int): NewsFragment {
            val fragment = NewsFragment()
            val args = Bundle()
            args.putInt(INPUT_NUMBER_KEY, inputNumber)
            fragment.arguments = args
            return fragment
        }
    }
}