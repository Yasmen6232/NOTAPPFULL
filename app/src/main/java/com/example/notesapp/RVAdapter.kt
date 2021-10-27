package com.example.notesapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.NotesViewBinding
import io.github.muddz.styleabletoast.StyleableToast

class RVAdapter (private val context: Context,private val list: ArrayList<String>): RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {

    private val dpHelper by lazy { DBHelper(context) }

    class ItemViewHolder(val binding: NotesViewBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(NotesViewBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val note= list[position]
        holder.binding.noteTV.text= note
        holder.binding.editImage.setOnClickListener{
            val input = EditText(context)
            input.hint = "Enter New Note Here"
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage("Please Enter New Note:")
            .setCancelable(false)
            .setPositiveButton("Save") {
                _,_ -> val str =input.text.toString()
                if(str.isNotBlank()) {
                    val check= dpHelper.updateNotes(position+1,str)
                    if (check != 0) {
                        list.clear()
                        StyleableToast.makeText(
                            context,
                            "Updated Successfully!!",
                            R.style.mytoast
                        ).show()
                        list.addAll(dpHelper.gettingNotes())
                        notifyDataSetChanged()
                    }
                    else
                        StyleableToast.makeText(context,"Something Went Wrong!!\n$check",R.style.mytoast).show()
                }
                else
                    StyleableToast.makeText(context,"Please Enter Valid Values!!",R.style.mytoast).show()
            }
            .setNegativeButton("Cancel") {dialog,_ -> dialog.cancel()
            }
            val alert = dialogBuilder.create()
            alert.setTitle("Update Note")
            alert.setView(input)
            alert.show()
        }
        holder.binding.deleteImage.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes") {
                        _,_ ->
                    val check= dpHelper.deleteNotes(position+1)
                    if (check != 0) {
                        list.clear()
                        StyleableToast.makeText(
                            context,
                            "Deleted Successfully!!",
                            R.style.mytoast
                        ).show()
                        list.addAll(dpHelper.gettingNotes())
                        notifyDataSetChanged()
                    }
                    else
                        StyleableToast.makeText(context,"Something Went Wrong!!\n$check",R.style.mytoast).show()
                }
                .setNegativeButton("Cancel") {dialog,_ -> dialog.cancel()
                }
            val alert = dialogBuilder.create()
            alert.setTitle("Are You Sure You Want to Delete Note")
            alert.show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}