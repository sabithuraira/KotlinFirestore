package com.farifam.kotlinfirestore

import android.annotation.TargetApi
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.ContentValues

import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.opengl.Visibility
import android.os.AsyncTask

import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_form.*
import android.content.Intent
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*



class FormActivity : AppCompatActivity() {
    val settings: FirebaseFirestoreSettings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
    private val db by lazy { FirebaseFirestore.getInstance() }
    var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        db.setFirestoreSettings(settings);

        if (intent.hasExtra("id"))
            id = intent.getStringExtra("id")


        if(id.length!=0){
            initializeUpdate()
        }

        submit_button.setOnClickListener {
            processSubmit()
        }
    }

    fun processSubmit(){

        err_msg.visibility = View.GONE
        progress.visibility = View.VISIBLE

        val data = HashMap<String, Any>()
        data.put("first", fname.text.toString())
        data.put("last", lname.text.toString())
        data.put("born", born.text.toString())

        if(id.length==0){

//                 THIS ONLY WORK FOR ONLINE MODE
            db.collection("members")
                    .add(data)
                    .addOnSuccessListener {
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        this@FormActivity.finish()
                    }
                    .addOnFailureListener {
                        err_msg.text = "Failed updated data"
                        err_msg.visibility = View.VISIBLE;
                    }

//                db.collection("members")
//                        .addSnapshotListener(object : EventListener<QuerySnapshot> {
//                            override fun onEvent(querySnapshot: QuerySnapshot?,
//                                                 e: FirebaseFirestoreException?) {
//                                if (e != null) {
//                                    Log.w(ContentValues.TAG, "Listen error", e)
//                                    return
//                                }
//
//                                querySnapshot?.
//
//
//                                docSnapshot?.reference?.update(data)
//                                        ?.addOnSuccessListener {
//                                            val intent = Intent()
//                                            setResult(Activity.RESULT_OK, intent)
//                                            this@FormActivity.finish()
//                                        }
//                                        ?.addOnFailureListener{
//                                            err_msg.text = "Failed updated data"
//                                            err_msg.visibility = View.VISIBLE;
//                                        }
//
//                            }
//                        })
        }
        else{
            // THIS ONLY WORK FOR ONLINE MODE

//                db.collection("members").document(id)
//                        .update(data)
//                        .addOnSuccessListener{
//                            val intent = Intent()
//                            setResult(Activity.RESULT_OK, intent)
//                            this.finish()
//                        }
//                        .addOnFailureListener{
//                            err_msg.text = "Failed updated data"
//                            err_msg.visibility = View.VISIBLE;
//                        }

            db.collection("members").document(id)
                .addSnapshotListener(object : EventListener<DocumentSnapshot> {
                    override fun onEvent(snapshot: DocumentSnapshot?,
                                         e: FirebaseFirestoreException?) {
                        if (e != null) {
                            Log.w(ContentValues.TAG, "Listen error", e)
                            err_msg.text = e.message
                            err_msg.visibility = View.VISIBLE;
                            return
                        }

//                        if (snapshot != null && snapshot.exists()) {
                        snapshot?.reference?.update(data)

                                        val intent = Intent()
                                        setResult(Activity.RESULT_OK, intent)
                                        this@FormActivity.finish()

//                                    ?.addOnSuccessListener {
//                                        val intent = Intent()
//                                        setResult(Activity.RESULT_OK, intent)
//                                        this@FormActivity.finish()
//                                    }
//                                    ?.addOnFailureListener{
//                                        err_msg.text = "Failed updated data"
//                                        err_msg.visibility = View.VISIBLE;
//                                    }
//                        } else {
//                            err_msg.text = "Failed updated data"
//                            err_msg.visibility = View.VISIBLE;
//                        }
                    }
                })
        }
    }

    fun initializeUpdate(){
        fname.setText(intent.getStringExtra("first_name"))
        lname.setText(intent.getStringExtra("last_name"))
        born.setText(intent.getStringExtra("born"))
    }
}

