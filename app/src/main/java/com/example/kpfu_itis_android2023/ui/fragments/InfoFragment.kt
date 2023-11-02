package com.example.kpfu_itis_android2023.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.databinding.FragmentInfoBinding
import com.example.kpfu_itis_android2023.model.NewsDataModel
import com.example.kpfu_itis_android2023.util.NewsDataRepository

class InfoFragment : Fragment(R.layout.fragment_info) {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var newsTitle: String
    private lateinit var newsItem: NewsDataModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newsTitle = it.getString(ARG_NEWS_TITLE, "")
            newsItem = NewsDataRepository().getNewsByTitle(newsTitle)!!
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newsTitleTv.text = newsItem.newsTitle
        binding.newsImageIv.setImageResource(newsItem.newsImage ?: 0)
        binding.newsDescriptionTv.text = newsItem.newsDetails
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_NEWS_TITLE = "news_title"

        fun newInstance(newsTitle: String): InfoFragment {
            val fragment = InfoFragment()
            val args = Bundle()
            args.putString(ARG_NEWS_TITLE, newsTitle)
            fragment.arguments = args
            return fragment
        }
    }

}