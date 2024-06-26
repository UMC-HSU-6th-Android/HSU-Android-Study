package com.example.flo

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.util.Timer

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding 초기화
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
//        binding.songColorLikeIv.setOnClickListener {
//            setLike(songs[nowPos].isLike)
//        }

        binding.songUnlikeIv.setOnClickListener {
            setUnlikeStatus(false)
        }
        binding.songColorUnlikeIv.setOnClickListener {
            setUnlikeStatus(true)
        }

        var isRepeatEnabled = false // 초기에는 false로 설정합니다.

        binding.songRepeatIv.setOnClickListener {
            isRepeatEnabled = !isRepeatEnabled // 현재 상태를 반전시킵니다.

            if (isRepeatEnabled) {
                // Repeat가 활성화되었을 때 파란색으로 설정합니다.
                binding.songRepeatIv.setColorFilter(Color.parseColor("#000069"))
            } else {
                // Repeat가 비활성화되었을 때 회색으로 설정합니다.
                binding.songRepeatIv.setColorFilter(Color.parseColor("#323232"))
            }
        }
        binding.songRandomIv.setOnClickListener {
            isRepeatEnabled = !isRepeatEnabled // 현재 상태를 반전시킵니다.

            if (isRepeatEnabled) {
                // Repeat가 활성화되었을 때 파란색으로 설정합니다.
                binding.songRandomIv.setColorFilter(Color.parseColor("#000069"))
            } else {
                // Repeat가 비활성화되었을 때 회색으로 설정합니다.
                binding.songRandomIv.setColorFilter(Color.parseColor("#323232"))
            }
        }

        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }

        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID",songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun moveSong(direct: Int){
        if (nowPos + direct < 0){
            Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
            return
        }

        if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text=song.title
        binding.songSingerNameTv.text=song.singer
        binding.songStartTimeTv.text=String.format("%02d:%02d",song.second/60,song.second%60)
        binding.songEndTimeTv.text=String.format("%02d:%02d",song.playTime/60,song.playTime%60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress=(song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if (song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songPlayIv.visibility= View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else{
            binding.songPlayIv.visibility= View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    fun setLike(isLike : Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if (!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            Toast.makeText(this,"좋아요 한 곡에 담겼습니다.", Toast.LENGTH_SHORT).show()
        } else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            Toast.makeText(this,"좋아요 한 곡이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun setUnlikeStatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songUnlikeIv.visibility = View.VISIBLE
            binding.songColorUnlikeIv.visibility = View.GONE
        }
        else{
            binding.songUnlikeIv.visibility = View.GONE
            binding.songColorUnlikeIv.visibility = View.VISIBLE
        }
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime,songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean= true):Thread(){
        private var second : Int = 0
        private var mills : Float = 0f

        override fun run() {
            super.run()
            try{
                while (true){

                    if(second >= playTime){
                        break
                    }

                    if(isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread{
                            binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                            saveProgress(mills.toInt())
                        }
                        if(mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text=String.format("%02d:%02d",second/60,second%60)
                            }
                            second++
                        }
                    }
                }

            }catch (e: InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }

    private fun saveProgress(progress: Int) {
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("progress", progress)
        editor.apply()
    }

    // 사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second = ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // 에디터

        editor.putInt("songId", songs[nowPos].id)
        editor.putInt("progress", songs[nowPos].second * 1000)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }


}