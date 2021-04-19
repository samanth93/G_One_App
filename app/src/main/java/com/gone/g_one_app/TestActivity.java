package com.gone.g_one_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private TextView tvQuestion;
    private TextView tvScore;
    private TextView tvQuestionCount;
    private RadioGroup rbGroup;
    private RadioButton rbOne;
    private RadioButton rbTwo;
    private RadioButton rbThree;
    private RadioButton rbFour;
    private Button buttonConfirmNext;
    private List<Question> questionList;
    private ColorStateList textColorDefaultRb;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tvQuestion = findViewById(R.id.text_view_question);
        tvScore = findViewById(R.id.textView_score);
        tvQuestionCount = findViewById(R.id.textView_question_count);
        rbGroup = findViewById(R.id.radio_group);
        rbOne = findViewById(R.id.radio_option_one);
        rbTwo = findViewById(R.id.radio_option_two);
        rbThree = findViewById(R.id.radio_option_three);
        rbFour = findViewById(R.id.radio_option_four);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);
        textColorDefaultRb = rbOne.getTextColors();
        DBHelper dbHelper = new DBHelper(this);
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);
        showQuestion();
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rbOne.isChecked() || rbTwo.isChecked() || rbThree.isChecked() || rbFour.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(TestActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showQuestion();
                }
            }
        });
    }

    private void checkAnswer() {
        answered = true;
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        if (answerNr == currentQuestion.getAnswer()) {
            score++;
            tvScore.setText("Score: " + score);
        }
        displayCorrectAnswer();
    }

    private void displayCorrectAnswer() {
        rbOne.setTextColor(Color.RED);
        rbTwo.setTextColor(Color.RED);
        rbThree.setTextColor(Color.RED);
        rbFour.setTextColor(Color.RED);
        switch (currentQuestion.getAnswer()) {
            case 1:
                rbOne.setTextColor(Color.GREEN);
                tvQuestion.setText("Option 1 is correct");
                break;
            case 2:
                rbTwo.setTextColor(Color.GREEN);
                tvQuestion.setText("Option 2 is correct");
                break;
            case 3:
                rbThree.setTextColor(Color.GREEN);
                tvQuestion.setText("Option 3 is correct");
                break;
            case 4:
                rbFour.setTextColor(Color.GREEN);
                tvQuestion.setText("Option 4 is correct");
                break;
        }
        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }

    private void showQuestion() {
        rbOne.setTextColor(textColorDefaultRb);
        rbTwo.setTextColor(textColorDefaultRb);
        rbThree.setTextColor(textColorDefaultRb);
        rbFour.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);
            tvQuestion.setText(currentQuestion.getQuestion());
            rbOne.setText(currentQuestion.getOptionOne());
            rbTwo.setText(currentQuestion.getOptionTwo());
            rbThree.setText(currentQuestion.getOptionThree());
            rbFour.setText(currentQuestion.getOptionFour());
            questionCounter++;
            tvQuestionCount.setText("Question: " + questionCounter + " of " + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");
        } else {
            finishTest();
        }
    }

    private void finishTest() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


}