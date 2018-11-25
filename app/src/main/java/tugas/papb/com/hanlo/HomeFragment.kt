package tugas.papb.com.hanlo

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.function.Consumer

class HomeFragment : Fragment() {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var post_list: RecyclerView
    private val posts: MutableList<Post> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        loadData()
        post_list = rootView.findViewById(R.id.post_rv)
        post_list.layoutManager = LinearLayoutManager(context)
        post_list.adapter = PostAdapter(posts, requireContext())
        post_list.addItemDecoration(ListPaddingDecoration(
                resources.getDimension(R.dimen.default_padding).toInt()))
         rootView.findViewById<FloatingActionButton>(R.id.add_post_fab).setOnClickListener {
            val intent = Intent(context, AddPostActivity::class.java)
            container?.context?.startActivity(intent)
        }
        return rootView
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }



    fun loadData(){
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                posts.clear()
                dataSnapshot.children.mapNotNullTo(posts) { it.getValue<Post>(Post::class.java) }
                dataSnapshot.children.forEach {
                    Log.d("POST", it.getValue<Post>(Post::class.java)?.name)
                }
                post_list.adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        firebaseDatabase.reference.child("posts").addListenerForSingleValueEvent(menuListener)
    }

    fun loadDatabase(data: DatabaseReference) {
        val post: List<Post> = mutableListOf(
                Post("Igar Ramaddhan", "Test First Post"),
                Post("Igar Ramaddhan", "Test Second Post")
        )
        post.forEach {
            val key: String = data.push().key.toString()
            it.uuid = key
            data.child(key).setValue(it)
            Log.d("POST", key)
        }
    }
}


