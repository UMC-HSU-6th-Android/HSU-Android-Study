package com.example.floClone.core.ui.vp

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.SavedAlbumFragment
import com.example.floClone.feature.main.locker.musicfile.MusicFileFragment
import com.example.floClone.feature.main.locker.savedsongs.SavedSongsFragment

class LockerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SavedSongsFragment()
            1 -> MusicFileFragment()
            else -> SavedAlbumFragment()
        }
    }
}