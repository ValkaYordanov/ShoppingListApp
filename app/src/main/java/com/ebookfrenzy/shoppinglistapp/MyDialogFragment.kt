package com.ebookfrenzy.shoppinglistapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

open class MyDialogFragment(var posClick: ()-> Unit, var negClick: ()->Unit= {}) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val alert = AlertDialog.Builder(
                activity)
        alert.setTitle(R.string.confirmation)
        alert.setMessage(R.string.areYouSure)
        alert.setPositiveButton(R.string.yes, pListener)
        alert.setNegativeButton(R.string.no, nListener)

        return alert.create()
    }


    private var pListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener {_, _ ->

        posClick()
    }


    private var nListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, _ ->

        negClick()
    }

}