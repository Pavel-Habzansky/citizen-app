package com.pavelhabzansky.citizenapp.features.cities.search.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.pavelhabzansky.citizenapp.features.cities.search.view.vo.AutoCompleteItem

class CityAutocompleteArrayAdapter : ArrayAdapter<AutoCompleteItem> {

    constructor(context: Context) : super(context, android.R.layout.simple_list_item_1)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: run {
            LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        }

        view.findViewById<TextView>(android.R.id.text1).text = getItem(position)?.name
        return view
    }

}