package tugas.papb.com.hanlo

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.MenuItem
import android.view.Window
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : AppCompatActivity() {
val firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        supportActionBar?.title = "Forget Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        email_edit_text.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val content = s?.toString()
                email_edit_text.error = if (Patterns.EMAIL_ADDRESS.matcher(content).matches()) null else "Valid email is required"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        send_button.setOnClickListener {
            send()
        }
    }

    fun send() {
        val email = email_edit_text.text.toString()
        if(email.isEmpty()){
            email_edit_text.error = "Email is required"
        }else{
            val dialog= Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.loading_indicator)
            dialog.show()
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                dialog.hide()
                if(it.isSuccessful){
                    Snackbar.make(activity_forget_password, "Request sent, please check your email",
                            Snackbar.LENGTH_LONG).show()
                }else{
                    Snackbar.make(activity_forget_password, it.exception?.message.toString(),
                            Snackbar.LENGTH_LONG).show()
                }
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
