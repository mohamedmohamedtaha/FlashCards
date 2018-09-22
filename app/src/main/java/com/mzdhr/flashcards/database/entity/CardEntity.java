package com.mzdhr.flashcards.database.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.v7.util.DiffUtil;

import java.util.Date;

@Entity(
        tableName = "cards",
        foreignKeys = {
                @ForeignKey(entity = SubjectEntity.class,
                        parentColumns = "subject_id",
                        childColumns = "card_parent_id",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "card_parent_id")}
)
public class CardEntity {

    /**
     * A comparator for our CardListAdapter (because it is required for PagedListAdapter type to use paging).
     */
    public static DiffUtil.ItemCallback<CardEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<CardEntity>() {
        @Override
        public boolean areItemsTheSame(CardEntity oldItem, CardEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(CardEntity oldItem, CardEntity newItem) {
            return oldItem.equals(newItem);
        }
    };

    // We override this method to our comparator
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            CardEntity cardEntity = (CardEntity) obj;
            return cardEntity.getId() == this.getId() && cardEntity.getFrontSide() == this.getFrontSide();
        }
    }


    /**
     * Fields
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_id")
    private int mId;
    @ColumnInfo(name = "card_parent_id")
    private int mParentId;
    private String mFrontSide;
    private String mBackSide;
    private Date mDate;

    // Room Construction
    public CardEntity(int id, String frontSide, String backSide, int parentId, Date date) {
        mId = id;
        mFrontSide = frontSide;
        mBackSide = backSide;
        mParentId = parentId;
        mDate = date;
    }

    // Our Construction
    @Ignore
    public CardEntity(String frontSide, String backSide, int parentId, Date date) {
        mFrontSide = frontSide;
        mBackSide = backSide;
        mParentId = parentId;
        mDate = date;
    }

    // Getter and Setter pattern required for Room.
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setParentId(int parentId) {
        mParentId = parentId;
    }

    public String getFrontSide() {
        return mFrontSide;
    }

    public void setFrontSide(String frontSide) {
        mFrontSide = frontSide;
    }

    public String getBackSide() {
        return mBackSide;
    }

    public void setBackSide(String backSide) {
        mBackSide = backSide;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
