package tugas.papb.com.hanlo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : AppCompatActivity() {
    private val comments: MutableList<Comment> = mutableListOf()
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var uuid: String
    lateinit var adapter : CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        val name = intent.extras["name"] as String
        val desc = intent.extras["desc"] as String
        uuid = intent.extras["uuid"] as String

        name_text_view.text = name
        desc_text_view.text = desc

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = name
        adapter = CommentAdapter(comments, this@PostDetailActivity)
        comments_rv.layoutManager = LinearLayoutManager(this@PostDetailActivity, LinearLayoutManager.VERTICAL, false)
        comments_rv.adapter = adapter
        comments_rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
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
                comments.clear()
                dataSnapshot.children.mapNotNullTo(comments) { it.getValue<Comment>(Comment::class.java) }
                comments.forEach {
                    Log.d("CHATS", it.comment)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadChat:onCancelled ${databaseError.toException()}")
            }
        }
        firebaseDatabase.reference.child("comments").orderByChild("postId").equalTo(uuid).addValueEventListener(menuListener)
    }

    fun send() {
        val comment = comment_edit_text.text.toString()
        if (!comment.isEmpty()) {
            val name = firebaseAuth.currentUser?.displayName as String
            val ref = firebaseDatabase.reference.child("comments")
            val key = ref.push().key as String
            ref.child(key).setValue(Comment(name, comment, key, uuid)).addOnCompleteListener {
                comment_edit_text.text.clear()
            }
        }
    }
}
