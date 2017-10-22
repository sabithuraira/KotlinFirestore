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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_form.*
import java.util.HashMap
import android.content.Intent




class FormActivity : AppCompatActivity() {
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        submit_button.setOnClickListener {
            err_msg.visibility = View.GONE
            progress.visibility = View.VISIBLE

            val data = HashMap<String, Any>()
            data.put("first", fname.text.toString())
            data.put("last", lname.text.toString())
            data.put("born", born.text.toString())

            db.collection("members")
                .add(data)
                .addOnSuccessListener {
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    this.finish()
                }
                .addOnFailureListener {
                    err_msg.text = "Failed updated data"
                    err_msg.visibility = View.VISIBLE;
                }
        }

    }
}

