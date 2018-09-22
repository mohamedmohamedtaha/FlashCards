package com.mzdhr.flashcards.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mzdhr.flashcards.R;
import com.mzdhr.flashcards.database.entity.SubjectEntity;

import java.util.List;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.SubjectViewHolder> {

    private final LayoutInflater mInflater;
    private List<SubjectEntity> mSubjectEntities;    // Cached

    public SubjectListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_subject, parent, false);
        return new SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectListAdapter.SubjectViewHolder holder, final int position) {
        if (mSubjectEntities != null) {
            SubjectEntity currentSubjectEntity = mSubjectEntities.get(position);
            holder.subjectTitleTextView.setText(currentSubjectEntity.getTitle());

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

    public void setSubjectEntities(List<SubjectEntity> subjectEntities) {
        mSubjectEntities = subjectEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mSubjectEntities != null) {
            return mSubjectEntities.size();
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
    public SubjectEntity getItem(int position) {
        return mSubjectEntities.get(position);
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
    class SubjectViewHolder extends RecyclerView.ViewHolder{
        private final TextView subjectTitleTextView;

        private SubjectViewHolder(View itemView) {
            super(itemView);
            subjectTitleTextView = itemView.findViewById(R.id.subject_title_textView);
        }
    }
}
