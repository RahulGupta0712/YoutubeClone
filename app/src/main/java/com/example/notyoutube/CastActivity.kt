package com.example.notyoutube

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import cn.pedant.SweetAlert.SweetAlertDialog
import com.shashank.sony.fancytoastlib.FancyToast

class CastActivity {
    fun castActivity(context: Context, view: View) {
        val pop = PopupMenu(context, view)
        pop.inflate(R.menu.cast_menu)

        pop.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.connect -> {
                    SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Connecting with TV")
                        .setContentText("It is taking longer than usual !!!")
                        .setCancelButton("Cancel"){cancel->
                            FancyToast.makeText(context, "Connection Failed", FancyToast.LENGTH_LONG, FancyToast.CONFUSING, false).show()
                            cancel.dismissWithAnimation()
                        }
                        .show()
                    true
                }

                R.id.manage -> {
                    FancyToast.makeText(context, "No Devices Found", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                    true
                }

                R.id.help -> {
                    FancyToast.makeText(context, "No Help", FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false).show()
                    true
                }

                else -> false
            }
        }

        try {
            val fieldPopUp = PopupMenu::class.java.getDeclaredField("popup")
            fieldPopUp.isAccessible = true
            val popup = fieldPopUp.get(pop)
            popup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(popup, true)
        } catch (e: Exception) {

        } finally {
            pop.show()
        }
    }
}