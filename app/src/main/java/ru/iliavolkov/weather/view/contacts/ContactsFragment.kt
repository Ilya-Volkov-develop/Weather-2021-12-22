package ru.iliavolkov.weather.view.contacts

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.iliavolkov.weather.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {


    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getContacts(){

    }

    companion object {
        @JvmStatic
        fun newInstance() = ContactsFragment()
    }
}