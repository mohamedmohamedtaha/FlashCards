package com.mzdhr.flashcards.database.dao;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mzdhr.flashcards.database.entity.CardEntity;

@Dao
public interface CardDao {

    // Using Paging
    @Query("SELECT * FROM cards ")
    DataSource.Factory<Integer, CardEntity> getAllCard();

    // Using Paging
    @Query("SELECT * FROM cards WHERE card_parent_id = :id")
    DataSource.Factory<Integer, CardEntity> getCardsByParentId(int id);

    @Query("SELECT * FROM cards WHERE card_id = :id")
    CardEntity getCardById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCard(CardEntity cardEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCard(CardEntity cardEntity);

    @Delete
    void deleteCard(CardEntity cardEntity);

    @Query("DELETE FROM cards")
    void deleteAll();

}
