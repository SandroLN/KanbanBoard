package com.github.sandroln.editticket.data

internal interface ChangeSingleField {

    fun change(key: String, newValue: Any)
}