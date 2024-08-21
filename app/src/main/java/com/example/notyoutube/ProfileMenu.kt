package com.example.notyoutube

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import com.shashank.sony.fancytoastlib.FancyToast

class ProfileMenu {
    fun profileMenu(context : Context, view : View){
        val pop = PopupMenu(context, view)
        pop.inflate(R.menu.profile_menu)

        pop.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.viewChannel ->{

                    true
                }
                R.id.logout -> {
                    true
                }

                R.id.settings -> {
                    FancyToast.makeText(context, "Settings Not Available Right Now !", FancyToast.LENGTH_SHORT,FancyToast.WARNING, false).show()
                    true
                }

                R.id.help -> {
                    Toast.makeText(context, "No Help :)", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        try {
            val fieldpopUp = PopupMenu::class.java.getDeclaredField("popUp")
            fieldpopUp.isAccessible = true
            val popUp = fieldpopUp.get(pop)
            popUp.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(popUp, true)
        } catch (e: Exception) {
            Log.d("error", "profileMenu: Exception occurred")
        } finally {
            pop.setForceShowIcon(true)
            pop.show()
        }
    }


}