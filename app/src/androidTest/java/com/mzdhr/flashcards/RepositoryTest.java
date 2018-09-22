package com.mzdhr.flashcards;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mzdhr.flashcards.database.AppDatabase;
import com.mzdhr.flashcards.database.dao.CardDao;
import com.mzdhr.flashcards.database.dao.SubjectDao;
import com.mzdhr.flashcards.database.entity.SubjectEntity;
import com.mzdhr.flashcards.repository.SubjectRepository;
import com.mzdhr.flashcards.ui.SubjectListActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RepositoryTest {

    SubjectRepository mSubjectRepository;

    private AppDatabase mAppDatabase;
    private SubjectDao mSubjectDao;
    private CardDao mCardDao;
    private Context mContext;

    @Rule
    public ActivityTestRule<SubjectListActivity> mActivityTestRule = new ActivityTestRule<>(SubjectListActivity.class);


    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();

        mAppDatabase = Room.inMemoryDatabaseBuilder(mContext, AppDatabase.class).build();
        mSubjectDao = mAppDatabase.subjectDao();
        mCardDao = mAppDatabase.cardDao();

        mSubjectRepository = SubjectRepository.getInstance(mActivityTestRule.getActivity().getApplication());
    }

    @Test
    public void testRepository() throws InterruptedException{
        mSubjectRepository.insert(new SubjectEntity("Math", new Date(), 1));

        final LiveData<SubjectEntity> retrieveSubject = mSubjectRepository.getSubjectById(1);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        retrieveSubject.observeForever(new Observer<SubjectEntity>() {
            @Override
            public void onChanged(@Nullable SubjectEntity subjectEntity) {
                assertThat(subjectEntity.getTitle(), is("Computer"));
                countDownLatch.countDown();
                retrieveSubject.removeObserver(this);
            }
        });
        countDownLatch.await(2, TimeUnit.SECONDS);
    }

}
