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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

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
        view.findViewById<ImageButton>(R.id.effect_volume_btn).setOnClickListener {
            toggleVisibility(view.findViewById(R.id.effect_seekBar))
        }
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

        val effect = view.findViewById<SeekBar>(R.id.effect_seekBar)
        effect.progress = application.volumeEffect
        effect.max = application.volumeEffectMax
        effect.setOnSeekBarChangeListener(
            CustomSeekListener(
                application,
                activity, false
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}