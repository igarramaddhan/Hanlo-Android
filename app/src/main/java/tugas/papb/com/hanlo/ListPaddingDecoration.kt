package tugas.papb.com.hanlo

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ListPaddingDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {

        with(outRect) {
            if (parent.getChildAdapterPosition(view) != 0) {
                top = spaceHeight
            }
//            left =  spaceHeight
//            right = spaceHeight
        }
    }
}