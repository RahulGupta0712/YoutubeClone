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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.emreesen.sntoast.SnToast
import com.emreesen.sntoast.Type
import com.example.notyoutube.DataModelVideoDetails
import com.example.notyoutube.MyVideoAdapter
import com.example.notyoutube.R
import com.example.notyoutube.databinding.FragmentVideoHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.shashank.sony.fancytoastlib.FancyToast
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class ProfileVideosFragment : Fragment() {
    private lateinit var adapterObject : MyVideoAdapter
    private lateinit var binding:FragmentVideoHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVideoHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseRef:DatabaseReference
    private lateinit var videoList : ArrayList<DataModelVideoDetails>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
        videoList = ArrayList()
        adapterObject = MyVideoAdapter(videoList, context as AppCompatActivity)
        binding.videos.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.videos.adapter = adapterObject

        val user = auth.currentUser
        user?.let{
            databaseRef.child("users").child(user.uid).child("Videos").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    videoList.clear()
                    for(snap in snapshot.children){
                        val data = snap.getValue<DataModelVideoDetails>()
                        data?.let{
                            videoList.add(data)
                        }
                    }

                    videoList.reverse()

                    adapterObject.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

        val datalist = listOf("Latest", "Popular", "Oldest")
        val adapter = ArrayAdapter(context as AppCompatActivity, android.R.layout.simple_list_item_1, datalist)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        binding.spinner3.adapter = adapter

        binding.spinner3.onItemSelectedListener = object:OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item = parent?.getItemAtPosition(position)
                when(position){
                    0 -> FancyToast.makeText(context as AppCompatActivity, "Sorting the videos by Latest video first", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                    1 -> {
                        // custom toast
                        SnToast.Builder()
                            .context(context as AppCompatActivity)
                            .message("Sorting the videos by Popularity")
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
                    2 -> MotionToast.darkColorToast(context as AppCompatActivity, "Sort videos", "Sorting the videos by oldest video first", MotionToastStyle.INFO, MotionToast.GRAVITY_CENTER, 2000, ResourcesCompat.getFont(context as AppCompatActivity, www.sanju.motiontoast.R.font.helvetica_regular))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

    }

}