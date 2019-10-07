package com.example.sample2.peopleTable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sample2.model.Person
import com.example.sample2.R
import kotlinx.android.synthetic.main.people_list_item.view.*

//List adapter class
class PeopleListAdapter(
    private val clickListener: OnItemClickListener,
    private val viewModel: PeopleListViewModel
) : ListAdapter<Person, PeopleListAdapter.PeopleListViewHolder>(
    PeopleListDiffUtilCallBack()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleListViewHolder {
        return PeopleListViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: PeopleListViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, viewModel)
    }

    class PeopleListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view: View = v

        companion object {
            fun from(parent: ViewGroup): PeopleListViewHolder {

                return PeopleListViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.people_list_item,
                        parent,
                        false
                    )
                )
            }
        }

        //Bind person data to the view
        fun bind(data: Person, clickListener: OnItemClickListener, viewModel: PeopleListViewModel) {

            view.dobTextViewItem.text = data.dob
            view.nameTextViewItem.text = data.name
            view.emailTextViewItem.text = data.emailID
            view.setOnClickListener {
                clickListener.onItemClick(data)
            }
            view.delete.setOnClickListener {
                viewModel.deletePersonData(data)
            }


        }


    }
}

class PeopleListDiffUtilCallBack : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {

        return oldItem == newItem
    }
}

class OnItemClickListener(private val clickListener: (peopleData: Person) -> Unit) {

    fun onItemClick(peopleData: Person) = clickListener(peopleData)
}