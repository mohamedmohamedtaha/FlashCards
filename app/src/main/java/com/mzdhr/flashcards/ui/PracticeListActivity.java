package com.mzdhr.flashcards.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mzdhr.flashcards.R;
import com.mzdhr.flashcards.adapter.PracticeListAdapter;
import com.mzdhr.flashcards.database.entity.CardEntity;
import com.mzdhr.flashcards.viewmodel.CardListViewModel;

import java.util.ArrayList;

public class PracticeListActivity extends AppCompatActivity {

    private static final String TAG = PracticeListActivity.class.getSimpleName();
    private CardListViewModel mCardListViewModel;
    private int mCardParentId;
    int mCurrentPosition;
    private ArrayList<CardEntity> mCurrentCardEntities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCardParentId = getIntent().getIntExtra("SUBJECT_EXTRA_ID", -1);

        // Adapter
        final PracticeListAdapter adapter = new PracticeListAdapter(this);

        // RecyclerView
        final RecyclerView recyclerView = findViewById(R.id.practice_list_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);

        // Preparing our factory
        CardListViewModel.Factory factory = new CardListViewModel.Factory(getApplication(), mCardParentId);
        // getting our viewModel
        mCardListViewModel = ViewModelProviders.of(this, factory).get(CardListViewModel.class);
        // Setting data to the adapter
        mCardListViewModel.getCardsByParentId().observe(this, new Observer<PagedList<CardEntity>>() {
            @Override
            public void onChanged(@Nullable PagedList<CardEntity> cardEntities) {
                mCurrentCardEntities = new ArrayList<>();
                mCurrentCardEntities.addAll(cardEntities);
                adapter.setCardEntities(cardEntities);
            }
        });


        // Navigation Button
        adapter.setClickListener(new PracticeListAdapter.onItemClickListener() {
            @Override
            public void ItemClicked(final View v, int position) {
                //CardEntity card = adapter.getItem(position);
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(v, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        oa2.start();
                        TextView front = v.findViewById(R.id.card_front_textView);
                        TextView back = v.findViewById(R.id.card_back_textView);
                        if (front.getVisibility() == View.VISIBLE) {
                            front.setVisibility(View.GONE);
                            back.setVisibility(View.VISIBLE);
                            v.setBackgroundColor(getResources().getColor(R.color.colorDefaultBackCard));
                        } else {
                            front.setVisibility(View.VISIBLE);
                            back.setVisibility(View.GONE);
                            v.setBackgroundColor(getResources().getColor(R.color.colorDefaultFrontCard));
                        }
                    }
                });
                oa1.start();
            }
        });

        ImageView nextArrow = findViewById(R.id.right_arrow_imageView);
        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                recyclerView.smoothScrollToPosition(mCurrentPosition + 1);
            }
        });

        ImageView backArrow = findViewById(R.id.left_arrow_imageView);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (mCurrentPosition != 0) {
                    recyclerView.smoothScrollToPosition(mCurrentPosition - 1);
                } else {
                    Toast.makeText(PracticeListActivity.this, "You reach start", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView closeImageView = findViewById(R.id.close_imageView);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView correctImageView = findViewById(R.id.correct_imageView);
        correctImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentCardEntities.remove(mCardParentId);
            }
        });

        ImageView wrongImageView = findViewById(R.id.wrong_imageView);
        wrongImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageView againImageView = findViewById(R.id.again_imageView);
        againImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setCardEntities(mCurrentCardEntities);
            }
        });
    }

}
