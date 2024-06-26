package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice.databinding.FragmentChatBinding
import com.google.android.material.tabs.TabLayout

class chatfragment : Fragment() {
    lateinit var binding : FragmentChatBinding
    private lateinit var songDB : SongDatabase
    private var albumdatas = ArrayList<Album>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        albumdatas.addAll(songDB.albumDao().getAlbums()) // songDB에서 album list를 가져옵니다.
        val adpater = ChatRVadapter(albumdatas)
        binding.chatrecycler.adapter = adpater
        binding.chatrecycler.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

}
