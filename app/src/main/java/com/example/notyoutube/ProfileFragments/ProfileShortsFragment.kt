package com.example.notyoutube.ProfileFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notyoutube.DataAdapterShortsProfile
import com.example.notyoutube.DataModelVideoDetails
import com.example.notyoutube.databinding.FragmentProfileShortsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class ProfileShortsFragment : Fragment() {

    private lateinit var binding: FragmentProfileShortsBinding
    private lateinit var Adapter:DataAdapterShortsProfile
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileShortsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var datalist : ArrayList<DataModelVideoDetails>
    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datalist = ArrayList()
        Adapter = DataAdapterShortsProfile(context as AppCompatActivity, datalist)
        binding.rvProfileShorts.layoutManager = GridLayoutManager(context as AppCompatActivity, 3)
        binding.rvProfileShorts.adapter = Adapter

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val user = auth.currentUser
        if(user != null){
            databaseReference.child("users").child(user.uid).child("Shorts").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    datalist.clear()
                    for(snap in snapshot.children){
                        val short = snap.getValue<DataModelVideoDetails>()
                        short?.let{
                            datalist.add(short)
                        }
                    }

                    datalist.reverse()
                    Adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }


    }


}