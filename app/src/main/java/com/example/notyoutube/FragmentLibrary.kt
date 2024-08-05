package com.example.notyoutube

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notyoutube.databinding.FragmentLibraryBinding
import com.shashank.sony.fancytoastlib.FancyToast
import nl.joery.timerangepicker.TimeRangePicker

class FragmentLibrary : Fragment() {
    private lateinit var binding:FragmentLibraryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set the timer's start time and end time
        binding.picker.startTime = TimeRangePicker.Time(0, 0)
        binding.picker.endTime = TimeRangePicker.Time(8,0)

        // display the start time and end time
        binding.startTime.text = binding.picker.startTime.toString()
        binding.endTime.text = binding.picker.endTime.toString()

        // if any thumb is dragged, change the display time
        binding.picker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                binding.startTime.text = startTime.toString()
            }
            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                binding.endTime.text = endTime.toString()
            }
            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {}
        })

        binding.picker.setOnDragChangeListener(object : TimeRangePicker.OnDragChangeListener {
            override fun onDragStart(thumb: TimeRangePicker.Thumb): Boolean {
                // Do something on start dragging
                if (thumb == TimeRangePicker.Thumb.START) {
                    return false // disallow the user from dragging start thumb
                }
                return true // can drag end
            }

            override fun onDragStop(thumb: TimeRangePicker.Thumb) {
                // Do something on stop dragging
            }
        })



        // submit the time to update in picker
        binding.submitButton.setOnClickListener{
            // input
            val hourString = binding.editTextNumber.text.toString()
            val minuteString = binding.editTextNumber2.text.toString()
            if(hourString.isEmpty() || minuteString.isEmpty()){
                Toast.makeText(context, "Invalid Hour/minute", Toast.LENGTH_SHORT).show()
            }
            else {
                val hour = hourString.toInt()
                val minute = minuteString.toInt()

                if (hour > 23 || minute >= 60)   // negative numbers are not accepted in number edittext, so no need to check for that
                    FancyToast.makeText(
                        context as AppCompatActivity,
                        "Invalid Hour/Minute",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                else {
                    // update time in picker
                    binding.picker.startTime = TimeRangePicker.Time(hour, minute)

                    // update display time
                    binding.startTime.text = binding.picker.startTime.toString()
                }
            }
        }

        // adding a spinner for selecting hour
        val datalist = ArrayList<Int>()
        for (i in 0..23){
            datalist.add(i)
        }

        val Adapter = ArrayAdapter(context as AppCompatActivity, android.R.layout.simple_list_item_1, datalist)
        Adapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice)
        binding.spinner.adapter = Adapter

        var hour = -1
        binding.spinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                hour = parent?.getItemAtPosition(position).toString().toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // spinner for minutes
        val minuteList = ArrayList<Int>()
        for (i in 0..<60 ){
            minuteList.add(i)
        }

        val adapter1 = ArrayAdapter(context as AppCompatActivity, android.R.layout.simple_list_item_1, minuteList)
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        binding.spinner2.adapter = adapter1

        var minute = -1
        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                minute = parent?.getItemAtPosition(position).toString().toInt()
                binding.picker.startTime = TimeRangePicker.Time(hour, minute)
                binding.startTime.text = binding.picker.startTime.toString()
                Toast.makeText(context, " Timer set for startTime : ${binding.picker.startTime}, endTime : ${binding.picker.endTime}", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // setting customised animation on library headline
        val anim = AnimationUtils.loadAnimation(context as AppCompatActivity, R.anim.rotate)
        binding.textView.startAnimation(anim)
    }

    companion object {}
}