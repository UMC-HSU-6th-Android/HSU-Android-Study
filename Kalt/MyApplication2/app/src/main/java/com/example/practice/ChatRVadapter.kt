package com.example.practice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice.databinding.ItemAlbumBinding

class ChatRVadapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<ChatRVadapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRVadapter.Viewholder {
        val binding: ItemAlbumBinding =
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: ChatRVadapter.Viewholder, position: Int) {
        holder.bind(albumList[position])
    }

    override fun getItemCount(): Int = albumList.size
inner class Viewholder(val binding : ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverimg!!)

    }
}

}