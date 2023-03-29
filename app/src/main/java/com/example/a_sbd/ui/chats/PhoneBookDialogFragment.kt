package com.example.a_sbd.ui.chats

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class PhoneBookDialogFragment: DialogFragment() {
    var onPhoneBookContactClickListener: OnPhoneBookContactClickListener? = null

    private val contactsArray = arrayOf("One", "Two", "Three")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setItems(contactsArray) { dialog, which ->
            onPhoneBookContactClickListener?.onPhoneBookContactClick(contactsArray[which])
        }
    return alertDialogBuilder.create() ?: throw IllegalStateException("Activity cannot be null")
    //return super.onCreateDialog(savedInstanceState)
    }

    interface OnPhoneBookContactClickListener {
        fun onPhoneBookContactClick(name: String)
    }
}