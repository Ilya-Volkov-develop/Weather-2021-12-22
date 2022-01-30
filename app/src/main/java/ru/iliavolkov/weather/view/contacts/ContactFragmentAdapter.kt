package ru.iliavolkov.weather.view.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.iliavolkov.weather.R
import ru.iliavolkov.weather.databinding.FragmentContactRecyclerItemBinding

class ContactFragmentAdapter(val listener: OnItemClickListener): RecyclerView.Adapter<ContactFragmentAdapter.ContactViewHolder>() {

    private var contactData:List<Contact> = listOf()

    fun setContact(data:List<Contact>){
        this.contactData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_contact_recycler_item,parent,false))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(this.contactData[position])
    }

    override fun getItemCount(): Int {
        return contactData.size
    }

    inner class ContactViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind(contact: Contact){
            FragmentContactRecyclerItemBinding.bind(itemView).run {
                contactName.text = contact.name
                root.setOnClickListener{ listener.onItemClick(contact) }
            }
        }
    }
}