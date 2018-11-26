package tugas.papb.com.hanlo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_post.*

class AddPostActivity : AppCompatActivity() {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add New Post"
        submit_button.setOnClickListener {
            val content = post_edit_text.text.toString()
            if(!content.isEmpty()){
                submitPost(content)
            }
        }
    }

    fun submitPost(content: String){
        val currentUser = firebaseAuth.currentUser
        val postRef = firebaseDatabase.reference.child("posts")
        val key = postRef.push().key.toString()
        postRef.child(key).setValue(Post(currentUser?.displayName.toString(), content, key)).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("ADD_POST", "SUCCESS")
                finish()
            }
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
}
