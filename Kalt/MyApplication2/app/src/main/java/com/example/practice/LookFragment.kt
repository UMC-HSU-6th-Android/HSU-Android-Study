package com.example.practice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.practice.databinding.ActivityHomeFragmentBinding
import com.example.practice.databinding.ActivityLookFragmentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LookFragment : Fragment() {
    lateinit var binding : ActivityLookFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityLookFragmentBinding.inflate(inflater,container,false)
        val adapter = lookVpAdapter(this)
        binding.lockerViewpager.adapter = adapter
        TabLayoutMediator(binding.lookTab, binding.lockerViewpager){ tab,position->
            when(position){
                0-> tab.text = "차트"
                1-> tab.text = "영상"
                else-> tab.text = "장르"
            }
        }.attach()
        setTabItemMargin(binding.lookTab, 50)
        return binding.root
    }
}
private fun setTabItemMargin(tabLayout: TabLayout, marginEnd: Int = 20) {
    for (i in 0 until 3) {
        val tabs = tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabs.childCount) {
            val tab = tabs.getChildAt(i)
            val lp = tab.layoutParams as LinearLayout.LayoutParams
            lp.marginEnd = marginEnd
            tab.layoutParams = lp
            tabLayout.requestLayout()
        }
    }
}