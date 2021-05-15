package com.example.fixx

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Details(val phoneNumber: String, val accountType: String) : Parcelable