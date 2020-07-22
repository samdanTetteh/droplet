package com.ijikod.droplet.ui

import android.app.Activity
import android.app.Dialog
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.util.Util
import com.ijikod.droplet.R
import com.ijikod.droplet.Utils


/**
 * A simple [Fragment] subclass.
 * Use the [ListDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListDialogFragment : DialogFragment() {

    lateinit var optionsList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialog?.setTitle("samdan")
        val view =  inflater.inflate(R.layout.fragment_list_dialog, container, false)
        optionsList = view.findViewById(R.id.options_list)
        setUpAdapterView()
        return view
    }


    private fun setUpAdapterView(){
        val optionsAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, arrayOf(getString(R.string.camera_txt), getString(R.string.select_image_txt)))
        optionsList.adapter = optionsAdapter

        optionsList.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val intent = Intent()
                intent.putExtra(getString(R.string.option_extra), optionsAdapter.getItem(position))
                targetFragment?.onActivityResult(Utils.FRAGMENT_REQUEST, Activity.RESULT_OK, intent)
                dialog?.dismiss()
            }

        }
    }
}