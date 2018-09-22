package com.mzdhr.flashcards;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mzdhr.flashcards.database.AppDatabase;
import com.mzdhr.flashcards.database.dao.CardDao;
import com.mzdhr.flashcards.database.dao.SubjectDao;
import com.mzdhr.flashcards.database.entity.CardEntity;
import com.mzdhr.flashcards.database.entity.SubjectEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class DatabaseDaoTest {
    private AppDatabase mAppDatabase;
    private SubjectDao mSubjectDao;
    private CardDao mCardDao;
    private Context mContext;

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
        mAppDatabase = Room.inMemoryDatabaseBuilder(mContext, AppDatabase.class).build();
        mSubjectDao = mAppDatabase.subjectDao();
        mCardDao = mAppDatabase.cardDao();
    }

    @After
    public void closeDatabase() {
        mAppDatabase.close();
    }

    @Test
    public void testInsertSubject() throws InterruptedException {
        // Arrange
        SubjectEntity subjectEntity = new SubjectEntity("Math", new Date(), 1);

        // Action
        mSubjectDao.insertSubject(subjectEntity);

        // Assertion
        final LiveData<List<SubjectEntity>> retrieveSubjects = mSubjectDao.getAllSubject();

        // Assert not empty
        // create a count down
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        // Setting an observer for our LiveData object
        retrieveSubjects.observeForever(new Observer<List<SubjectEntity>>() {
            @Override
            public void onChanged(@Nullable List<SubjectEntity> subjectEntities) {
                // Assert that our LiveData not empty
                assertThat("Assert subjectEntity not Empty", subjectEntities.size(), not(0));
                // Assert that our first object is inserted and exist in the database
                assertThat("Assert subjectEntity inserted", subjectEntities.get(0).getTitle(), is("Mathematics"));
                countDownLatch.countDown();     // start the count down
                retrieveSubjects.removeObserver(this);  // remove the observer
            }
        });
        // tell the count down to wait for 2 seconds
        countDownLatch.await(2, TimeUnit.SECONDS);
    }

    @Test
    public void testUpdateSubject() throws InterruptedException {
        // Arrange
        final LiveData<List<SubjectEntity>> retrieveSubjects = mSubjectDao.getAllSubject();
        final SubjectEntity subjectEntity = new SubjectEntity("Math", new Date(), 1);
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        // Action
        mSubjectDao.insertSubject(subjectEntity);   // insert a subject
        // update subject (You need to use the id when you want to updated also use the room constructor)
        // We are creating a new subject with the same id of the old, Room gonna figure it out and updated.
        mSubjectDao.updateSubject(new SubjectEntity(1, "Mathematics", subjectEntity.getDate(), subjectEntity.getColor()));

        // Assertion
        retrieveSubjects.observeForever(new Observer<List<SubjectEntity>>() {
            @Override
            public void onChanged(@Nullable List<SubjectEntity> subjectEntities) {
                assertThat(subjectEntities.get(0).getTitle(), is("Mathematics"));
                countDownLatch.countDown();
                retrieveSubjects.removeObserver(this);
            }
        });
        countDownLatch.await(2, TimeUnit.SECONDS);
    }


    /**
     * More Info at:
     * https://developer.android.com/topic/libraries/architecture/paging/
     * @throws InterruptedException
     */
    @Test
    public void testCardAndSubject() throws InterruptedException{
        // Arrange
        final SubjectEntity subjectEntity = new SubjectEntity("Grammar", new Date(), 1);
        final CardEntity card1 = new CardEntity("Book", "Box with papers!", 1, new Date());
        final CardEntity card2 = new CardEntity("Paper", "Paper with lines!", 1, new Date());
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final LiveData<PagedList<CardEntity>> mCardsByParentId = new LivePagedListBuilder<>(mCardDao.getCardsByParentId(1), 20).build();

        // Action
        mSubjectDao.insertSubject(subjectEntity);
        mCardDao.insertCard(card1);
        mCardDao.insertCard(card2);

        // Assert
        mCardsByParentId.observeForever(new Observer<PagedList<CardEntity>>() {
            @Override
            public void onChanged(@Nullable PagedList<CardEntity> cardEntities) {
                assertThat(cardEntities.get(0).getFrontSide(), is("Book"));
                assertThat(cardEntities.get(0).getBackSide(), is("Box with papers!"));
                assertThat(cardEntities.get(0).getParentId(), is(1));

                assertThat(cardEntities.get(1).getFrontSide(), is("Paper"));
                assertThat(cardEntities.get(1).getBackSide(), is("Paper with lines!"));
                assertThat(cardEntities.get(1).getParentId(), is(2));
                countDownLatch.countDown();
                mCardsByParentId.removeObserver(this);
            }
        });
        countDownLatch.await(2, TimeUnit.SECONDS);
    }


}
