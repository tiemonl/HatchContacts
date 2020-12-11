package dev.tiemon.hatchcontacts.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import dev.tiemon.hatchcontacts.utils.ListenerUtils.setSafeOnClickListener
import kotlinx.android.synthetic.main.contact_view.view.*

class ContactViewHolder(
    itemView: View,
    contactListener: ContactListener
) : RecyclerView.ViewHolder(itemView) {
    private val name = itemView.contact_name
    private val number = itemView.contact_number
    private val lastEdit = itemView.contact_last_edit
    private val favorite = itemView.contact_fav
    private val divider = itemView.divider
    private val sendSms = itemView.send_sms_button

    interface ContactListener {
        fun onContactClicked(position: Int)
    }

    init {
        sendSms.setSafeOnClickListener {
            contactListener.onContactClicked(bindingAdapterPosition)
        }
    }

    fun bindContact(contact: Contact, isLastItem: Boolean = false) {
        name.text = contact.name
        number.text = contact.phone_number
        lastEdit.text = contact.lastEdit
        favorite.isVisible = contact.favorite
        divider.isVisible = !isLastItem
    }
}