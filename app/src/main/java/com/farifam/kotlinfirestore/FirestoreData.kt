package com.farifam.kotlinfirestore

import android.content.ContentValues.TAG
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import com.google.firebase.firestore.DocumentReference
import android.support.annotation.NonNull
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.OnCompleteListener


/**
 * Created by sabithuraira on 10/21/17.
 */
class FirestoreData {

    private val db by lazy { FirebaseFirestore.getInstance() }

    fun saveData(member: Member) {
        val data = HashMap<String, Any>()
        data.put("first", member.first_name)
        data.put("last", member.last_name)
        data.put("born", member.born)

        db.collection("members")
                .add(data)
                .addOnSuccessListener { documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id) }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    fun loadDatas() : MutableList<Member>{

        Log.d(TAG, "masuk load datas")
        var list_member= mutableListOf<Member>()
        db.collection("members")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
//                            Log.d(TAG, "dapet" + document.id + " => " + document.data)
                            list_member.add(Member(document.id,document.get("first").toString(), document.get("last").toString(), document.get("born").toString()))
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
        return list_member
    }
}