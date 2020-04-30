package br.com.training.android.mymediaplayer

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*
import kotlin.Exception

class MainActivity : AppCompatActivity() {
    private var songsList = ArrayList<SongInfo>()
    private var adapter: SongListAdapter? = null
    private var mediaPlayer: MediaPlayer? = null
    private val _requestCodeAskPermissions = 156

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkUserPermissions()

        val myTracking = MySongTrack()

        myTracking.start()
    }

    inner class SongListAdapter(var myListSong: ArrayList<SongInfo>) : BaseAdapter() {

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val myView = layoutInflater.inflate(R.layout.song_ticket, null)
            val song = myListSong[p0]
            myView.textViewSongName.text = song.title
            myView.textViewAuthor.text = song.authorName
            myView.btnPlay.setOnClickListener {

                if(myView.btnPlay.text == resources.getText(R.string.stop_text)) {
                    mediaPlayer = MediaPlayer()
                    myView.btnPlay.text = resources.getText(R.string.start_text)

                } else {

                    try {
                        mediaPlayer!!.setDataSource(song.songURL)
                        mediaPlayer!!.prepare()
                        mediaPlayer!!.start()

                        myView.btnPlay.text = resources.getText(R.string.stop_text)

                        seekBarProgress.max = mediaPlayer!!.duration
                    } catch (exc: Exception) {}
                }
            }

            return myView
        }

        override fun getItem(p0: Int): Any {
            return this.myListSong[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return this.myListSong.size
        }
    }

    inner class MySongTrack: Thread() {

        override fun run() {

            while (true) {
                try {
                    sleep(1000)
                }catch (exc: Exception) {}

                runOnUiThread {
                    if(mediaPlayer != null) {
                        seekBarProgress.progress = mediaPlayer!!.currentPosition
                    }
                }
            }
        }

    }

    private fun checkUserPermissions() {
        if(Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(applicationContext,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                _requestCodeAskPermissions)

            return
        }

        loadSong()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode) {
            _requestCodeAskPermissions ->
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadSong()
                } else {
                    Toast.makeText(applicationContext, "Application doesn't have permission to read external storage", Toast.LENGTH_LONG).show()
                }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }

    private fun loadSong() {
        val allSongsURL = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}!=0"
        val cursor = contentResolver.query(allSongsURL, null, selection, null, null)

        if(cursor != null && cursor.moveToFirst()) {
            do {
                val songURL = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val songAuthor = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))

                songsList.add(SongInfo(songName,songAuthor,songURL))

            } while(cursor.moveToNext())
        }

        cursor!!.close()

        adapter = SongListAdapter(songsList)
        listViewSongs.adapter = adapter
    }
}
