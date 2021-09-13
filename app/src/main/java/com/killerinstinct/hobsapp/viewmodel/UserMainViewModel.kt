package com.killerinstinct.hobsapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.killerinstinct.hobsapp.Utils
import com.killerinstinct.hobsapp.model.*
import kotlinx.coroutines.launch

class UserMainViewModel: ViewModel() {

    private val TAG = "UserMainViewModel"
    private val userUid = FirebaseAuth.getInstance().currentUser?.uid
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _allWorkers: MutableLiveData<MutableList<Worker>> = MutableLiveData()
    val allWorkers: LiveData<MutableList<Worker>> = _allWorkers

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    private val _requests: MutableLiveData<List<Request>> = MutableLiveData()
    val requests: LiveData<List<Request>> = _requests

    private val _notifications: MutableLiveData<List<Notification>> = MutableLiveData()
    val notification: LiveData<List<Notification>> = _notifications

    private val _notifyIds: MutableLiveData<List<String>> = MutableLiveData()
    val notifyIds: LiveData<List<String>> = _notifyIds

    private val _jobs: MutableLiveData<List<Job>> = MutableLiveData()
    val jobs: LiveData<List<Job>> = _jobs


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

    fun fetchUserDetail(){
        viewModelScope.launch {
            if (userUid == null)
                return@launch
            db.collection("User")
                .document(userUid)
                .get()
                .addOnSuccessListener {
                    _user.value = it.toObject(User::class.java)
                    Log.d(TAG, "fetchUserDetail: User fetched ${user.value}")
                    getAllJobs()
                    getUserNotifications()
                }.addOnFailureListener {
                    Log.d(TAG, "fetchUserDetail: User not fetched")
                }
        }
    }

    private fun getUserNotifications(){
        viewModelScope.launch {
            val userid = _user.value?.uid ?: return@launch
            db.collection("User")
                .document(userid)
                .collection("Notifications")
                .orderBy("timestamp",Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    val list = mutableListOf<Notification>()
                    val ids = mutableListOf<String>()
                    it.forEach { document ->
                        ids.add(document.id)
                        list.add(document.toObject(Notification::class.java))
                    }
                    _notifications.value = list.toList()
                    _notifyIds.value = ids.toList()
                    Log.d(TAG, "getUserNotifications: ${notification.value}")
                }.addOnFailureListener {
                    TODO()
                }
        }
    }

    fun sendWorkRequest(
        name: String,
        fromId: String,
        toId: String,
        description: String,
        location: GeoPoint,
        date: String,
        time: String,
        contact: String,
        isSent: (Boolean) -> Unit
    ){
        val reqId = Utils.randomString()
        val request = Request(
            reqId,
            name,
            fromId,
            toId,
            description,
            date,
            time,
            location,
            contact
        )
        db.collection("Request")
            .document(reqId)
            .set(request)
            .addOnSuccessListener {
                db.collection("Worker")
                    .document(toId)
                    .update("requests",FieldValue.arrayUnion(reqId))
                    .addOnSuccessListener {
                        db.collection("User")
                            .document(fromId)
                            .update("requests",FieldValue.arrayUnion(reqId))
                            .addOnSuccessListener {
                                Log.d(TAG, "sendWorkRequest: Request added")
                                isSent(true)
                            }
                            .addOnFailureListener {
                                isSent(false)
                            }
                    }.addOnFailureListener {
                        isSent(false)
                        Log.d(TAG, "sendWorkRequest: Request not sent")
                    }
            }.addOnFailureListener {
                isSent(false)
            }
    }

    fun getUserRequests(){

        db.collection("Request")
            .get()
            .addOnSuccessListener {
                val mutableList = mutableListOf<Request>()
                it.forEach { doc ->
                    val request = doc.toObject(Request::class.java)
                    if(request.from == userUid)
                    mutableList.add(request)
                }
                _requests.value = mutableList.toList()
            }.addOnFailureListener {
                Log.d(TAG, "getUserRequests: Failed to fetch requests")
            }

    }

