package com.example.worldstory.duc.ducadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemUserCreatedStoryLayoutBinding
import com.example.worldstory.duc.ducutils.loadImgURL
import com.example.worldstory.model.Story
import com.example.worldstory.model.User

class Duc_UseCreatedStory_Adapter(
    var context: Context,
    var userslist: List<User>,
    var storiesList: List<Story>,
    var isText: Boolean
): RecyclerView.Adapter<Duc_UseCreatedStory_Adapter.Viewhoder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewhoder {
        var inflater= LayoutInflater.from(parent.context)
        var binding= ItemUserCreatedStoryLayoutBinding.inflate(inflater)
        return Viewhoder(binding)
    }

    override fun onBindViewHolder(
        holder: Viewhoder,
        position: Int
    ) {
        var user=userslist[position]
        var binding=holder.binding
        var numStoriesAreCreatedByUser=0
        storiesList.forEach { story->
            if(story.userID==user.userID){
                numStoriesAreCreatedByUser++
            }
        }
        binding.txtNicknameItemUserCreatedStory.text=user.userName
        // so luong truyen ma user da dang tai
        binding.txtNumStoriesItemUserCreatedStory.text=numStoriesAreCreatedByUser.toString()
        binding.imgAvatarItemUserCreatedStory.loadImgURL(context,user.imgAvatar)

    }

    override fun getItemCount(): Int {
        return userslist.size
    }

    inner class Viewhoder(var binding: ItemUserCreatedStoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}