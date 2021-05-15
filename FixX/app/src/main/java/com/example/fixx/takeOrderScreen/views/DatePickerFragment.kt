package com.example.fixx.takeOrderScreen.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.fixx.takeOrderScreen.contracts.DateSelected
import java.util.*

class DatePickerFragment(val dateSelected : DateSelected) : DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month  = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(context!!, this, year, month, dayOfMonth).apply {
            this.datePicker.minDate = System.currentTimeMillis() - 1000
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateSelected.receiveDate(year, month, dayOfMonth)
        Log.d("TAG", "Got the date")

    }
}