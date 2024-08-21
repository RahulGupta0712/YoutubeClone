package com.example.notyoutube

import android.content.Context
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import cn.pedant.SweetAlert.SweetAlertDialog
import com.emreesen.sntoast.SnToast
import com.emreesen.sntoast.Type
import com.shashank.sony.fancytoastlib.FancyToast


class YoutubeMenu {
    fun customMenu(context: Context, view: View) {
        val pop = PopupMenu(context, view)  // create a popup
        pop.inflate(R.menu.menu)    // display menu over current activity (meaning of inflate)

        pop.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.download -> {
                    SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Download Video")
                        .setContentText("Buy Premium to download the video")
                        .setCustomImage(R.drawable.youtube)
                        .setConfirmButton("OK") { confirm ->
                            // Custom Toast
                            SnToast.Builder()
                                .context(context)
                                .type(Type.INFORMATION)
                                .message("Pay Premium Fees")
                                .cancelable(true)
                                .iconSize(24)
//                                 .textSize(int size) Optional Default 18sp
//                                 .animation(false or true) Optional Default: True
                                .duration(2000)
                                .backgroundColor(R.color.cadet_blue)// Default: It is filled according to the toast type. If an assignment is made, the assigned value is used
                                .icon(R.drawable.money_icon)// Default: It is filled according to the toast type. If an assignment is made, the assigned value is used
                                .build()
                            confirm.dismissWithAnimation()
                        }
                        .setCancelButton("Cancel") { ite ->
                            // Custom Toast
                            SnToast.Builder()
                                .context(context)
                                .type(Type.ERROR)
                                .message("Some Error occurred")
                                .cancelable(true)
                                .iconSize(24)
//                                 .textSize(int size) Optional Default 18sp
//                                 .animation(false or true) Optional Default: True
                                .duration(2000)
                                .backgroundColor(R.color.fire_brick)// Default: It is filled according to the toast type. If an assignment is made, the assigned value is used
//                                .icon(R.drawable.money_icon)// Default: It is filled according to the toast type. If an assignment is made, the assigned value is used
                                .build()
                            ite.dismissWithAnimation()
                        }
                        .show()
                    true
                }

                R.id.share -> {
                    val dialog = AlertDialog.Builder(context)
                    dialog.setTitle("Share to")
                    dialog.setMessage("Copy Link")
                    dialog.setIcon(R.drawable.share_icon)
                    dialog.setNeutralButton("Cancel") { _, _ ->
                        FancyToast.makeText(
                            context,
                            "Error Occurred",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
                    }
                    val alert_dialog = dialog.create()
                    alert_dialog.setCancelable(false)
                    alert_dialog.show()
                    true
                }

                R.id.save -> {
                    FancyToast.makeText(
                        context,
                        "Video Saved to Playlist",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.INFO,
                        false
                    ).show()
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
            Log.d("error", "customMenu: Exception occurred")
        } finally {
            pop.show()
        }
    }
}