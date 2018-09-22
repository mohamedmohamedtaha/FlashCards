package com.mzdhr.flashcards.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mzdhr.flashcards.R;
import com.mzdhr.flashcards.database.entity.CardEntity;

import java.util.List;

public class CardListAdapter extends PagedListAdapter<CardEntity, CardListAdapter.CardViewHolder>{

    private final LayoutInflater mInflater;
    private List<CardEntity> mCardEntities;

    public CardListAdapter(Context context) {
        super(CardEntity.DIFF_CALLBACK);
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CardListAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_card, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardListAdapter.CardViewHolder holder, int position) {
        if (mCardEntities != null) {
            CardEntity currentCardEntity = mCardEntities.get(position);
            holder.cardFrontTextView.setText(currentCardEntity.getFrontSide());
            holder.cardBackTextView.setText(currentCardEntity.getBackSide());
        }
    }

    public void setCardEntities(List<CardEntity> cardEntities) {
        mCardEntities = cardEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCardEntities != null) {
            return mCardEntities.size();
        } else {
            return 0;
        }
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private final TextView cardFrontTextView;
        private final TextView cardBackTextView;

        public CardViewHolder(View itemView) {
            super(itemView);
            cardFrontTextView = itemView.findViewById(R.id.card_front_textView);
            cardBackTextView = itemView.findViewById(R.id.card_back_textView);

        }
    }

}
