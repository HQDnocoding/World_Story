package com.example.worldstory.dat.admin_viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class PreviewViewHolder(item:View):RecyclerView.ViewHolder(item) {
    val col:ImageView=item.findViewById(R.id.prv_upload_imgview)
}