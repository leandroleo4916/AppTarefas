package com.example.app_point.business

import android.content.Context
import com.example.app_point.repository.RepositoryEmployee

class BusinessEmployee(context: Context?) {

    private val mRepositoryEmployee: RepositoryEmployee = RepositoryEmployee(context)

    fun getEmployee(name: String, office: String, email: String, phone: String, admission: String,
        hour1: String, hour2: String, hour3: String, hour4: String): Boolean{

        return mRepositoryEmployee.getEmployee(name, office, email, phone, admission, hour1, hour2, hour3, hour4)
    }

}