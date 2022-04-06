package com.thaariq.notesapp.utils

import android.view.View
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView
import com.thaariq.notesapp.R
import com.thaariq.notesapp.data.entity.Notes
import com.thaariq.notesapp.data.entity.Priority
import com.thaariq.notesapp.ui.home.HomeFragmentDirections

object BindingAdapters {

    @BindingAdapter("android:parsePriorityColor")
    @JvmStatic
    fun parsePriorityColor(cardView: MaterialCardView, priority: Priority){
        when(priority){
            Priority.HIGH -> {
                cardView.setCardBackgroundColor(cardView.context.getColor(R.color.pink))
            }
            Priority.MEDIUM -> {
                cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))
            }
            Priority.LOW -> {
                cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))
            }
        }
    }

    @BindingAdapter("android:sendDataToDetail")
    @JvmStatic
    fun sendDataToDetail(view: ConstraintLayout, currentItem : Notes){
        view.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(currentItem)
            view.findNavController().navigate(action)
        }
    }

    @BindingAdapter("android:parsePriorityToInt")
    @JvmStatic
    fun parsePriorityToInt(view: Spinner, priority: Priority){
        when(priority){
            Priority.HIGH -> view.setSelection(0)
            Priority.MEDIUM -> view.setSelection(1)
            Priority.LOW -> view.setSelection(2)
        }
    }

    @BindingAdapter("android:emptyDatabase")
    @JvmStatic
    fun emptyDatabase(view: View, emptyDatabase : MutableLiveData<Boolean>){
        emptyDatabase.observe(view.context as FragmentActivity){
            view.visibility = if (it) View.VISIBLE else View.GONE
        }
//        when (emptyDatabase.value){
//            true -> view.visibility = View.VISIBLE
//            else -> view.visibility = View.INVISIBLE
//        }
    }
}