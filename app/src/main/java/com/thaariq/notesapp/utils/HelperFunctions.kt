package com.thaariq.notesapp.utils

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.thaariq.notesapp.R
import com.thaariq.notesapp.data.entity.Notes
import com.thaariq.notesapp.data.entity.Priority

object HelperFunctions {


    fun spinnerListener(context : Context?, priorityIndicator : CardView): AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            val red = ContextCompat.getColor(priorityIndicator.context, R.color.pink)
            val yellow = ContextCompat.getColor(priorityIndicator.context, R.color.yellow)
            val green = ContextCompat.getColor(priorityIndicator.context, R.color.green)

            context?.let {
                when(position){

                    0 ->{
                        priorityIndicator.setCardBackgroundColor(red)
                    }

                    1 ->{
                        priorityIndicator.setCardBackgroundColor(yellow)
                    }

                    2 ->{
                        priorityIndicator.setCardBackgroundColor(green)
                    }
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }

    fun parseToPriority(priority: String, context: Context?): Priority {
        val expectedPriority = context?.resources?.getStringArray(R.array.priorities)
        return when(priority){
            expectedPriority?.get(0) -> Priority.HIGH
            expectedPriority?.get(1) -> Priority.MEDIUM
            expectedPriority?.get(2) -> Priority.LOW
            else -> Priority.HIGH

        }
    }

    val emptyDatabase :MutableLiveData<Boolean> = MutableLiveData(true)
    fun checkIsDataEmpty(data: List<Notes>) {
        emptyDatabase.value = data.isEmpty()

    }
}