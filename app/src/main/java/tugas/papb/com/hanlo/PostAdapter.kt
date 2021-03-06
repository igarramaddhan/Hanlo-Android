package tugas.papb.com.hanlo

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.post_list_item.view.*

class PostAdapter(val items : MutableList<Post>, val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.post_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = items.get(position).name
        val desc = items.get(position).description
        val uuid = items.get(position).uuid
        holder.name.text = name
        holder.desc.text = desc
        holder.itemView.card_view.setOnClickListener {
            val intent = Intent(context, PostDetailActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("desc", desc)
            intent.putExtra("uuid", uuid)
            context.startActivity(intent)
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val name = view.name_text_view
        val desc = view.desc_text_view
    }
}