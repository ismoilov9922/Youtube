package uz.pdp.youtube.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.pdp.youtube.R
import uz.pdp.youtube.databinding.ItemPlaylistBinding
import uz.pdp.youtube.models.playlist.Item

class PlaylistAdapter(var listener: OnItemClickListener) :
    ListAdapter<Item, PlaylistAdapter.Vh>(MyDiffUtill()) {

    inner class Vh(private var itemVideoListBinding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(itemVideoListBinding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(item: Item) {
            Picasso.get().load(item.snippet.thumbnails.medium.url)
                .placeholder(R.drawable.ic_youtube_placeholder)
                .error(R.drawable.ic_youtube_placeholder)
                .into(itemVideoListBinding.imageView)
            itemVideoListBinding.title.text = item.snippet.title
            itemVideoListBinding.desc.text = item.contentDetails.itemCount.toString()+" video"
            itemVideoListBinding.playlistCount.text = item.contentDetails.itemCount.toString()

            itemView.setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }


    class MyDiffUtill : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id.equals(newItem.id)
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }
}