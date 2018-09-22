package com.mzdhr.flashcards.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.mzdhr.flashcards.database.entity.SubjectEntity;
import com.mzdhr.flashcards.repository.SubjectRepository;

import java.util.List;

public class SubjectListViewModel extends AndroidViewModel {

    private SubjectRepository mRepository;
    private LiveData<List<SubjectEntity>> mAllSubjects;

    public SubjectListViewModel(@NonNull Application application) {
        super(application);
        mRepository = SubjectRepository.getInstance(application);
        mAllSubjects = mRepository.getAllSubjects();
    }

    public LiveData<List<SubjectEntity>> getAllSubjects() {
        return mAllSubjects;
    }

    public void getSubjectById(int subjectId) {
        mRepository.getSubjectById(subjectId);
    }

    public void insert(SubjectEntity subjectEntity) {
        mRepository.insert(subjectEntity);
    }

    public void update(SubjectEntity subjectEntity) {
        mRepository.update(subjectEntity);
    }

    public void delete(SubjectEntity subjectEntity) {
        mRepository.delete(subjectEntity);
    }

    public void deleteAll(SubjectEntity subjectEntity) {
        mRepository.deleteAll();
    }

}
