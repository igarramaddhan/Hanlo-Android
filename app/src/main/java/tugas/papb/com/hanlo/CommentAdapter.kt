package tugas.papb.com.hanlo

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.chat_list_item.view.*
import kotlinx.android.synthetic.main.room_list_item.view.*

class CommentAdapter(val items: MutableList<Comment>, val context: Context) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = items.get(position).name
        val comment = items.get(position).comment
        holder.name.text = name
        holder.message.text = comment
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val name = view.chat_name_text_view
        val message = view.chat_message_text_view
    }
}