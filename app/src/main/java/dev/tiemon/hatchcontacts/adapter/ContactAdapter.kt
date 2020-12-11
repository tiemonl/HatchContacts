package dev.tiemon.hatchcontacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tiemon.hatchcontacts.R

class ContactAdapter(
    private val contactListener: ContactAdapterListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ContactViewHolder.ContactListener {

    private val contactsList = mutableListOf<Contact>()

    interface ContactAdapterListener {
        fun onContactClicked(contact: Contact, position: Int)
    }

    init {
        setHasStableIds(true)
    }

    fun setContacts(contacts: List<Contact>) {
        contactsList.addAll(contacts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.contact_view,
                parent,
                false
            ),
            this
        )

    override fun getItemCount(): Int = contactsList.size

    override fun getItemId(position: Int): Long = contactsList[position].hashCode().toLong()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            when (holder) {
                is ContactViewHolder -> holder.bindContact(
                    contactsList[position],
                    position == contactsList.size.minus(1)
                )
            }
        }
    }

    override fun onContactClicked(position: Int) {
        contactListener.onContactClicked(contactsList[position], position)
    }

}