package com.nexus.unify.AdapterClasses

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nexus.unify.ModelClasses.User
import com.nexus.unify.databinding.ItemCardBinding

import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import java.util.*

class SwipeAdapter(
    val context: Context,
    val list: ArrayList<User>,
    val cardView: CardStackView,
    val manager: CardStackLayoutManager

) :
    RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder>() {
    inner class SwipeViewHolder(val binding: ItemCardBinding)

        : RecyclerView.ViewHolder(binding.root)
    

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {

        return SwipeViewHolder(ItemCardBinding.inflate(LayoutInflater.from(context), parent, false))

    }



    override fun onBindViewHolder(holder: SwipeViewHolder, position: Int) {

        holder.binding.itemName.text = list[position].name
        holder.binding.itemAge.text = list[position].course
        holder.binding.itemCity.text = list[position].dep
        holder.binding.textView3.text = list[position].bio
        holder.binding.textView17.text = list[position].bio
        holder.binding.textView18.text = list[position].intrests
        //  holder.binding.tvIntrests.text = list[position].intrests

        holder.binding.imageView10.setOnClickListener {
            StrikeClick.add(
                holder.binding.imageView10,
                list[position].uid,
                list[position].name,
                list[position].img1,
                list[position].token
            )

        }
        holder.binding.imageView4.setOnClickListener {

            StrikeClick.report(list[position].uid, holder.binding.imageView4)

        }

        holder.binding.imageclose.setOnClickListener {


            val setting = SwipeAnimationSetting.Builder()

                .setDirection(Direction.Left)
                .setDuration(1500)
                .setInterpolator(AccelerateInterpolator())

                .build()
            manager.setSwipeAnimationSetting(setting)
            cardView.swipe()


        }
        Glide.with(context).load(list[position].img1).into(holder.binding.itemImage)
        holder.binding.imageView9.setOnClickListener {



            StrikeClick.like(holder.binding.imageView10, list[position].uid)

            Handler().postDelayed({
                val setting = SwipeAnimationSetting.Builder()

                    .setDirection(Direction.Right)
                    .setDuration(1500)
                    .setInterpolator(AccelerateInterpolator())

                    .build()
                manager.setSwipeAnimationSetting(setting)
                cardView.swipe()

            }, 1000)


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun hiicancel() {
        //Toast.makeText(context, "cancell", Toast.LENGTH_SHORT).show()

    }

    fun hiilike() {
        Toast.makeText(context, "like", Toast.LENGTH_SHORT).show()

    }




}