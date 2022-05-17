package com.android.androidprojectapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.list_item.view.*


class ItemsAdaptor(
    private var itemsList: MutableList<ItemInList>,
    private var filteredList: MutableList<ItemInList>,
) : RecyclerView.Adapter<ItemsAdaptor.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var context: Context? = null
    val CAMERA_REQUEST_CODE = 1
    val GALLERY_REQUEST_CODE = 2

    private fun toggleStrikeThrough(newItemTitle: TextView, isChecked: Boolean) {
        if (isChecked) {
            newItemTitle.paintFlags = newItemTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        }
        else {
            newItemTitle.paintFlags = newItemTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun addItemInList(item: ItemInList) {
        itemsList.add(item)
        filteredList.add(item)
        notifyItemInserted(itemsList.size - 1)
    }

    fun filterList(searchText: String) {
        var newList = filteredList.filter { item -> item.title.contains(searchText) }
        itemsList = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun recoverList() {
        itemsList = filteredList
        notifyDataSetChanged()
    }

    fun deleteCheckedItems() {
        itemsList.removeAll { item ->
            item.isChecked
        }
        filteredList.removeAll { item ->
            item.isChecked
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        context = parent.context
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = itemsList[position]
        holder.itemView.apply {
            individualItemName.text = currentItem.title
            individualItemCheckBox.isChecked = currentItem.isChecked
            toggleStrikeThrough(individualItemName, currentItem.isChecked)
            individualItemCheckBox.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(individualItemName, isChecked)
                currentItem.isChecked = !currentItem.isChecked
            }
        }
        val img = holder.itemView.findViewById<ImageView>(R.id.individualItemPhoto)
        img.setImageResource(currentItem.imageSrc)
        img.setOnClickListener {
            popupMenus(holder, position)
        }
    }

    private fun popupMenus(holder: ListViewHolder, position: Int) {
        //TODO: make photo change after it was taken or picked
        //TODO: implement Realm database
        val popupMenu = PopupMenu(context, holder.itemView.individualItemPhoto)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.takePhotoButton -> {
                    cameraCheckPermissions()
                    true
                }
                R.id.addPhotoFromGallery -> {
                    galleryCheckPermissions()
                    true
                }
                else -> true
            }
        }
        popupMenu.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenu)
        menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
    }

    private fun galleryCheckPermissions() {
        Dexter.withContext(context)
            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(
                object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        (context as Activity).startActivityForResult(
                            intent,
                            GALLERY_REQUEST_CODE
                        )
                    }
                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                        AlertDialog.Builder(context)
                            .setMessage("Permission denied!")
                            .setPositiveButton("Go to settings.") {_, _->
                                try {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", context?.packageName ?: context?.packageName, null)
                                    intent.data = uri
                                    (context as Activity).startActivity(
                                        intent
                                    )
                                }catch (e: ActivityNotFoundException) {
                                    e.printStackTrace()
                                }
                            }
                            .setNegativeButton("Cancel") {dialog, _->
                                dialog.dismiss()
                            }.show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        AlertDialog.Builder(context)
                            .setMessage("Permission denied!")
                            .setPositiveButton("Go to settings.") {_, _->
                                try {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", context?.packageName ?: context?.packageName, null)
                                    intent.data = uri
                                    (context as Activity).startActivity(
                                        intent
                                    )
                                }catch (e: ActivityNotFoundException) {
                                    e.printStackTrace()
                                }
                            }
                            .setNegativeButton("Cancel") {dialog, _->
                                dialog.dismiss()
                            }.show()
                    }
                }
            ).onSameThread().check()
    }

    private fun cameraCheckPermissions() {
        Dexter.withContext(context)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA).withListener(
                object : MultiplePermissionsListener {

                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()) {
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                (context as Activity).startActivityForResult(
                                    intent,
                                    CAMERA_REQUEST_CODE
                                )
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        AlertDialog.Builder(context)
                            .setMessage("Permission denied!")
                            .setPositiveButton("Go to settings.") {_, _->
                                try {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", context?.packageName ?: context?.packageName, null)
                                    intent.data = uri
                                    (context as Activity).startActivity(
                                        intent
                                    )
                                }catch (e: ActivityNotFoundException) {
                                    e.printStackTrace()
                                }
                            }
                            .setNegativeButton("Cancel") {dialog, _->
                                dialog.dismiss()
                            }.show()
                    }

                }
            ).onSameThread().check()
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}