    fun getAllJobs(){
        viewModelScope.launch {
            db.collection("Jobs")
                .get()
                .addOnSuccessListener {
                    val mutableList = mutableListOf<Job>()
                    it.forEach { doc ->
                        val job = doc.toObject(Job::class.java)
                        if(job.fromId == userUid)
                            mutableList.add(job)
                    }
                    _jobs.value = mutableList.toList()
                }.addOnFailureListener {
                    Log.d(TAG, "getAllJobs: failed")
                }
        }
    }

    fun postReview(review: Review,workerId: String,hasPosted: (Boolean) -> Unit){
        viewModelScope.launch {
            db.collection("Worker")
                .document(workerId)
                .collection("Review")
                .add(review)
                .addOnSuccessListener {
                    for (worker in allWorkers.value!!){
                        Log.d(TAG, "postReview: HEEREE")
                        if (worker.uid == workerId){
                            var raterCount = worker.ratersCount.toInt()
                            var rating = worker.rating.toDouble()
                            Log.d(TAG, "postReview:$workerId ${worker.uid} -- $raterCount $rating")
                            val x = (rating * raterCount + review.rating.toDouble())
                            raterCount++
                            rating = x/raterCount
                            val ratingStr = String.format("%.1f", rating)
                            Log.d(TAG, "postReview: $raterCount $rating")
                            db.collection("Worker")
                                .document(workerId)
                                .update(mapOf(
                                    "ratersCount" to raterCount.toString(),
                                    "rating" to ratingStr
                                )).addOnSuccessListener {
                                    hasPosted(true)
                                }.addOnFailureListener {
                                    hasPosted(true)
                                }
                        }
                    }
                }.addOnFailureListener {
                    hasPosted(true)
                }
        }
    }

    fun getSpecificWorkerPosts(
        workerId: String,
        posts: (List<Post>) -> Unit
    ){
        viewModelScope.launch {
            db.collection("Worker")
                .document(workerId)
                .collection("Posts")
                .get()
                .addOnSuccessListener {
                    val mutableList = mutableListOf<Post>()
                    it.forEach { doc ->
                        val post = doc.toObject(Post::class.java)
                        mutableList.add(post)
                    }
                    posts(mutableList.toList())
                }.addOnFailureListener {
                    posts(mutableListOf())
                }
        }
    }

    fun updateUserData(
        userName: String,
        contact: String,
        city: String,
        imageUri: Uri?,
        isSuccessful: (Boolean) -> Unit
    ){
        val map = mapOf(
            "city" to city,
            "phoneNumber" to contact,
            "userName" to userName
        )
        db.collection("User")
            .document(userUid!!)
            .update(map)
            .addOnSuccessListener {
                uploadProPic(imageUri,user.value!!.uid){
                    isSuccessful(true)
                }
            }.addOnFailureListener {
                isSuccessful(false)
                Log.d(TAG, "updateUserData: failed to update user data")
            }
    }

    private fun uploadProPic(uri: Uri?, userUid: String,isSuccessful: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (uri == null)
                return@launch
            val ref = storage.getReference("$userUid/profilePicture")
            ref.putFile(uri)
                .addOnSuccessListener {
                    Log.d(TAG, "uploadImage: Success")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "download url success")
                        db.collection("User")
                            .document(userUid)
                            .update("profile", it.toString())
                            .addOnSuccessListener {
                                isSuccessful(true)
                                Log.d(TAG, "image url stored in db successfully")
                            }.addOnFailureListener {
                                isSuccessful(false)
                                Log.d(TAG, "image url failed to store in db")
                            }
                    }.addOnFailureListener {
                        isSuccessful(false)
                        Log.d(TAG, "updateWorkerInfo: $it")
                    }
                }
                .addOnFailureListener {
                    isSuccessful(false)
                    Log.d(TAG, "updateWorkerInfo: $it")
                }
        }
    }

    fun logoutUser(isSuccessful: (Boolean) -> Unit){
        FirebaseAuth.getInstance().signOut()
        isSuccessful(true)
    }



}