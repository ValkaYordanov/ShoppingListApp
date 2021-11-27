package com.ebookfrenzy.shoppinglistapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.addrange_dialog.*
import kotlinx.android.synthetic.main.addrange_dialog.view.*

class UpdateProductDialog(val addRemoveFunc: (Int, Int, Boolean) -> (Unit)) : DialogFragment() {


    private fun doInputCheck(start: Int, end: Int): Boolean {
        if (start < 1 || end < 1) {
            Toast.makeText(
                context,
                context?.getText(R.string.no_negative_numbers),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (end < start) {
            Toast.makeText(
                context,
                context?.getText(R.string.end_number_bigger_than_start),
                Toast.LENGTH_LONG
            )
                .show()
            return false
        }

        return true

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.addrange_dialog, container)
        dialog?.setTitle(R.string.dialog_remove_add_title)


        view.addButton.setOnClickListener {
            val start = Integer.valueOf(editTextStart.text.toString())
            val end = Integer.valueOf(editTextSlut.text.toString())
            if (doInputCheck(start, end)) {
                dismiss()
                addRemoveFunc(start, end, true)
            }
        }

        view.removeButton.setOnClickListener {
            val start = Integer.valueOf(editTextStart.text.toString())
            val end = Integer.valueOf(editTextSlut.text.toString())
            if (doInputCheck(start, end)) {
                dismiss()
                addRemoveFunc(start, end, false)
            }
        }

        view.cancelButton.setOnClickListener {
            dismiss()
        }

        return view
    }
}