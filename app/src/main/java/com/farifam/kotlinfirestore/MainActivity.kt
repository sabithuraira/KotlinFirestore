package com.farifam.kotlinfirestore

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import com.google.firebase.firestore.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener

import com.farifam.kotlinfirestore.DataAdapter



class MainActivity : AppCompatActivity() {

    val settings: FirebaseFirestoreSettings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();

    private val db by lazy { FirebaseFirestore.getInstance() }
    var list_member= mutableListOf<Member>()
    var dataAdapter : DataAdapter? = null

    private val FORM_ACTIVITY_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        db.setFirestoreSettings(settings);

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


        search.setOnEditorActionListener() { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                loadFirestoreDatas()
                true
            }
            false
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

        val memberCollection = db.collection("members");
        var query : Query = memberCollection.orderBy("first")

        if(search.text.toString().length>0){
            query = memberCollection.whereEqualTo("first", search.text.toString())
        }

//        query.get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    list_member.clear()
//                    for (document in task.result) {
//                        var cur_data: Member = document.toObject(Member::class.java)
//                        cur_data.id = document.id
//                        list_member.add(cur_data)
//                    }
//
//                    dataAdapter = DataAdapter(ArrayList(list_member), applicationContext)
//                    listview.setAdapter(dataAdapter)
//
//                    progress.visibility = View.GONE
//                } else {
//                    Log.d(ContentValues.TAG, "Error getting documents: ", task.exception)
//                }
//            }

        query
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(querySnapshot: QuerySnapshot?,
                                     e: FirebaseFirestoreException?) {
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen error", e)
                        return
                    }

                    for (change in querySnapshot!!.documentChanges) {

                        when (change.type) {
                            DocumentChange.Type.ADDED -> onDocumentAdded(change)
                            DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                            DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
                        }
                    }

                    dataAdapter = DataAdapter(ArrayList(list_member), applicationContext)
                    listview.setAdapter(dataAdapter)

                    progress.visibility = View.GONE
                }
            })
    }

    fun onDocumentAdded(change: DocumentChange){
        var cur_data: Member = change.document.toObject(Member::class.java)
        cur_data.id = change.document.id

        if(!list_member.any { x -> x.id == change.document.id })
            list_member.add(cur_data)
    }

    fun onDocumentModified(change: DocumentChange){
        var cur_data: Member = change.document.toObject(Member::class.java)
        cur_data.id = change.document.id
        if (change.getOldIndex() == change.getNewIndex()) {
            list_member.set(change.getOldIndex(),  cur_data);
        } else {
            list_member.removeAt(change.getOldIndex());
            list_member.add(change.getNewIndex(), cur_data);
        }
    }

    fun onDocumentRemoved(change: DocumentChange){
        list_member.removeAt(change.getOldIndex());
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
