package com.example.notyoutube

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notyoutube.databinding.FragmentShortsCommentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class ShortsCommentFragment(private var videoId : String, private var channelId : String, private var isVideo:Boolean) : Fragment() {
    private lateinit var binding:FragmentShortsCommentBinding

    private lateinit var adapterObject:dataAdapterComment
    private lateinit var data : ArrayList<CommentModel>

    lateinit var auth:FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        binding = FragmentShortsCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(auth.currentUser == null){
            // no user signed-in, so can't comment
            binding.postCommentButton.visibility = View.GONE
        }
        else{
            binding.postCommentButton.visibility = View.VISIBLE
        }

        val db = databaseReference.child("users").child(channelId).child(if(isVideo) "Videos" else "Shorts").child(videoId)

        // showing comment count
        val count = db.child("commentCount").get().toString()
        binding.commentCountShortsInside.text = count
        Log.d("new", "no. of comments on this post: $count")

        // adding a comment
        binding.postCommentButton.setOnClickListener{
            val comment = binding.newComment.text.toString()

            if (comment.isNotEmpty()) {
                val key = db.child("Comments").push().key
                key?.let {
                    db.child("Comments").child(key).setValue(
                        CommentModel(
                            comment,
                            auth.currentUser!!.uid,
                            0,
                            System.currentTimeMillis(),
                            0
                        )
                    )
                }
            }
            binding.newComment.setText("")
        }

        // showing comments
        data = ArrayList()
        binding.recyclerViewShortsComment.layoutManager = LinearLayoutManager(context as AppCompatActivity)
        adapterObject = dataAdapterComment(data, context as AppCompatActivity)
        binding.recyclerViewShortsComment.adapter = adapterObject

        // fetching data of comments
        db.child("Comments").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                for(snap in snapshot.children){
                    val com = snap.getValue(CommentModel::class.java)
                    com?.let{
                        data.add(com)
                    }
                }
                binding.commentCountShortsInside.text = data.size.toString()

                // update count
                db.child("commentCount").setValue(data.size)

                data.reverse()
                adapterObject.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



        binding.exitButton.setOnClickListener{
            val activity = context as AppCompatActivity
            val manager = activity.supportFragmentManager
            manager.popBackStack("comment fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

    }

}