package tugas.papb.com.hanlo

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            val email = email_edit_text.text.toString()
            val password = password_edit_text.text.toString()
            login(email, password)
        }

        register_button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        activity_login.setOnClickListener {
            email_edit_text.clearFocus()
            password_edit_text.clearFocus()
            hideKeyboard(it)
        }

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

        login_button.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                hideKeyboard(v)
            }
        }
    }

    fun login(email: String, password: String) {
        if(email.isEmpty()){
            email_edit_text.error = "Email is required"
        }else if(password.isEmpty()){
            password_edit_text.error = "Password is required"
        }else{
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Snackbar.make(activity_login, "Login Failed",
                            Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun hideKeyboard(view: View){
        val inputManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
