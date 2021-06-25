package uz.pdp.youtube.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.pdp.youtube.databinding.ItemCommentBinding

class SimpleAdapter(val list: ArrayList<String>) : RecyclerView.Adapter<SimpleAdapter.Vh>() {


    inner class Vh(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(search: String) {
            binding.commentImg.visibility = View.GONE
            binding.desc.visibility = View.GONE
            binding.userName.text = search
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}