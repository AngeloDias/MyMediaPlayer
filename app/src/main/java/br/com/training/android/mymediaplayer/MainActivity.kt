package br.com.training.android.mymediaplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*

class MainActivity : AppCompatActivity() {
    private var songsList = ArrayList<SongInfo>()
    private var adapter: SongListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadURLOnline()

        adapter = SongListAdapter(songsList)
        listViewSongs.adapter = adapter
    }

    private fun loadURLOnline() {
        songsList.add(SongInfo("Toccata And Fugue In D Minor, BWV 565",
            "Johann Sebastian Bach",
            "https://ia803006.us.archive.org/5/items/ToccataAndFugueInDMinorBWV565_201310/Toccata%20and%20Fugue%20in%20D%20Minor%2C%20BWV%20565%20-%20I.%20Toccata.mp3"))
        songsList.add(SongInfo("TheBestOfClassicalMusicMozartBeethovenBachChopin...ClassicalMusicPianoPlaylistMix",
            "MozartBeethovenBachChopin",
            "https://ia800607.us.archive.org/24/items/TheBestOfClassicalMusicMozartBeethovenBachChopin...ClassicalMusicPianoPlaylistMix/LONG%20Playlist%20of%20Relaxing%20Soft%20Piano%20Music%20to%20Sleep%20and%20Study.mp3"))
        songsList.add(SongInfo("1AA",
            "J Bach",
            "https://ia803006.us.archive.org/5/items/ToccataAndFugueInDMinorBWV565_201310/Toccata%20and%20Fugue%20in%20D%20Minor%2C%20BWV%20565%20-%20I.%20Toccata.mp3"))
        songsList.add(SongInfo("B221",
            "Sebas",
            "https://ia803006.us.archive.org/5/items/ToccataAndFugueInDMinorBWV565_201310/Toccata%20and%20Fugue%20in%20D%20Minor%2C%20BWV%20565%20-%20I.%20Toccata.mp3"))
        songsList.add(SongInfo("T8885",
            "Stian",
            "https://ia803006.us.archive.org/5/items/ToccataAndFugueInDMinorBWV565_201310/Toccata%20and%20Fugue%20in%20D%20Minor%2C%20BWV%20565%20-%20I.%20Toccata.mp3"))
    }

    inner class SongListAdapter(var myListSong: ArrayList<SongInfo>) : BaseAdapter() {

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val myView = layoutInflater.inflate(R.layout.song_ticket, null)
            val song = myListSong[p0]
            myView.textViewSongName.text = song.title
            myView.textViewAuthor.text = song.authorName
            myView.btnPlay.setOnClickListener {}

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
}
