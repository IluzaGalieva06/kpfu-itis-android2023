package com.example.kpfu_itis_android2023.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.kpfu_itis_android2023.QuestionsRepository
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.adapter.AnswerAdapter
import com.example.kpfu_itis_android2023.databinding.FragmentQuestionBinding
import com.google.android.material.snackbar.Snackbar

class QuestionFragment(private val questionCount: Int) : Fragment() {
    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private var questions = QuestionsRepository.allQuestions

    private lateinit var questionRepository: QuestionsRepository
    private lateinit var answerAdapter: AnswerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionRepository = QuestionsRepository
        val position = arguments?.getInt(POSITION_KEY, 0) ?: 0
        val question = questionRepository.getQuestionByPosition(position)

        with(binding) {

            tvNumber.text = (position + 1).toString()
            tvQuestion.text = question.question

            answerAdapter = AnswerAdapter(
                list = question.answers,
                onItemChecked = { position ->
                    updateChecked(position)
                    updateButton(btnFinish, questionCount)
                },
                onRootClicked = { position ->
                    updateChecked(position)
                    updateButton(btnFinish, questionCount)
                }
            )

            rvAnswers.adapter = answerAdapter
            if (savedInstanceState != null) {
                val selectedAnswerIndex = savedInstanceState.getInt(SELECTED_ANSWER_INDEX_KEY, -1)
                if (selectedAnswerIndex != -1) {
                    updateChecked(selectedAnswerIndex)
                }
            }

            if (isAllChecked(questionCount) && position >= questionCount - 1) {
                btnFinish.visibility = View.VISIBLE
            }
            btnFinish.setOnClickListener {
                btnFinish.visibility = View.GONE
                val parentFragment = parentFragment
                parentFragment?.let {
                    val fragmentManager = it.parentFragmentManager
                    fragmentManager?.beginTransaction()
                        ?.replace(
                            R.id.main_fragment_container,
                            StartFragment.getInstance(),
                            StartFragment.START_FRAGMENT_TAG
                        )
                        ?.commit()
                }
                Snackbar.make(view, getString(R.string.end), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val selectedAnswerIndex = answerAdapter.list.indexOfFirst { it.isSelected }
        if (selectedAnswerIndex != -1) {
            outState.putInt(SELECTED_ANSWER_INDEX_KEY, selectedAnswerIndex)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateChecked(position: Int) {
        answerAdapter?.list?.let {
            it.forEach { answer ->
                answer.isSelected = false
            }
            it[position].isSelected = true
        }
        answerAdapter.notifyDataSetChanged()
    }

    private fun isAllChecked(totalQuestionCount: Int): Boolean {
        var allQuestionsChecked = true
        for (i in 0 until totalQuestionCount) {
            val currentQuestion = questions[i]
            var isAnyAnswerChecked = false
            currentQuestion.answers.forEach { answer ->
                if (answer.isSelected) {
                    isAnyAnswerChecked = true
                    return@forEach
                }
            }
            if (!isAnyAnswerChecked) {
                allQuestionsChecked = false
            }
        }
        return allQuestionsChecked
    }

    private fun updateButton(button: Button, cnt: Int) {
        if (isAllChecked(cnt)) {
            button.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val POSITION_KEY = "position"
        private const val SELECTED_ANSWER_INDEX_KEY = "selected_answer_index"
        private const val QUESTION_COUNT_KEY = "question_count"

        fun newInstance(position: Int, questionCount: Int): QuestionFragment {
            val fragment = QuestionFragment(questionCount)
            val args = Bundle()
            args.putInt(POSITION_KEY, position)
            args.putInt(QUESTION_COUNT_KEY, questionCount)
            fragment.arguments = args
            return fragment
        }
    }
}