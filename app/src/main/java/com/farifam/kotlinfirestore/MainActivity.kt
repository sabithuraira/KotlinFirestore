package com.farifam.kotlinfirestore

import android.app.Activity
import android.app.Application
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.Menu
import android.view.MenuItem

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.widget.TextView



class MainActivity : AppCompatActivity() {

    private val db by lazy { FirebaseFirestore.getInstance() }
    var list_member= mutableListOf<Member>()
    var dataAdapter : DataAdapter? = null

    private val FORM_ACTIVITY_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val intent = Intent(this, FormActivity::class.java)
            startActivityForResult(intent, FORM_ACTIVITY_CODE);
        }

        loadFirestoreDatas()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // check that it is the SecondActivity with an OK result
        if (requestCode == FORM_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                loadFirestoreDatas()
            }
        }
    }

    fun loadFirestoreDatas(){
        progress.visibility = View.VISIBLE
        db.collection("members")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    list_member.clear()
                    for (document in task.result) {
                        Log.d(ContentValues.TAG, "dapet" + document.id + " => " + document.data)
                        list_member.add(Member(document.get("first").toString(), document.get("last").toString(), document.get("born").toString()))
                    }

                    dataAdapter = DataAdapter(ArrayList(list_member), applicationContext)
                    listview.setAdapter(dataAdapter)

                    progress.visibility = View.GONE
                } else {
                    Log.w(ContentValues.TAG, "Error getting documents.", task.exception)
                    progress.visibility = View.GONE
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
}
