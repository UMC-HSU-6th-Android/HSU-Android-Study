package com.example.floClone.feature.main.locker.musicfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.floClone.databinding.FragmentMusicFileBinding

class MusicFileFragment : Fragment() {
    lateinit var binding: FragmentMusicFileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicFileBinding.inflate(layoutInflater)

        return binding.root
    }


}