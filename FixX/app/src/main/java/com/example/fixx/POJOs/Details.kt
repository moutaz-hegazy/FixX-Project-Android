package com.example.fixx.POJOs

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Details(val phoneNumber: String, val accountType: String) : Parcelable