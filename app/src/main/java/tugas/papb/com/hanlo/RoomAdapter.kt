package tugas.papb.com.hanlo

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.room_list_item.view.*

class RoomAdapter(val items : MutableList<Room>, val context: Context) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.room_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = items.get(position).name
        val uuid = items.get(position).uuid
        holder.name.text = name
        holder.itemView.room_list_item.setOnClickListener {
            val intent = Intent(context, ChatDetailActivity::class.java)
            intent.putExtra("room_name", name)
            intent.putExtra("uuid", uuid)
            context.startActivity(intent)
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val name = view.room_name_text_view
    }
}