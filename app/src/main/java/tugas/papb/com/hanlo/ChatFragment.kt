package tugas.papb.com.hanlo

import android.app.AlertDialog
import android.opengl.Visibility
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    lateinit var room_list: RecyclerView
    private val rooms: MutableList<Room> = mutableListOf()
    val currentUser = firebaseAuth.currentUser
    val uid = currentUser?.uid.toString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_chat, container, false)
        loadData(rootView)
        rootView.findViewById<FloatingActionButton>(R.id.new_room_fab).setOnClickListener {
            showCreateCategoryDialog(rootView)
        }
        room_list = rootView.findViewById(R.id.room_rv)
        room_list.layoutManager = LinearLayoutManager(context)
        room_list.adapter = RoomAdapter(rooms, requireContext())
        room_list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        return rootView
    }

    companion object {
        fun newInstance() = ChatFragment()
    }

    fun showCreateCategoryDialog(rootView: View) {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        builder.setTitle("New/Join Room")

        val view = layoutInflater.inflate(R.layout.dialog_new_room, null)

        val roomEditText = view.findViewById(R.id.new_room_edit_text) as EditText

        builder.setView(view)

        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            val newCategory = roomEditText.text
            var isValid = true
            if (newCategory.isBlank()) {
                roomEditText.error = "Room name should not be empty"
                isValid = false
            }

            if (isValid) {
                addRoom(roomEditText.text.toString())
                dialog.dismiss()
            }
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
            Snackbar.make(rootView, "Canceled", Snackbar.LENGTH_SHORT).show()
        }

        builder.show()
    }

    fun addRoom(name: String) {
        val ref = firebaseDatabase.reference.child("rooms")

        ref.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot.children.count() > 0) {
                            var key:String
                            dataSnapshot.children.forEach {
                                val uuid = it.child("uuid").value.toString()
                                val chats = mutableListOf<Chat>()
                                val member = mutableListOf<String>()
                                val chatRef = it.child("chats")
                                val memberRef = it.child("member")
                                chatRef.children.mapNotNullTo(chats) { it.getValue(Chat::class.java) }
                                memberRef.children.mapNotNullTo(member) {it.getValue(String::class.java)}

                                val room = Room(name, uuid, member, chats)
                                key = room.uuid
                                val childUpdates: MutableList<String> = mutableListOf()
                                room.member.mapTo(childUpdates){
                                    it
                                }
                                childUpdates.add(uid)
                                ref.child(key).child("member").setValue(childUpdates.distinct())
                            }

                        }else{
                            Log.d("ROOM", "Room Not Found")
                            val key = ref.push().key.toString()
                            ref.child(key).setValue(Room(name, key, mutableListOf(uid), mutableListOf()))
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        println("loadPost:onCancelled ${databaseError.toException()}")
                    }
                }
        )
    }

    fun loadData(view: View){
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                rooms.clear()
                dataSnapshot.children.mapNotNullTo(rooms){
                    val uuid = it.child("uuid").value.toString()
                    val name = it.child("name").value.toString()
                    val chats = mutableListOf<Chat>()
                    val member = mutableListOf<String>()

                    val chatRef = it.child("chats")
                    val memberRef = it.child("member")
                    chatRef.children.mapNotNullTo(chats) { it.getValue(Chat::class.java) }
                    memberRef.children.mapNotNullTo(member) {it.getValue(String::class.java)}
                    if(member.contains(uid)) {
                        Room(name, uuid, member, chats)
                    }
                    else{
                        null
                    }
                }
                view.progress_bar.visibility = RelativeLayout.GONE
                room_list.adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadRoom:onCancelled ${databaseError.toException()}")
            }
        }
        firebaseDatabase.reference.child("rooms").addValueEventListener(menuListener)
    }
}
