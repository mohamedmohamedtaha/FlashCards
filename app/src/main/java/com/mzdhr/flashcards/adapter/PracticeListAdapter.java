package com.mzdhr.flashcards.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mzdhr.flashcards.R;
import com.mzdhr.flashcards.database.entity.CardEntity;

import java.util.List;

public class PracticeListAdapter extends PagedListAdapter<CardEntity, PracticeListAdapter.CardViewHolder>{

    private final LayoutInflater mInflater;
    private List<CardEntity> mCardEntities;

    public PracticeListAdapter(Context context) {
        super(CardEntity.DIFF_CALLBACK);
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PracticeListAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_practice, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PracticeListAdapter.CardViewHolder holder,final int position) {
        if (mCardEntities != null) {
            CardEntity currentCardEntity = mCardEntities.get(position);
            holder.cardFrontTextView.setText(currentCardEntity.getFrontSide());
            holder.cardBackTextView.setText(currentCardEntity.getBackSide());

            // Hook our custom click item listener to the item view.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.ItemClicked(v, position);
                    }
                }
            });
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

    /**
     * Method to get item by position.
     * @param position
     * @return
     */
    @Nullable
    public CardEntity getItem(int position) {
        return mCardEntities.get(position);
    }


    /**
     * Custom click item listener.
     */
    onItemClickListener mOnItemClickListener;

    public void setClickListener(onItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    public interface onItemClickListener {
        void ItemClicked(View v, int position);
    }


    /**
     * View Holder Class
     */
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
