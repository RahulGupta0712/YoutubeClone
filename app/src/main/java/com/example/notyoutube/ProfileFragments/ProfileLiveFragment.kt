package com.example.notyoutube.ProfileFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreesen.sntoast.SnToast
import com.emreesen.sntoast.Type
import com.example.notyoutube.MyVideoAdapter
import com.example.notyoutube.Profile
import com.example.notyoutube.R
import com.example.notyoutube.dataAdapter
import com.example.notyoutube.dataLive
import com.example.notyoutube.dataStore
import com.example.notyoutube.databinding.FragmentProfileCommunityBinding
import com.example.notyoutube.databinding.FragmentProfileLiveBinding
import com.shashank.sony.fancytoastlib.FancyToast
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class ProfileLiveFragment : Fragment() {

    private lateinit var binding: FragmentProfileLiveBinding
    private lateinit var Adapter: MyVideoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileLiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Adapter = MyVideoAdapter(dataLive().getData(), context as AppCompatActivity)
        binding.rvLive.layoutManager = LinearLayoutManager(context as AppCompatActivity)
        binding.rvLive.adapter = Adapter

        val datalist = listOf("Latest", "Popular", "Oldest")
        val adapter = ArrayAdapter(
            context as AppCompatActivity,
            android.R.layout.simple_spinner_item,
            datalist
        )
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        binding.spinner4.adapter = adapter

        binding.spinner4.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = parent?.getItemAtPosition(position)
                when (position) {
                    0 -> MotionToast.darkColorToast(
                        context as AppCompatActivity,
                        "Sort streams",
                        "Sorting the livestreams by latest stream first",
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_CENTER,
                        2000,
                        ResourcesCompat.getFont(
                            context as AppCompatActivity,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )

                    1 -> {
                        FancyToast.makeText(
                            context as AppCompatActivity,
                            "Sorting the livestreams by popularity",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS,
                            false
                        ).show()
                    }

                    2 -> {// custom toast
                        SnToast.Builder()
                            .context(context as AppCompatActivity)
                            .message("Sorting the livestreams by oldest stream first")
                            .icon(R.drawable.emoji)
                            .cancelable(false)
                            .animation(true)
                            .type(Type.INFORMATION)
                            .duration(2000)
                            .textSize(18)
                            .textColor(R.color.dodger_blue)
                            .iconSize(30)
                            .backgroundColor(R.color.forest_green)
                            .build()
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }


    companion object {

    }
}