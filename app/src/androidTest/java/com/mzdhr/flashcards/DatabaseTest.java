package com.mzdhr.flashcards;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mzdhr.flashcards.database.AppDatabase;
import com.mzdhr.flashcards.database.dao.CardDao;
import com.mzdhr.flashcards.database.dao.SubjectDao;
import com.mzdhr.flashcards.database.entity.SubjectEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private SubjectDao mSubjectDao;
    private CardDao mCardDao;
    private AppDatabase mAppDatabase;


    @Before
    public void createDatabase() {
        Context context = InstrumentationRegistry.getTargetContext();
        mAppDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mSubjectDao = mAppDatabase.subjectDao();
        mCardDao = mAppDatabase.cardDao();
    }

    @Test
    public void writeSubject() {
//        // Arrange
//        SubjectEntity subjectEntity = new SubjectEntity("Chemistry", new Date(), 1);
//        // Action
//        long subjectId = mSubjectDao.insertSubject(subjectEntity);
//        // Assert
//        final LiveData<SubjectEntity> retrieveSubject = mSubjectDao.getSubjectById(subjectId);
//        retrieveSubject.observeForever(new Observer<SubjectEntity>() {
//            @Override
//            public void onChanged(@Nullable SubjectEntity subjectEntity) {
//                assertThat(retrieveSubject.getValue().getTitle(), is("Chemistry"));
//            }
//        });
    }

    @Test
    public void writeSubjects() {
        for (int i = 0; i < 10; i++) {
            SubjectEntity subjectEntity = new SubjectEntity("SubjectEntity #" + i, new Date(), 9 - i);
            mSubjectDao.insertSubject(subjectEntity);
        }

        LiveData<List<SubjectEntity>> retrieveSubjects = mSubjectDao.getAllSubject();
        retrieveSubjects.observeForever(new Observer<List<SubjectEntity>>() {
            @Override
            public void onChanged(@Nullable List<SubjectEntity> subjectEntities) {
                for (int i = 0; i < subjectEntities.size(); i++) {
                    assertThat(subjectEntities.size(), not(0));
                }
            }
        });

    }

    @Test
    public void writeCardToSubject() {
//        final SubjectEntity subjectEntity = new SubjectEntity("Math", new Date(), 1);
//        long subjectId = mSubjectDao.insertSubject(subjectEntity);
//
//        final CardEntity cardEntity01 = new CardEntity("Question", "Answer", subjectId, new Date());
//
//   //     LiveData<List<CardEntity>> retrieveCards = mCardDao.getCardsByParentId(subjectId);
////
////        retrieveCards.observeForever(new Observer<List<CardEntity>>() {
////            @Override
////            public void onChanged(@Nullable List<CardEntity> cards) {
////                assertThat(cards.get(0), equalTo(cardEntity01));
////
////            }
////        });

    }


    @After
    public void closeDatabase() throws IOException {
        mAppDatabase.close();
    }
}
