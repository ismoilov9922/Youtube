package uz.pdp.youtube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.pdp.youtube.R
import uz.pdp.youtube.database.VideoHome1
import uz.pdp.youtube.databinding.ItemVideoBinding

class RvAdapter(private val list: List<VideoHome1>, val listener: OnItemClickListener) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(private val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(videoHome: VideoHome1) {
            Picasso.get().load(videoHome.image)
                .placeholder(R.drawable.ic_youtube_placeholder)
                .error(R.drawable.ic_youtube_placeholder)
                .into(binding.imageView)
            binding.textView.text = videoHome.title
            binding.textView2.text = videoHome.description
            binding.imageView.setOnClickListener {
                listener.onItemClick(videoHome)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}

interface OnItemClickListener {
    fun onItemClick(item: VideoHome1)
}