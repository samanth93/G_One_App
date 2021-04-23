package com.gone.g_one_app;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gone.g_one_app.TestContract.*;
import com.gone.g_one_app.UserContract.*;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
//    Below tag is for debugging
    private static final String TAG = "DBHelper";
//    Initializing database context
    public DBHelper(Context context) {
        super(context, "Gone.db", null, 1);
    }

//    creating all the database tables required on activity start
    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key not null, password TEXT, numberofattempts INTEGER DEFAULT 0)");
        MyDB.execSQL("create Table questions(questionid INTEGER primary key autoincrement not null, testquestion TEXT, optionone TEXT, optiontwo TEXT, optionthree TEXT, optionfour TEXT, answer TEXT)");
        MyDB.execSQL("create Table users_test(testid INTEGER primary key autoincrement not null, username TEXT not null, teststatus TEXT DEFAULT 'Fail', score INTEGER DEFAULT 0, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (username) REFERENCES users (username))");
//        Fill the questions table with all the questions
        fillQuestionsTable(MyDB);
    }
    private void fillQuestionsTable(SQLiteDatabase myDB) {
//        creating questions object and adding it to table using addQuestion method
        Question q1 = new Question("If you first convicted criminaly for how long your Licence could be suspended:", "One year", "Two years", "Three years", "Four years", 3);
        addQuestions(q1, myDB);
        Question q2 = new Question("The broken centre line on a roadway means you may:", "Never pass?", "Pass if the way is clear?", "Pass at any time?", "Pass only during daylight hours?", 2);
        addQuestions(q2, myDB);
        Question q3 = new Question("When driving in heavy fog, you should use:", "Lowbeamheadlights?", "Parking lights", "Parking lights and high beam headlights", "High beam headlights", 3);
        addQuestions(q3, myDB);
        Question q4 = new Question(" In Ontario, a driver will be given 7 demerit points if he/she is convicted of:", "Following too close", "Failing to stop for a stopped school bus with its red alternating lights on", "Failing to remain at the scene of an accident", "Failing to obey the directions of a police officer", 3);
        addQuestions(q4, myDB);
        Question q5 = new Question("In Ontario, a first conviction for drinking and driving can bring a minimum license suspension of:", "6 months", "12 months", "24 months", "48 months", 2);
        addQuestions(q5, myDB);
        Question q6 = new Question("In Ontario, it is legal to turn right on red:", "Only after the vehicle has come to a complete stop yielding right of way", "Only from a one-way street to another one-way street", "Only from a two-way street to another two-way street", "Only from a one-way street to a two-way street", 1);
        addQuestions(q6, myDB);
        Question q7 = new Question("In what lane should you drive if you intend to make a right turn?", " Close to the left of the roadway.", "Close to the right hand side of the roadway", "Close to the centerline of the roadway", "Does not matter", 2);
        addQuestions(q7, myDB);
        Question q8 = new Question("In Ontario, a solid yellow line at the left of your lane means:", "It is illegal to pass", "It is unsafe to pass", "It is illegal to cross over", "It is legal to pass", 2);
        addQuestions(q8, myDB);
        Question q9 = new Question("Before leaving the car parked on a downgrade, you should:", "Leave your front wheels parallel to the curb", "Turn your front wheels to the left and set the parking brakes. ", "Set the parking brakes only.", "Turn your front wheels to the right and set the parking brakes", 4);
        addQuestions(q9, myDB);
        Question q10 = new Question("As a level G2 driver, your blood alcohol level must not be over:", "0.08% ", "0.05% ", " 0.02%", "0.00% ", 4);
        addQuestions(q10, myDB);
    }

//    this method is used to add questions to table
    private void addQuestions(Question ques, SQLiteDatabase myDB) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TestContract.COLUMN_QUESTIONS, ques.getQuestion());
        contentValues.put(TestContract.COLUMN_OPTION_ONE, ques.getOptionOne());
        contentValues.put(TestContract.COLUMN_OPTION_TWO, ques.getOptionTwo());
        contentValues.put(TestContract.COLUMN_OPTION_THREE, ques.getOptionThree());
        contentValues.put(TestContract.COLUMN_OPTION_FOUR, ques.getOptionFour());
        contentValues.put(TestContract.COLUMN_ANSWER, ques.getAnswer());
        myDB.insert("questions", null, contentValues);
    }

//    this method is used to get all the questions and send it in the form of ArrayList
    public List<Question> getAllQuestions() {
        List<Question> questionsList = new ArrayList<>();
        SQLiteDatabase MyDB = getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM " + TestContract.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(cursor.getString(cursor.getColumnIndex(TestContract.COLUMN_QUESTIONS)));
                question.setOptionOne(cursor.getString(cursor.getColumnIndex(TestContract.COLUMN_OPTION_ONE)));
                question.setOptionTwo(cursor.getString(cursor.getColumnIndex(TestContract.COLUMN_OPTION_TWO)));
                question.setOptionThree(cursor.getString(cursor.getColumnIndex(TestContract.COLUMN_OPTION_THREE)));
                question.setOptionFour(cursor.getString(cursor.getColumnIndex(TestContract.COLUMN_OPTION_FOUR)));
                question.setAnswer(cursor.getInt(cursor.getColumnIndex(TestContract.COLUMN_ANSWER)));
                questionsList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionsList;
    }

//    Details of all tests to show in the test start page
    public List<TestScore> getAllTestsOfUser(String user) {
        List<TestScore> testList = new ArrayList<>();
        SQLiteDatabase MyDB = getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users_test WHERE username = ?" , new String[]{user});
        if (cursor.moveToFirst()) {
            do {
                TestScore score = new TestScore();
                score.setTestid(cursor.getInt(cursor.getColumnIndex("testid")));
                score.setScore(cursor.getInt(cursor.getColumnIndex("score")));
                score.setTeststatus(cursor.getString(cursor.getColumnIndex("teststatus")));
                testList.add(score);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return testList;
    }

//    onUpgrade() is called (you do not call it yourself) when version of your DB changed which means underlying table structure changed etc
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists questions");
    }

    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

//    Score and test status are inserted into users_test table
    public Boolean insertScore(String username, int score){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("score", score);
        if(score > 4){
            contentValues.put("teststatus", "Pass");
        }
        long result = MyDB.insert("users_test", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

//    Number of attempts condition implementation
    public Boolean insertAttemptCount(String user){
        Log.v(TAG, "User=" + user);
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        ContentValues contentValuesTwo= new ContentValues();
        contentValues.put("username", user);
        Cursor cursor = MyDB.rawQuery("SELECT numberofattempts FROM users where username = ?", new String[]{user});
        if(cursor.moveToFirst()){
            int x = cursor.getInt(cursor.getColumnIndex("numberofattempts"))+1;
            Log.v(TAG, "numberofattempts"+x);
            contentValues.put("numberofattempts", x);
            if(x<4){
                long result = MyDB.update("users", contentValues,"username = '"+user+"'", null);
                if(result==-1) return false;
                else
                    return true;
            }
        }
        return false;
    }

//    Retrieving attempt count
    public int checkAttemptCount(String user){
        int x = 0;
        Log.v(TAG, "User=" + user);
        Log.v(TAG, "Count=" + x);
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT numberofattempts FROM users where username = ?", new String[]{user});
        if(cursor.moveToFirst()){
            x = cursor.getInt(cursor.getColumnIndex("numberofattempts"));
            if(x<3){
                return x;
            }
        }
        return x;
    }

//    User name checking for registration
    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

//    Username and password checking for login
    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}