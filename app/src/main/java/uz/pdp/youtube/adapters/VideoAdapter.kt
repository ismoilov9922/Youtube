package uz.pdp.youtube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.pdp.youtube.R
import uz.pdp.youtube.databinding.ItemVideoBinding
import uz.pdp.youtube.models.search.Item

class VideoAdapter(var listener: OnItemClickListener) :
    ListAdapter<Item, VideoAdapter.Vh>(MyDiffUtill()) {

    inner class Vh(private var itemVideoListBinding: ItemVideoBinding) :
        RecyclerView.ViewHolder(itemVideoListBinding.root) {

        fun onBind(item: Item) {
            Picasso.get().load(item.snippet.thumbnails.medium.url)
                .placeholder(R.drawable.ic_youtube_placeholder)
                .error(R.drawable.ic_youtube_placeholder)
                .into(itemVideoListBinding.imageView)
            itemVideoListBinding.textView.text = item.snippet.title
            itemVideoListBinding.textView2.text = item.snippet.description
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


    class MyDiffUtill : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }
}