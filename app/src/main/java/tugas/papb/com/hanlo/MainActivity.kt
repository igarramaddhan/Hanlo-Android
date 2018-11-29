package tugas.papb.com.hanlo


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.support.design.widget.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation_view.setOnNavigationItemSelectedListener {
            navigationOnSelectItem(it)
        }
        openFragment( HomeFragment.newInstance())
        supportActionBar?.title = "Home"
    }

    fun navigationOnSelectItem(item: MenuItem): Boolean{
        return when (item.itemId) {
            R.id.navigation_home -> {
                supportActionBar?.title = "Home"
                val homeFragment = HomeFragment.newInstance()
                openFragment(homeFragment)
                true
            }
            R.id.navigation_chat -> {
                supportActionBar?.title = "Chat"
                val chatFragment = ChatFragment.newInstance()
                openFragment(chatFragment)
                true
            }
            R.id.navigation_profile -> {
                supportActionBar?.title = "Profile"
                val profileFragment = ProfileFragment.newInstance()
                openFragment(profileFragment)
                true
            }
            else -> {
                false
            }
        }
    }


    private fun openFragment(fragment: android.support.v4.app.Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_layout, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }
}
