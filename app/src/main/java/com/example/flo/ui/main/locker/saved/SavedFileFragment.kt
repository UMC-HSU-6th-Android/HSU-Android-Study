package com.example.flo.ui.main.locker.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSavedfileBinding

class SavedFileFragment:Fragment() {
    lateinit var binding : FragmentSavedfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedfileBinding.inflate(inflater,container,false)
        return binding.root
    }
}