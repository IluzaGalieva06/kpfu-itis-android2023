package com.example.kpfu_itis_android2023

import com.example.kpfu_itis_android2023.data.Answer
import com.example.kpfu_itis_android2023.data.Question

object QuestionsRepository {

    val allQuestions: List<Question> = listOf(
        Question(
            "Что тебя вдохновляет?", listOf(
                Answer("Семья"),
                Answer("Путешествия"),
                Answer("Искусство"),
                Answer("Книги"),
                Answer("Природа"),
                Answer("Научные открытия"),
                Answer("Спорт"),
                Answer("Волонтёрская работа"),
                Answer("Творческий процесс"),
                Answer("Друзья")
            )
        ),
        Question(
            "Какой твой любимый вид спорта?", listOf(
                Answer("Футбол"),
                Answer("Баскетбол"),
                Answer("Теннис"),
                Answer("Плавание"),
                Answer("Гимнастика"),
                Answer("Хоккей"),
                Answer("Волейбол"),
                Answer("Легкая атлетика"),
                Answer("Бокс"),
                Answer("Единоборства")
            )
        ),
        Question(
            " Какую книгу ты последний раз прочитал?", listOf(
                Answer("1984"),
                Answer("Тень горы"),
                Answer("Мастер и Маргарита"),
                Answer("Властелин колец"),
                Answer("Война и мир"),
                Answer("Гарри Поттер и философский камень"),
                Answer("Старик и море"),
                Answer("Гордость и предубеждение"),
                Answer("Маленький принц")
            )
        ),
        Question(
            "Какое место на Земле хотел бы посетить?", listOf(
                Answer("Мачу-Пикчу"),
                Answer("Сидней"),
                Answer("Венеция"),
                Answer("Мальдивы"),
                Answer("Париж"),
                Answer("Рио-де-Жанейро"),
                Answer("Токио"),
                Answer("Нью-Йорк"),
                Answer("Москва"),
                Answer("Каппадокия")
            )
        ),
        Question(
            "Какой твой любимый вид музыки?", listOf(
                Answer("Поп"),
                Answer("Рок"),
                Answer("Джаз"),
                Answer("Классика"),
                Answer("Хип-хоп"),
                Answer("Электронная музыка"),
                Answer("Кантри"),
                Answer("Регги"),
                Answer("Блюз")
            )
        ),
        Question(
            "Какое твоё любимое время года?", listOf(
                Answer("Весна"),
                Answer("Лето"),
                Answer("Осень"),
                Answer("Зима"),
                Answer("Все")
            )
        ),
        Question(
            "Какой твой любимый фильм?", listOf(
                Answer("Титаник"),
                Answer("Зеленая миля"),
                Answer("Форрест Гамп"),
                Answer("Звездные войны"),
                Answer("Властелин колец"),
                Answer("Хороший, плохой, злой"),
                Answer("Леон"),
                Answer("Интерстеллар"),
                Answer("Гарри Поттер"),
                Answer("Назад в будущее")
            )
        ),
        Question(
            "Когда ходили к психологу?", listOf(
                Answer("На прошлой неделе"),
                Answer("Месяц назад"),
                Answer("Больше месяца назад"),
                Answer("Никогда не хожу"),
                Answer("Недавно")
            )
        ),
        Question(
            "Какое твое любимое место для отдыха?", listOf(
                Answer("Горы"),
                Answer("Морской пляж"),
                Answer("Озеро"),
                Answer("Лес"),
                Answer("Тропический остров"),
                Answer("Городской парк"),
                Answer("Спа-курорт"),
                Answer("Санаторий"),
                Answer("Коттедж"),

                )
        ),
        Question(
            "Какой твой любимый вид животных?", listOf(
                Answer("Собаки"),
                Answer("Кошки"),
                Answer("Львы"),
                Answer("Тигры"),
                Answer("Панды"),
                Answer("Волки"),
                Answer("Дельфины"),
                Answer("Олени"),
                Answer("Жирафы"),
                Answer("Пингвины")
            )
        ),
        Question(
            "Что тебе нравится делать в свободное время??", listOf(
                Answer("Читать книги"),
                Answer("Смотреть фильмы"),
                Answer("Заниматься спортом"),
                Answer("Играть на музыкальных инструментах"),
                Answer("Гулять"),
                Answer("Готовить"),
                Answer("Рисовать"),
                Answer("Путешествовать"),
                Answer("Встречаться с друзьями")

            )
        ),
        Question(
            "Какое твое любимое блюдо?", listOf(
                Answer("Пицца"),
                Answer("Суши"),
                Answer("Спагетти"),
                Answer("Стейк"),
                Answer("Борщ"),
                Answer("Роллы")
            )
        )
    )

    fun getQuestionByPosition(position: Int): Question {
        return if (position in allQuestions.indices) {
            allQuestions[position]
        } else {
            throw Exception()
        }
    }
}
