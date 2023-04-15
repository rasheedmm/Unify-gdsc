package com.nexus.unify


import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nexus.unify.AdapterClasses.StrikeClick
import com.nexus.unify.AdapterClasses.SwipeAdapter
import com.nexus.unify.ModelClasses.User
import com.nexus.unify.databinding.FragmentSwipeNewBinding
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import java.util.*


class SwipeNewFragment : Fragment() {

    private lateinit var binding: FragmentSwipeNewBinding
    private lateinit var manager: CardStackLayoutManager
    private lateinit var swiipeadapter: SwipeAdapter
    private lateinit var  sharedPreferences : SharedPreferences

    var numSwipes = 0
    var maxSwipes = 0
    var lastSwipeTime: Long = 0
    var lastSwipeTimecheck: Long = 0
    var currenttime: Long = 0
    var intrests = ""
    val timer = Timer()
    val twelveHoursInMillis: Long = 12 * 60 * 60 * 1000


    // Keep track of the number of swipes so far today


    private lateinit var mContext: Context

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSwipeNewBinding.inflate(layoutInflater)


   /* sharedPreferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        numSwipes = sharedPreferences.getInt("numSwipes", 0)
        maxSwipes = 5
        //  Toast.makeText(mContext, "" + maxSwipes, Toast.LENGTH_SHORT).show()
        if (numSwipes > maxSwipes) {
            // If the limit has been reached, show a toast message
            Toast.makeText(mContext, "Daily swipe limit reached", Toast.LENGTH_SHORT).show()
            lastSwipeTimecheck = sharedPreferences.getLong("lastSwipeTime", 0)
            val currentTime = System.currentTimeMillis()
            if (getMinuteDifference(currentTime, lastSwipeTimecheck + (60 * 1000)) > 55) {

                Toast.makeText(
                    mContext,
                    "" + getMinuteDifference(currentTime, lastSwipeTimecheck + (60 * 1000)),
                    Toast.LENGTH_SHORT
                ).show()
                val editor = sharedPreferences.edit()
                editor.putInt("numSwipes", 0)
                val currentTime = System.currentTimeMillis()
                editor.putLong("lastSwipeTime", currentTime)
                editor.apply()
                getData()
            }
        } else {
            // If the limit has not been reached, check the time of the last swipe

            numSwipes++
            val editor = sharedPreferences.edit()
            editor.putInt("numSwipes", numSwipes)
            val currentTime = System.currentTimeMillis()
            editor.putLong("lastSwipeTime", currentTime)
            editor.apply()
            getData()
        }*/
        getData()




        return binding.root
    }

    private lateinit var list: ArrayList<User>


    private fun init() {


        // var value = _prefs.getInt("count", 0)


        binding.shimmerViewContainer.startShimmer()
        manager = CardStackLayoutManager(mContext, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {

                if (direction == Direction.Left) {


                }
                if (direction == Direction.Right) {

                    //swipeAdapter.hiilike();
                }


            }

            override fun onCardSwiped(direction: Direction?) {
                if (manager.topPosition == list.size) {


                }



                if (direction == Direction.Left) {



                }
                if (direction == Direction.Right) {


                }



                //   Toast.makeText(mContext, "" + maxSwipes, Toast.LENGTH_SHORT).show()
              /*  if (numSwipes > maxSwipes) {
                    // If the limit has been reached, show a toast message
                    Toast.makeText(mContext, "Daily swipe limit reached", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                    lastSwipeTimecheck = sharedPreferences.getLong("lastSwipeTime", 0)
                    val currentTime = System.currentTimeMillis()
                    if (getMinuteDifference(currentTime, lastSwipeTimecheck + (60 * 1000)) > 55) {


                        val editor = sharedPreferences.edit()
                        editor.putInt("numSwipes", 0)
                        val currentTime = System.currentTimeMillis()
                        editor.putLong("lastSwipeTime", currentTime)
                        editor.apply()
                        getData()
                    }
                } else {
                    // If the limit has not been reached, check the time of the last swipe

                    numSwipes++
                    val editor = sharedPreferences.edit()
                    editor.putInt(
                        "nu" +
                                "+mSwipes", numSwipes
                    )
                    val currentTime = System.currentTimeMillis()
                    editor.apply()

                }*/


            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {
                // binding.cardStackView1.adapter.btn_close.callOnClick()
            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {


            }
        })
        manager.setTranslationInterval(12.0f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(40.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setStackFrom(StackFrom.Bottom)
        manager.setVisibleCount(5)

    }

    private fun getData() {


        val rootRef = FirebaseDatabase.getInstance().reference
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val uidRef = rootRef.child("profiles").child(uid)
        uidRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                intrests =
                    snapshot.child("intrests").getValue(String::class.java).toString().toLowerCase()


            } else {

            }
        }
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        FirebaseDatabase.getInstance().getReference("profiles")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {

                        list = arrayListOf()
                        for (data in snapshot.children) {

                            val model = data.getValue(User::class.java)

                            if (model != null) {
                                if (currentFirebaseUser != null) {


                                    if (model.uid != currentFirebaseUser.uid && model.name != null && StrikeClick.check(
                                            intrests, model.intrests, context
                                        ) == false
                                    ) {
                                        list.add(model!!)
                                        binding.shimmerViewContainer.stopShimmer()
                                        binding.shimmerViewContainer.visibility = View.GONE
                                    }
                                    if (list.size == 0) {
                                        //  Toast.makeText(context, "no values"+list.size, Toast.LENGTH_SHORT).show()
                                    }

                                }
                            }
                        }
                        list.shuffle()

                        init()
                        binding.cardStackView1.layoutManager = manager
                        binding.cardStackView1.itemAnimator = DefaultItemAnimator()



                        swiipeadapter =
                            SwipeAdapter(mContext, list, binding.cardStackView1, manager)

                        binding.cardStackView1.adapter = swiipeadapter


                    } else {
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

    fun getMinuteDifference(time1: Long, time2: Long): Long {
        val diffInMillis = kotlin.math.abs(time1 - time2)
        val diffInMinutes = diffInMillis / (1000 * 60)
        return diffInMinutes
    }
}