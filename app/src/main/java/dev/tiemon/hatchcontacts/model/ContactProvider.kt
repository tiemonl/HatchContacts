package dev.tiemon.hatchcontacts.model

import android.content.Context
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.tiemon.hatchcontacts.adapter.Contact
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ContactProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getContactsInfo(): MutableList<Contact> {
        val contacts = mutableListOf<Contact>()

        val contactsCursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null
        )

        contactsCursor?.let { c ->
            while (c.count != 0 && c.moveToNext()) {
                val id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))
                val favorite = c.getInt(c.getColumnIndex(ContactsContract.Contacts.STARRED))
                    .toBoolean()
                val name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val lastEdit = c.getLong(
                    c.getColumnIndex(
                        ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP
                    )
                ).asDate()
                var number = ""
                if (c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val numberCursor = context.contentResolver.query(
                        ContactsContract.CommonDataKinds
                            .Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf<String>(id),
                        null
                    )
                    if (numberCursor?.moveToFirst() != false) {
                        number += numberCursor?.getString(
                            numberCursor.getColumnIndex
                                (ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )
                    }
                    numberCursor?.close()
                }

                contacts.add(
                    Contact(
                        name = name,
                        phone_number = number,
                        favorite = favorite,
                        lastEdit = lastEdit
                    )
                )
            }
        }
        contactsCursor?.close()

        return contacts.sortedWith(compareBy({ !it.favorite }, { it.name })).toMutableList()

    }

    private fun Int.toBoolean(): Boolean {
        return when (this) {
            1 -> true
            else -> false
        }
    }

    private fun Long.asDate(): String =
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(
            Instant.ofEpochMilli(this)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        )
}