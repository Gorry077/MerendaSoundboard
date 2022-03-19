package com.ebookfrenzy.merendasoundboard

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val mp = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.sound_recycler_view)
        recyclerView.apply {
            adapter = SoundAdapter(SoundDatabase.listSound)
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            addItemDecoration(GridSpacingItemDecoration(3, 50, false))
        }
    }

    fun playSound(id: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            with(mp) {
                reset()
                setDataSource(resources.openRawResourceFd(id))
                prepare()
                start()
            }
        } else {
            MediaPlayer.create(this, id).start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.add_new_sound, menu)
        return true
    }

    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(R.menu.add_new_sound == item.itemId) {
            createNewSound()
        }
    }*/

    private fun createNewSound() {

    }

    inner class SoundAdapter(private val soundList: List<Sound>) : RecyclerView.Adapter<SoundViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.button, parent, false)
            return SoundViewHolder(view)
        }

        override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
            val sound = soundList[position]
            holder.apply {
                button.text = sound.name
                this.sound = sound
            }
        }

        override fun getItemCount(): Int = soundList.size
    }

    inner class SoundViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val button: Button = view.findViewById(R.id.button_sound)
        var sound: Sound? = null

        init {
            button.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            playSound(sound!!.id)
        }
    }
}