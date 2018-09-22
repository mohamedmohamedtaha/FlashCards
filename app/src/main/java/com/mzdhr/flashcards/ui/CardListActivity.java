package com.mzdhr.flashcards.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mzdhr.flashcards.R;
import com.mzdhr.flashcards.adapter.CardListAdapter;
import com.mzdhr.flashcards.database.entity.CardEntity;
import com.mzdhr.flashcards.viewmodel.CardListViewModel;

import java.util.Date;
import java.util.Random;

public class CardListActivity extends AppCompatActivity {

    private static final String TAG = CardListActivity.class.getSimpleName();
    private CardListViewModel mCardListViewModel;
    private int mCardParentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCardParentId = getIntent().getIntExtra("SUBJECT_EXTRA_ID", -1);

        // Adapter
        final CardListAdapter adapter = new CardListAdapter(this);

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.card_list_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Preparing our factory
        CardListViewModel.Factory factory = new CardListViewModel.Factory(getApplication(), mCardParentId);
        // Getting our viewModel
        mCardListViewModel = ViewModelProviders.of(this, factory).get(CardListViewModel.class);
        // Setting data to the adapter
        mCardListViewModel.getCardsByParentId().observe(this, new Observer<PagedList<CardEntity>>() {
            @Override
            public void onChanged(@Nullable PagedList<CardEntity> cardEntities) {
                adapter.setCardEntities(cardEntities);
            }
        });

        // Add a random card
        FloatingActionButton addCardButton = findViewById(R.id.add_card_fab);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int n = random.nextInt(1000) + 1;
                mCardListViewModel.insert(new CardEntity("This is front side. It should be a Word, or a Question. #" + n, "This is back side. It should be a Definition, or an Answer. #" + n, mCardParentId, new Date()));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start_practice) {
            Intent intent = new Intent(CardListActivity.this, PracticeListActivity.class);
            intent.putExtra("SUBJECT_EXTRA_ID", mCardParentId);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
