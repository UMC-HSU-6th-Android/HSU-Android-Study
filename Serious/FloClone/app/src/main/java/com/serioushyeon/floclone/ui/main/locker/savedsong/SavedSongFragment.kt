package com.serioushyeon.floclone.ui.main.locker.savedsong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.serioushyeon.floclone.data.entities.Song
import com.serioushyeon.floclone.data.local.SongDatabase
import com.serioushyeon.floclone.databinding.FragmentSavedSongBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentSavedSongBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedSongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.saveSongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = SavedSongRVAdapter()

        songRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false,songId)
            }

        })

        binding.saveSongRv.adapter = songRVAdapter

        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }
}