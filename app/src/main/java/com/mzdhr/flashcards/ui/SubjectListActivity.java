package com.mzdhr.flashcards.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mzdhr.flashcards.R;
import com.mzdhr.flashcards.adapter.SubjectListAdapter;
import com.mzdhr.flashcards.database.entity.SubjectEntity;
import com.mzdhr.flashcards.viewmodel.SubjectListViewModel;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class SubjectListActivity extends AppCompatActivity {

    private SubjectListViewModel mSubjectListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Adapter
        final SubjectListAdapter adapter = new SubjectListAdapter(this);

        // RecyclerView
        final RecyclerView recyclerView = findViewById(R.id.subject_list_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Getting our viewModel
        mSubjectListViewModel = ViewModelProviders.of(this).get(SubjectListViewModel.class);
        // Observe data and setting it to the adapter
        mSubjectListViewModel.getAllSubjects().observe(this, new Observer<List<SubjectEntity>>() {
            @Override
            public void onChanged(@Nullable List<SubjectEntity> subjectEntities) {
                adapter.setSubjectEntities(subjectEntities);
            }
        });


        // Add a random subject
        FloatingActionButton addSubjectButton = findViewById(R.id.fab);
        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int n = random.nextInt(1000) + 1;
                mSubjectListViewModel.insert(new SubjectEntity("Random subject number " + n, new Date(), 1));
            }
        });


        // Delete subject by swabbing item left and right
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                final SubjectEntity subject = adapter.getItem(position);
                mSubjectListViewModel.delete(subject);
                Toast.makeText(SubjectListActivity.this, "Subject: " + subject.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);


        // Open subject when click on it
        adapter.setClickListener(new SubjectListAdapter.onItemClickListener() {
            @Override
            public void ItemClicked(View v, int position) {
                final SubjectEntity subject = adapter.getItem(position);
                Intent intent = new Intent(SubjectListActivity.this, CardListActivity.class);
                intent.putExtra("SUBJECT_EXTRA_ID", subject.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
