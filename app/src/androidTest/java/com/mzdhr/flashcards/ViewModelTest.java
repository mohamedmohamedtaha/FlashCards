package com.mzdhr.flashcards;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.mzdhr.flashcards.database.AppDatabase;
import com.mzdhr.flashcards.database.dao.CardDao;
import com.mzdhr.flashcards.database.dao.SubjectDao;
import com.mzdhr.flashcards.database.entity.CardEntity;
import com.mzdhr.flashcards.ui.SubjectListActivity;
import com.mzdhr.flashcards.viewmodel.CardListViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class ViewModelTest {
    private AppDatabase mAppDatabase;
    private SubjectDao mSubjectDao;
    private CardDao mCardDao;
    private Context mContext;
    private CardListViewModel mCardListViewModel;

    /**
     * Don't forget to add -> androidTestImplementation "com.android.support.test:rules:0.5" in gradle.
     */
    @Rule
    public ActivityTestRule<SubjectListActivity> mActivityTestRule = new ActivityTestRule<>(SubjectListActivity.class);


    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
        mAppDatabase = Room.inMemoryDatabaseBuilder(mContext, AppDatabase.class).build();
        mSubjectDao = mAppDatabase.subjectDao();
        mCardDao = mAppDatabase.cardDao();
//        mCardListViewModel = mock(CardListViewModel.class);

//        mCardListViewModel = ViewModelProviders.of(this, )
        mCardListViewModel = spy(CardListViewModel.class);


    }

    @After
    public void closeDatabase() {
        mAppDatabase.close();
    }

    @Test
    public void ttt() {
        CardEntity cardEntity = new CardEntity("Front", "Back", 1, new Date());
        mCardListViewModel.insert(cardEntity);


    }


























}
