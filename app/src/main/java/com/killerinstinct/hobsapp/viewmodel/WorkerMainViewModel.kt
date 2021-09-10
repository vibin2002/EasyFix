package com.killerinstinct.hobsapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.killerinstinct.hobsapp.model.Job
import com.killerinstinct.hobsapp.model.Request
import com.killerinstinct.hobsapp.model.Worker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WorkerMainViewModel : ViewModel() {

    private val TAG = "WorkerMainViewModel"
    private val userUid = FirebaseAuth.getInstance().currentUser?.uid
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _worker: MutableLiveData<Worker> = MutableLiveData()
    val worker: LiveData<Worker> = _worker

    private val _allWorkers: MutableLiveData<MutableList<Worker>> = MutableLiveData()
    val allWorkers: LiveData<MutableList<Worker>> = _allWorkers

    private val _requests: MutableLiveData<List<Request>> = MutableLiveData()
    val requests: LiveData<List<Request>> = _requests

    fun getWorkerDetails() {
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

    fun getAllWorkers() {
        viewModelScope.launch {
            val workers = mutableListOf<Worker>()
            db.collection("Worker")
                .get()
                .addOnSuccessListener { documents ->
                    for (doc in documents) {
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

    fun updateWorkerInfo(
//        location: String,
        phoneNumber: String,
        experience: String,
        minWage: String,
        category: List<String>,
        uri: Uri?,
        isCompleted: (Boolean) -> Unit
    ) {
        val workerData = mapOf(
//            "location" to location,
            "phoneNumber" to phoneNumber,
            "experience" to experience,
            "minWage" to minWage,
            "category" to category
        )
        if (userUid != null) {
            viewModelScope.launch {
                db.collection("Worker")
                    .document(userUid)
                    .update(workerData)
                    .addOnSuccessListener {
                        Log.d(TAG, "updateWorkerInfo: successfully added")
                        uploadProPic(uri, userUid) //Upload Profile pic
                        getWorkerDetails()
                        isCompleted(true)
                    }.addOnFailureListener {
                        isCompleted(false)
                        Log.d(TAG, "updateWorkerInfo: WorkerInfo not added")
                    }
            }
        } else {
            isCompleted(false)
        }
    }

    private fun uploadProPic(uri: Uri?, userUid: String) {
        viewModelScope.launch {
            if (uri == null)
                return@launch
            val ref = storage.getReference("$userUid/profilePicture")
            ref.putFile(uri)
                .addOnSuccessListener {
                    Log.d(TAG, "uploadImage: Success")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "download url success")
                        db.collection("Worker")
                            .document(userUid)
                            .update("profilePic", it.toString())
                            .addOnSuccessListener {
                                Log.d(TAG, "image url stored in db successfully")
                            }.addOnFailureListener {
                                Log.d(TAG, "image url failed to store in db")
                            }
                    }.addOnFailureListener {
                        Log.d(TAG, "updateWorkerInfo: $it")
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "updateWorkerInfo: $it")
                }
        }
    }

    fun getWorkerRequests(){
        db.collection("Request")
            .get()
            .addOnSuccessListener {
                val mutableList = mutableListOf<Request>()
                it.forEach { doc ->
                    val request = doc.toObject(Request::class.java)
                    if(request.to == userUid)
                        mutableList.add(request)
                }
                _requests.value = mutableList.toList()
            }.addOnFailureListener {
                Log.d(TAG, "getUserRequests: Failed to fetch requests")
            }
    }

    fun addJob(job: Job){
        db.collection("Jobs")
            .document(job.jobId)
            .set(job)
            .addOnSuccessListener {
                db.collection("User")
                    .document(job.fromId)
                    .update("jobs",FieldValue.arrayUnion(job.jobId))
                    .addOnSuccessListener {
                        db.collection("Worker")
                            .document(job.toId)
                            .update("jobs",FieldValue.arrayUnion(job.jobId))
                            .addOnSuccessListener {
                                deleteRequest(
                                    job.jobId,
                                    job.fromId,
                                    job.toId
                                )
                            }.addOnFailureListener {

                            }
                    }.addOnFailureListener {

                    }
            }.addOnFailureListener {

            }
    }

    fun deleteRequest(
        reqId: String,
        fromId: String,
        toId: String
    ){
        db.collection("Request")
            .document(reqId)
            .delete()
            .addOnSuccessListener {
                db.collection("User")
                    .document(fromId)
                    .update("requests",FieldValue.arrayRemove(reqId))
                    .addOnSuccessListener {
                        db.collection("Worker")
                            .document(toId)
                            .update("requests",FieldValue.arrayRemove(reqId))
                            .addOnSuccessListener {

                            }
                            .addOnFailureListener {

                            }
                    }.addOnFailureListener {

                    }
            }.addOnFailureListener {

            }
    }


}