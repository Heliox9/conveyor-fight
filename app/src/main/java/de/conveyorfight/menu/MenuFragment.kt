package de.conveyorfight.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import de.conveyorfight.ConveyorApplication
import de.conveyorfight.MainActivity
import de.conveyorfight.R


/**
 * A simple [Fragment] subclass used for the Menu Screen
 */
class MenuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        // main navigation action
        view.findViewById<Button>(R.id.menu_play_button).setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_menuFragment_to_aiGameFragment)
        }
        view.findViewById<Button>(R.id.menu_play_online_button).setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_menuFragment_to_onlineGameFragment)
        }

        // Volume bar visibility toggles
        view.findViewById<ImageButton>(R.id.music_volume_btn).setOnClickListener {
            toggleVisibility(view.findViewById(R.id.music_seekBar))
        }

        // SeekBar change listener logic
        val activity = activity as MainActivity
        val application: ConveyorApplication =
            (activity.application) as ConveyorApplication

        val music = view.findViewById<SeekBar>(R.id.music_seekBar)
        music.progress = application.volumeMusic
        music.max = application.volumeMusicMax
        music.setOnSeekBarChangeListener(
            CustomSeekListener(
                application,
                activity, true
            )
        )



        activity.changeTrack(R.raw.track_menu)

        // set screen background resource
        view.findViewById<ConstraintLayout>(R.id.menu_layout)
            .setBackgroundResource(R.drawable.background_menu)

        // Inflate the layout for this fragment
        return view
    }

    private class CustomSeekListener(
        val application: ConveyorApplication,
        val activity: MainActivity,
        val music: Boolean
    ) :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (music) application.volumeMusic = progress
            else application.volumeEffect = progress

        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            // ("Not yet implemented")
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            // update volumes
            activity.setVolume()
        }

    }

    private fun toggleVisibility(seek: SeekBar) {
        if (seek.visibility == View.VISIBLE) seek.visibility = View.INVISIBLE
        else seek.visibility = View.VISIBLE
    }


}