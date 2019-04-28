package com.udacity.android.quizapp;

import com.udacity.android.quizapp.model.Question;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface QuizDao {

    @Query("SELECT * FROM Question")
    LiveData<List<Question>> getQuestionList();

    @Query("SELECT * FROM Question WHERE questionId = :questionId")
    LiveData<Question> findQuestionById(int questionId);

    @Insert
    void insertQuestion(Question question);

    @Update
    void updateQuestion(Question question);

    @Delete
    void deleteQuestion(Question question);
}
