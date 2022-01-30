package ru.iliavolkov.weather.view.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.iliavolkov.weather.databinding.FragmentContactsBinding

class ContactsFragment : Fragment(),OnItemClickListener {


    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val arrayContacts = mutableListOf<Contact>()

    private val adapter: ContactFragmentAdapter by lazy {
        ContactFragmentAdapter(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission(){
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it,Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED ->{
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)->{
                    showDialog()
                }
                else ->{
                    requestPermission()
                }
            }
        }
    }

    private val REQUEST_CODE = 999
    private fun requestPermission() {
        @Suppress("DEPRECATION")
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CODE){
            when{
                grantResults[0]==PackageManager.PERMISSION_GRANTED->{
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)->{
                    showDialog()
                }
            }
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
                .setTitle("Доступ к контактам")
                .setMessage("Объяснение")
                .setPositiveButton("Предоставить доступ") { _, _ ->
                    requestPermission()
                }
                .setNegativeButton("Нет") {dialog,_ ->dialog.dismiss()}
                .create().show()
    }

    @SuppressLint("Range")
    private fun getContacts(){
        context?.let {
            val contentResolver = it.contentResolver
            val cursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursor?.let { cursor->
                for (i in 0 until cursor.count){
                    cursor.moveToPosition(i)
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val numberPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    arrayContacts.add(Contact(name,numberPhone))
                }
            }
            cursor?.close()
            initView()
        }
    }

    private fun initView(){
        binding.contactRecyclerView.adapter = adapter
        adapter.setContact(arrayContacts)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContactsFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(contact: Contact) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:${contact.number}")
        startActivity(callIntent)
    }
}