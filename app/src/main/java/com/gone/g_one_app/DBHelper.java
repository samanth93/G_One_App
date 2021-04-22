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
    private static final String TAG = "DBHelper";
    public DBHelper(Context context) {
        super(context, "Gone.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT, numberofattempts INTEGER DEFAULT 0)");
        MyDB.execSQL("create Table questions(questionid INTEGER primary key autoincrement not null, testquestion TEXT, optionone TEXT, optiontwo TEXT, optionthree TEXT, optionfour TEXT, answer TEXT)");
        fillQuestionsTable(MyDB);
    }
    private void fillQuestionsTable(SQLiteDatabase myDB) {
        Question q1 = new Question("If you first convicted criminaly for how long your Licence could be suspended:", "One year", "Two years", "Three years", "Four years", 3);
        addQuestions(q1, myDB);
        Question q2 = new Question("The broken centre line on a roadway means you may:", "Never pass?", "Pass if the way is clear?", "Pass at any time?", "Pass only during daylight hours?", 2);
        addQuestions(q2, myDB);
        Question q3 = new Question("When driving in heavy fog, you should use:", "Lowbeamheadlights?", "Parking lights", "Parking lights and high beam headlights", "High beam headlights", 3);
        addQuestions(q3, myDB);
    }

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

    public Boolean insertAttemptCount(String user){
        Log.v(TAG, "User=" + user);
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
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

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}