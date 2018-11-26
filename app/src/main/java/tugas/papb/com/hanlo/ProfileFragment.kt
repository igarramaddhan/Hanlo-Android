package tugas.papb.com.hanlo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {
    val firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val user = FirebaseAuth.getInstance().currentUser
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        rootView.display_name_edit_text.setText(user?.displayName)
        rootView.email_edit_text.setText(user?.email)
        rootView.edit_button.setOnClickListener {
            edit(rootView)
        }
        rootView.save_button.setOnClickListener {
            save(rootView)
        }
        rootView.logout_button.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Logout")
            builder.setMessage("Are you sure want to logout?")
            builder.setPositiveButton("YES"){ dialog, which ->
                firebaseAuth.signOut()
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            builder.setNegativeButton("NO"){ dialog, which ->
                Snackbar.make(rootView, "Logout canceled", Snackbar.LENGTH_SHORT).show()
            }
            builder.create().show()
        }
        return rootView
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }

    fun edit(view: View) {
        view.edit_button.visibility = LinearLayout.GONE
        view.save_button.visibility = LinearLayout.VISIBLE
        view.display_name_edit_text.isEnabled = true
        view.email_edit_text.isEnabled = true
    }

    fun save(view: View) {
        val user = firebaseAuth.currentUser

        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(view.display_name_edit_text.text.toString())
                .build()
        user?.updateProfile(profileUpdates)?.addOnCompleteListener {
            user.updateEmail(view.email_edit_text.text.toString()).addOnCompleteListener {
                view.edit_button.visibility = LinearLayout.VISIBLE
                view.save_button.visibility = LinearLayout.GONE
                view.display_name_edit_text.isEnabled = false
                view.email_edit_text.isEnabled = false
                Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show()

            }
        }

    }
}
