package com.serioushyeon.floclone.ui.main.locker.savedalbum

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.serioushyeon.floclone.data.local.SongDatabase
import com.serioushyeon.floclone.databinding.FragmentSavedAlbumBinding
import com.serioushyeon.floclone.ui.main.album.AlbumLockerRVAdapter

class SavedAlbumFragment : Fragment() {
    lateinit var binding: FragmentSavedAlbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedAlbumBinding.inflate(inflater, container, false)

        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val albumRVAdapter = AlbumLockerRVAdapter()
        //리스너 객체 생성 및 전달

        albumRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                albumDB.albumDao().disLikeAlbum(getJwt(), songId)
                albumDB.albumDao().getLikedAlbums(getJwt())
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = albumRVAdapter

        albumRVAdapter.addAlbums(albumDB.albumDao().getLikedAlbums(getJwt()) as ArrayList)
    }

    private fun getJwt() : Int {
        val spf = activity?.getSharedPreferences("auth" , AppCompatActivity.MODE_PRIVATE)
        val jwt = spf!!.getInt("jwt", 0)
        Log.d("MAIN_ACT/GET_JWT", "jwt_token: $jwt")

        return jwt
    }
}