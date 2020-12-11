package dev.tiemon.hatchcontacts.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import dev.tiemon.hatchcontacts.R
import dev.tiemon.hatchcontacts.adapter.Contact
import dev.tiemon.hatchcontacts.adapter.ContactAdapter
import dev.tiemon.hatchcontacts.databinding.ContactsFragmentBinding
import dev.tiemon.hatchcontacts.model.ContactViewModel
import kotlinx.android.synthetic.main.contact_view.view.*

@AndroidEntryPoint
class ContactFragment : Fragment(R.layout.contacts_fragment),
    ContactAdapter.ContactAdapterListener {

    private val contactViewModel by activityViewModels<ContactViewModel>()
    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!

    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ContactsFragmentBinding.bind(view)
        val vied = binding.root
        binding.contactsList.adapter = contactAdapter
        setUpObserver()
        activity?.let {
            if (checkSelfPermission(it, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    CONTACTS_PERMISSION
                )
            } else {
                contactViewModel.getContacts()
            }
        }
    }

    private fun setUpObserver() {
        contactViewModel.contacts.observe(viewLifecycleOwner) {
            contactAdapter.setContacts(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CONTACTS_PERMISSION) {
            contactViewModel.getContacts()
        }
    }

    override fun onContactClicked(contact: Contact, position: Int) {
        binding.contactsList.findViewHolderForLayoutPosition(position)?.itemView?.send_sms_button?.let {
            val smsIntent = Intent(Intent.ACTION_VIEW)
            smsIntent.data = Uri.parse("sms:")
            smsIntent.putExtra("address", contact.phone_number)
            it.context.startActivity(smsIntent)
        }
    }

    companion object {
        const val CONTACTS_PERMISSION = 11
    }
}