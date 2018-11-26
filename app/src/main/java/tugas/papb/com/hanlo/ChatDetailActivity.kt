package tugas.papb.com.hanlo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chat_detail.*
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatDetailActivity : AppCompatActivity() {
    private val chats: MutableList<Chat> = mutableListOf()
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var uuid: String
    lateinit var adapter : ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)
        val name = intent.extras["room_name"] as String
        uuid = intent.extras["uuid"] as String
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = name
        adapter = ChatAdapter(chats, this@ChatDetailActivity)
        chats_rv.layoutManager = LinearLayoutManager(this@ChatDetailActivity, LinearLayoutManager.VERTICAL, false)
        chats_rv.adapter = adapter
        chats_rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        loadData()

        send_button.setOnClickListener {
            send()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun loadData() {
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chats.clear()
                dataSnapshot.children.mapNotNullTo(chats) { it.getValue<Chat>(Chat::class.java) }
                chats.forEach {
                    Log.d("CHATS", it.message)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadChat:onCancelled ${databaseError.toException()}")
            }
        }
        firebaseDatabase.reference.child("rooms").child(uuid).child("chats").addValueEventListener(menuListener)
    }

    fun send() {
        val message = message_edit_text.text.toString()
        if (!message.isEmpty()) {
            val name = firebaseAuth.currentUser?.displayName as String
            val ref = firebaseDatabase.reference.child("rooms").child(uuid).child("chats")
            val key = ref.push().key as String
            ref.child(key).setValue(Chat(name, message, key)).addOnCompleteListener {
                message_edit_text.text.clear()
            }
        }
    }
}
