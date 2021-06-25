package uz.pdp.youtube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.pdp.youtube.R
import uz.pdp.youtube.databinding.ItemCommentBinding
import uz.pdp.youtube.models.getComment.Item

class CommentAdapter() :
    ListAdapter<Item, CommentAdapter.Vh>(MyDiffUtill()) {

    inner class Vh(var itemVideoListBinding: ItemCommentBinding) :
        RecyclerView.ViewHolder(itemVideoListBinding.root) {

        fun onBind(item: Item) {
            Picasso.get().load(item.snippet.topLevelComment.snippet.authorProfileImageUrl)
                .placeholder(R.drawable.ic_youtube_placeholder)
                .error(R.drawable.ic_youtube_placeholder)
                .into(itemVideoListBinding.commentImg)
            itemVideoListBinding.userName.text =
                item.snippet.topLevelComment.snippet.authorDisplayName
            itemVideoListBinding.desc.text = item.snippet.topLevelComment.snippet.textDisplay
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
}