package com.killerinstinct.hobsapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.killerinstinct.hobsapp.model.Worker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WorkerMainViewModel: ViewModel() {

    private val TAG = "WorkerMainViewModel"
    private val userUid = FirebaseAuth.getInstance().currentUser?.uid
    private val db = FirebaseFirestore.getInstance()

    private val _worker: MutableLiveData<Worker> = MutableLiveData()
    val worker: LiveData<Worker> = _worker
    
    private val _allWorkers: MutableLiveData<MutableList<Worker>> = MutableLiveData()
    val allWorkers: LiveData<MutableList<Worker>> = _allWorkers

    fun getWorkerDetails()
    {
        viewModelScope.launch {
            if (userUid == null)
                return@launch
            db.collection("Worker")
                .document(userUid)
                .get()
                .addOnSuccessListener {
                    val worker = it.toObject(Worker::class.java)
                    _worker.value = worker
                }.addOnFailureListener {
                    Log.d(TAG, "getWorkerDetails: $it")
                }
        }
    }
    
    fun getAllWorkers(){
        viewModelScope.launch { 
            val workers = mutableListOf<Worker>()
            db.collection("Worker")
                .get()
                .addOnSuccessListener { documents -> 
                    for (doc in documents){
                        val worker = doc.toObject(Worker::class.java)
                        workers.add(worker)
                    }
                    _allWorkers.value = workers
                    Log.d(TAG, "getAllWorkers: Obtained all workers")
                }.addOnFailureListener {
                    Log.d(TAG, "getAllWorkers: $it")
                }
        }
    }




}