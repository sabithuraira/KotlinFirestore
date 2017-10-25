package com.farifam.kotlinfirestore

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Menu
import android.view.MenuItem

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.*

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

        val actions = listOf("Update", "Delete")
        listview.setOnItemClickListener { parent, view, position, id ->
            selector(null, actions, { dialogInterface, i ->
                if(i==0) updateData(position)
                else deleteData(position)
            })
        }
    }

    fun deleteData(position: Int){
        db.collection("members").document(list_member[position].id)
                .delete()
                .addOnSuccessListener{
                    loadFirestoreDatas()
                }
                .addOnFailureListener{
                    toast("Failed to delete data, please check your connection")
                }
    }

    fun updateData(position: Int){
        val intent = Intent(this, FormActivity::class.java)
        intent.putExtra("id", list_member[position].id)
        intent.putExtra("first_name", list_member[position].first)
        intent.putExtra("last_name", list_member[position].last)
        intent.putExtra("born", list_member[position].born)
        startActivityForResult(intent, FORM_ACTIVITY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

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
//                        list_member.add(Member(document.id,document.get("first").toString(), document.get("last").toString(), document.get("born").toString()))
                        list_member.add(document.toObject(Member::class.java))
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
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
}
