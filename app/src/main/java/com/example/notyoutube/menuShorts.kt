package com.example.notyoutube

import android.content.Context
import android.util.Log
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.shashank.sony.fancytoastlib.FancyToast

class menuShorts {
    fun showMenu(context : Context, view : View){
        val pop = PopupMenu(context, view )
        pop.inflate(R.menu.menu_shorts)

        pop.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.descriptionShorts ->{
                    FancyToast.makeText(context, "Coming Soon...", FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false).show()
                    true
                }
                R.id.saveShorts ->{
                    FancyToast.makeText(context, "Saved the short...", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                    true
                }
                R.id.sendFeedbackShorts ->{
                    FancyToast.makeText(context, "This is Coming Soon...", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
                    true
                }
                R.id.captionsShorts ->{
                    FancyToast.makeText(context, "Captioning Starting", FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show()
                    true
                }
                R.id.qualityShorts ->{
                    FancyToast.makeText(context, "Quality can't be set for shorts", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                    true
                }
                R.id.notInterestedShorts ->{
                    FancyToast.makeText(context, "We won't show you again", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
                    true
                }
                R.id.dontRecommendShorts ->{
                    FancyToast.makeText(context, "We won't recommend you again", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                    true
                }
                R.id.reportShorts->{
                    FancyToast.makeText(context, "Video Reported", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
                    true
                }

                else -> false
            }
        }

        try{
            val fieldPopUp = PopupMenu::class.java.getDeclaredField("popUp")
            fieldPopUp.isAccessible = true
            val popUp = fieldPopUp.get(pop)
            popUp.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(popUp, true)
        }catch (e : Exception){
            Log.d("error", "showMenu: Exception occurred")
        }
        finally {
            pop.show()
        }
    }
}