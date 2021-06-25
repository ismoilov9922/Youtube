package uz.pdp.youtube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.pdp.youtube.R
import uz.pdp.youtube.database.VideoHome
import uz.pdp.youtube.databinding.ItemVideoBinding

class HomeRoomAdapter(val listener: OnItemClickListener) :
    ListAdapter<VideoHome, HomeRoomAdapter.Vh>(MyDiffUtill()) {

    inner class Vh(var itemVideoListBinding: ItemVideoBinding) :
        RecyclerView.ViewHolder(itemVideoListBinding.root) {

        fun onBind(item: VideoHome) {
            Picasso.get().load(item.image)
                .placeholder(R.drawable.ic_youtube_placeholder)
                .error(R.drawable.ic_youtube_placeholder)
                .into(itemVideoListBinding.imageView)
            itemVideoListBinding.textView.text = item.title
            itemVideoListBinding.textView2.text = item.description
            itemVideoListBinding.imageView.setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }


    class MyDiffUtill : DiffUtil.ItemCallback<VideoHome>() {
        override fun areItemsTheSame(oldItem: VideoHome, newItem: VideoHome): Boolean {
            return oldItem.videoId == newItem.videoId
        }

        override fun areContentsTheSame(oldItem: VideoHome, newItem: VideoHome): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: VideoHome)
    }
}