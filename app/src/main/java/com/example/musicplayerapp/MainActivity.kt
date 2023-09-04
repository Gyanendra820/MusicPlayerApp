package com.example.musicplayerapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayerapp.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var oneTimeOnly: Int = 0
    private var startTime: Double = 0.0
    private var finalTime: Double = 0.0
    private var forwardTime = 1000
    private var backwardTime = 1000
    private val handler = Handler()
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMediaPlayer()
    }

    private fun setMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.my_music)

        binding.musicProgress.isClickable = false


        //Adding functionality for the buttons
        binding.playBtn.setOnClickListener {
            mediaPlayer!!.start()

            finalTime = mediaPlayer!!.duration.toDouble()
            startTime = mediaPlayer!!.currentPosition.toDouble()

            if (oneTimeOnly == 0) {
                binding.musicProgress.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            binding.songTimeTv.text = startTime.toString()
            binding.musicProgress.progress = startTime.toInt()

            handler.postDelayed(UpdateSongTime, 100)

        }

        //Setting the Music Title

        binding.songTitleTv.text = "" + resources.getResourceEntryName(R.raw.my_music)

        //Stop Button
        binding.pauseBtn.setOnClickListener {
            mediaPlayer!!.pause()
        }

        //forward function
        binding.forwardBtn.setOnClickListener {
            var temp = startTime
            if ((temp + forwardTime) <= finalTime) {
                startTime = startTime + forwardTime
                mediaPlayer!!.seekTo(startTime.toInt())
            } else {
                Toast.makeText(applicationContext, "Can't forward", Toast.LENGTH_SHORT).show()
            }
        }

        //Back Button

        binding.backBtn.setOnClickListener {
            var temp = startTime.toInt()
            if (temp - backwardTime > 0){
                startTime = startTime - backwardTime
                mediaPlayer!!.seekTo(startTime.toInt())
            } else {
                Toast.makeText(applicationContext, "Can't forward", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val UpdateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer!!.currentPosition.toDouble()
            binding.songTimeTv.text = "" + String.format(
                "%d min , %d sec",
                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(
                    startTime.toLong() - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())
                    )
                )
            )

            binding.musicProgress.progress = startTime.toInt()
            // handler.postDelayed(this,100)
            handler.postDelayed(this, 100)
        }

    }


}