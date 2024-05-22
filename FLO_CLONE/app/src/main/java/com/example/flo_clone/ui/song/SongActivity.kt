package com.example.flo_clone.ui.song

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.flo_clone.MainActivity
import com.example.flo_clone.R
import com.example.flo_clone.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySongBinding
    private lateinit var song: Song
    private lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        initSong()
        setPlayer(song)

        setPlayerStatus(isPlaying = false)
    }

    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song) {
        binding.songTitleTv.text = intent.getStringExtra("title")!!
        binding.singerTitleTv.text = intent.getStringExtra("singer")!!
        binding.startTimerTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.endTimerTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        setPlayerStatus(song.isPlaying)
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    // 뒤로가기 버튼 쓰면 호출되는 함수
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivity.STRING_INTENT_KEY, binding.songTitleTv.text.toString())
        }
        setResult(Activity.RESULT_OK, intent)
        //intent.putExtra("title", binding.songTitleTv.text.toString())
        // 이동할 때 액티비티를 스택에서 제거
        //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //startActivity(intent)
    }

    private fun setButton() {

        // 재생/정지
        var isPlaying = true // 초기 상태 설정
        binding.nuguBtnDownIb.setOnClickListener{
            finish()
        }
        binding.nuguBtnPlayIb.setOnClickListener {
            isPlaying = !isPlaying // 클릭할 때마다 상태 변경
            setPlayerStatus(isPlaying) // 변경된 상태에 따라 이미지 변경
        }

        // 반복 재생
        var isRepeat = false
        binding.nuguBtnRepeatInactiveIb.setOnClickListener{
            isRepeat = !isRepeat
            if (!isRepeat) {
                val color = ContextCompat.getColor(this, R.color.gray_color)
                binding.nuguBtnRepeatInactiveIb.setColorFilter(color)
                song.isRepeating = false
            } else {
                val color = ContextCompat.getColor(this, R.color.blue)
                binding.nuguBtnRepeatInactiveIb.setColorFilter(color)
                song.isRepeating = true
            }
        }

        // 셔플
        var isRandom = false
        binding.nuguBtnRandomInactiveIb.setOnClickListener{
            isRandom = !isRandom
            if (!isRandom) {
                val color = ContextCompat.getColor(this, R.color.gray_color)
                binding.nuguBtnRandomInactiveIb.setColorFilter(color)
            } else {
                val color = ContextCompat.getColor(this, R.color.blue)
                binding.nuguBtnRandomInactiveIb.setColorFilter(color)
            }
        }

        // 좋아요(찜하기)
        var isLike = true
        binding.songLikeIv.setOnClickListener{
            isLike = !isLike
            if (isLike) {
                binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            } else {
                binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            }
        }

        // 블랙리스트?
        var isUnLike = true
        binding.songUnlikeIv.setOnClickListener{
            isUnLike = !isUnLike
            if (isUnLike) {
                binding.songUnlikeIv.setImageResource(R.drawable.btn_player_unlike_on)
            } else {
                binding.songUnlikeIv.setImageResource(R.drawable.btn_player_unlike_off)
            }
        }
    }

    // 재생/정지 버튼 클릭시 호출되는 함수 -> 클릭시 이미지 변경, 음악 재생 및 정지
    private fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (!isPlaying) {
            binding.nuguBtnPlayIb.setImageResource(R.drawable.nugu_btn_play_32)
            if(mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } else {
            binding.nuguBtnPlayIb.setImageResource(R.drawable.nugu_btn_pause_32)
            mediaPlayer?.start()
        }
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread() {

        private var second: Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()

            try {
                while (true) {

                    if (second >= playTime) {
                        if (song.isRepeating) {
                            mills = 0f
                            second = 0
                            mediaPlayer?.seekTo(0)
                            mediaPlayer?.start()
                        } else {
                            mediaPlayer?.pause()
                            break
                        }
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                        }
                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.startTimerTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e:InterruptedException) {
                Log.d("Song", "스레드가 죽었습니다. ${e.message}")
            }

        }
    }

    // 사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/1000

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // 에디터
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }
}