package com.gone.g_one_app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StartTestPage extends AppCompatActivity {
    DBHelper DB;
    String user;
    private static final int REQUEST_CODE_TEST = 1;
    private TextView textViewFinalscore;
    private TextView textViewNumberOfAttempts;
    private TextView textviewTestOneResult;
    private TextView textviewTestTwoResult;
    private TextView textviewTestThreeResult;
    private List<TestScore> scores;

    //    onCreate is called when the activity first starts, we can programatically set UI here
//    by using the

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test_page);
        user = getIntent().getExtras().getString("user");
        DB = new DBHelper(this);
//        int attempts = DB.checkAttemptCount(user);
        textViewFinalscore = findViewById(R.id.text_view_finalscore);
        textViewNumberOfAttempts = findViewById(R.id.text_view_numberOfAttempts);
        updatePreviousTestScoresOnView(user);
        updateAttemptsOnView(user);
        Button buttonStartQuiz = findViewById(R.id.button_start_test);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    checking for exceeding count
                    if(DB.checkAttemptCount(user) < 3){
                        startQuiz();
                    }else{
                        Toast.makeText(StartTestPage.this, "You exceeded 3 attempts, Sorry!!", Toast.LENGTH_SHORT).show();
                    }

                }

        });
    }

//    start button to start the quiz
    private void startQuiz() {
        Intent intent = new Intent(StartTestPage.this, TestActivity.class);
//        to receive a call back and direct flow to onActivityResult
        startActivityForResult(intent, REQUEST_CODE_TEST);
    }

// this function acts as a call back for startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TEST) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(TestActivity.FINAL_SCORE, 0);
                Log.v("StartTestPage", "Score=" + score);
                Log.v("StartTestPage", "Score=" + user);
                DB.insertScore(user, score);
                DB.insertAttemptCount(user);
                updateAttemptsOnView(user);
                updatePreviousTestScoresOnView(user);
                textViewFinalscore.setText("Result: " + score);
            }
        }
    }

//    Updating the attempts
    private void updateAttemptsOnView(String user) {
        int attempts = DB.checkAttemptCount(user);
        textViewNumberOfAttempts.setText("Number of attempts: "+attempts);
    }

//    updating the previous test scores
    private void updatePreviousTestScoresOnView(String user) {
        scores = DB.getAllTestsOfUser(user);
        for(int i=0;i<scores.size();i++){
            if(i==0){
                textviewTestOneResult = findViewById(R.id.text_view_test_one_result);
                textviewTestOneResult.setText("First test, Score: "+scores.get(i).getScore()+", Status: "+scores.get(i).getTeststatus());
            }else if(i==1){
                textviewTestTwoResult = findViewById(R.id.text_view_test_two_result);
                textviewTestTwoResult.setText("Second test, Score: "+scores.get(i).getScore()+", Status: "+scores.get(i).getTeststatus());
            }else if(i==2){
                textviewTestThreeResult = findViewById(R.id.text_view_test_three_result);
                textviewTestThreeResult.setText("Third test, Score: "+scores.get(i).getScore()+", Status: "+scores.get(i).getTeststatus());
            }else{

            }
            Log.v("StartTestPage", "Score=" + scores.get(i).getTeststatus());
        }
    }
}