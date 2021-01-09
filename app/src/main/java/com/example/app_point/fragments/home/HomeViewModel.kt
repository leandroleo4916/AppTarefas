package com.example.app_point.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.business.BusinessPoint

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val mBusinessPoint: BusinessPoint = BusinessPoint(application)

    private val mListHours = MutableLiveData<List<String>>()
    val listHours: LiveData<List<String>> = mListHours

    private val mListDate = MutableLiveData<List<String>>()
    val listDate: LiveData<List<String>> = mListDate

    fun getHourList(){
        mListHours.value = mBusinessPoint.storePointHour()
    }
    fun getDateList(){
        mListHours.value = mBusinessPoint.storePointDate()
    }
}