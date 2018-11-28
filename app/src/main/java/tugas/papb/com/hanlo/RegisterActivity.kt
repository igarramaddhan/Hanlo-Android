package tugas.papb.com.hanlo

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.MenuItem
import android.view.Window
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Register"

        register_button.setOnClickListener {
            val  email = email_edit_text.text.toString()
            val password = password_edit_text.text.toString()
            val displayName = name_edit_text.text.toString()
            register(email, password, displayName)
        }
    }

    fun register(email: String, password: String, displayName: String) {
        val dialog= Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loading_indicator)
        dialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                dialog.hide()
                val updateProfile = UserProfileChangeRequest.Builder().setDisplayName(displayName).build()
                firebaseAuth.currentUser?.updateProfile(updateProfile)?.addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }else{
                dialog.hide()
                Snackbar.make(register_layout, "Register Failed",
                        Snackbar.LENGTH_SHORT).show()
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
