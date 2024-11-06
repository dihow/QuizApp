package com.example.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz.ui.theme.QuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizTheme {
                Start()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Start() {
    val showProgram = remember { mutableStateOf(false)}
    if (!showProgram.value)
    Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Географическая викторина (10 вопросов)", fontSize = 20.sp,
            modifier = Modifier.padding(30.dp))
        Button(onClick = { showProgram.value = true },
            Modifier.padding(40.dp).shadow(20.dp, RoundedCornerShape(40.dp))) {
            Text(text = "Начать", fontSize = 30.sp)
        }
    }
    if (showProgram.value) Program()
}

@SuppressLint("SuspiciousIndentation")
@Preview(showSystemUi = true)
@Composable
fun Program() {
    val questions = remember {
        mutableListOf(
            Question("1. Столица России?",
                listOf("Москва", "Санкт-Петербург", "Новосибирск"),
                "Москва"),
            Question("2. Самый высокий пик в мире?",
                listOf("Эверест", "К2", "Эльбрус"),
                "Эверест"),
            Question("3. Самый крупный континент?",
                listOf("Африка", "Южная Америка", "Евразия"),
                "Евразия"),
            Question("4. Самый высокий водопад в мире?",
                listOf("Ниагара", "Анхель", "Игуасу"),
                "Анхель"),
            Question("5. Самое глубокое озеро в мире?",
                listOf("Каспийское море", "Танганьика", "Байкал"),
                "Байкал"),
            Question("6. Самая большая страна в мире?",
                listOf("Россия", "США", "Канада", "Китай"),
                "Россия"),
            Question("7. Самая маленькая страна в мире?",
                listOf("Сан-Марино", "Монако", "Ватикан"),
                "Ватикан"),
            Question("8. Крупнейший остров в Средиземном море?",
                listOf("Сицилия", "Кипр", "Сардиния"),
                "Сицилия"),
            Question("9. Какое из течений Северного Ледовитого океана является тёплым?",
                listOf("Восточно-Гренландское", "Восточно-Исландское", "Норвежское"),
                "Норвежское"),
            Question("10. Самый жаркий континент?",
                listOf("Австралия", "Африка", "Южная Америка"),
                "Африка"),
        )
    }
    val questionId = remember { mutableStateOf(0) }
    val showEnd = remember { mutableStateOf(false)}
    val correctAnswers = remember { mutableStateOf(0) }

    if (!showEnd.value)
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Географическая викторина", fontSize = 20.sp,
            modifier = Modifier.padding(30.dp))
        Text(text = questions[questionId.value].text,
            fontSize = 17.sp, modifier = Modifier.padding(30.dp, 10.dp, 0.dp, 20.dp))
        questions[questionId.value].answers.forEachIndexed { index, option ->
            Button(
                onClick = {
                    if (!questions[questionId.value].showAnswer) {
                        if (option == questions[questionId.value].correctAnswer) {
                            questions[questionId.value].colors[index] = Color.Green
                            correctAnswers.value++
                        } else {
                            questions[questionId.value].colors[index] = Color.Red
                            questions[questionId.value].colors[questions[questionId.value].getCorrectAnswerId()] = Color.Green
                        }
                        questionId.value++
                        questionId.value--
                        questions[questionId.value].showAnswer = true
                    }
                },
                modifier = Modifier.padding(30.dp, 5.dp)
                    .shadow(10.dp, RoundedCornerShape(30.dp))) {
                Text(text = option, color = questions[questionId.value].colors[index])
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(30.dp, 40.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround)
        {
            if (questionId.value > 0)
            Button(onClick = {
                questionId.value -= 1
            }) {
                Text(text = "Назад")
            }
            if (questionId.value < questions.size - 1)
            Button(onClick = {
                questionId.value += 1
            }, modifier = Modifier.shadow(10.dp, RoundedCornerShape(30.dp))) {
                Text(text = "Далее")
            }
            else {
                Button(onClick = {
                    showEnd.value = true
                }, modifier = Modifier.shadow(10.dp, RoundedCornerShape(30.dp))) {
                    Text(text = "Завершить")
                }
            }
        }
    }
    if (showEnd.value) End(correctAnswers.value, questions.size)
}

@Preview(showSystemUi = true)
@Composable
fun End(correctAnswers: Int = 2, totalAnswers: Int = 2) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Правильные ответы: $correctAnswers из $totalAnswers",
            fontSize = 20.sp, modifier = Modifier.padding(0.dp, 100.dp, 0.dp, 50.dp))
        Button(onClick = {
            Runtime.getRuntime().exit(0)
        }, modifier = Modifier.shadow(10.dp, RoundedCornerShape(30.dp))) {
            Text(text = "Выход", fontSize = 25.sp)
        }
    }
}

data class Question(val text: String, val answers: List<String>, val correctAnswer: String,
                    var colors: MutableList<Color> = MutableList(answers.size) {Color.White},
                    var showAnswer: Boolean = false) {
    fun getCorrectAnswerId(): Int {
        return answers.indexOf(correctAnswer)
    }
}