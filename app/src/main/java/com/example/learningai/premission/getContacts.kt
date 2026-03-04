package com.example.learningai.premission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import com.example.learningai.model.Contact

fun getContacts(context: Context): List<Contact> {

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return emptyList()
    }

    val contactsList = mutableListOf<Contact>()
    val uniqueNumbers = mutableSetOf<String>()

    val cursor = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        ),
        null,
        null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )

    cursor?.use {

        val nameIndex =
            it.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )

        val numberIndex =
            it.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

        while (it.moveToNext()) {

            val name = it.getString(nameIndex) ?: ""
            val number = it.getString(numberIndex) ?: ""

            if (name.isNotEmpty() && number.isNotEmpty()) {

                if (!uniqueNumbers.contains(number)) {

                    uniqueNumbers.add(number)
                    contactsList.add(Contact(name, number))
                }
            }
        }
    }

    return contactsList
}