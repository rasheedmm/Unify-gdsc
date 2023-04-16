package com.nexus.unify.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nexus.latticetkmce.application.ExoPlayerCaching
import com.nexus.unify.AdapterClasses.AddPosts
import com.nexus.unify.ModelClasses.Posts
import com.nexus.unify.PlayerActivity
import com.nexus.unify.R
import com.nexus.unify.SharedProfileActivity
import com.nexus.unify.databinding.FragmentExoPlayerBinding

class ExoPlayerFragment(
    private val activity: PlayerActivity,
    private val videoPath: Posts,

    position: Int
) : Fragment(R.layout.fragment_exo_player) {
    var isTextViewClicked: Boolean = false
    private val TAG = "ExoPlayerFragment"

    private var viewBinding: FragmentExoPlayerBinding? = null

    private lateinit var simpleExoPlayer: SimpleExoPlayer

    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private val positionOfFragment = position

    private lateinit var simpleCache: SimpleCache

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentExoPlayerBinding.bind(view)
        Log.d(TAG, "onViewCreated: Fragment Position : $positionOfFragment")


        AddPosts.publisherInfo(
            viewBinding!!.imageView16,
            viewBinding!!.txtTitle,
            videoPath,
            activity
        )
        viewBinding!!.txtTitle.setOnClickListener() {
            if (videoPath.anms=="off") {
                  val intent = Intent(context, SharedProfileActivity::class.java)
                  intent.putExtra("uid", videoPath.publisher)
                  startActivity(intent)

              } else {
                  Toast.makeText(context, "broh!, i am anonymous.", Toast.LENGTH_SHORT).show()
              }

        }
        viewBinding!!.imageView16.setOnClickListener() {
            if (videoPath.anms=="off") {
                val intent = Intent(context, SharedProfileActivity::class.java)
                intent.putExtra("uid", videoPath.publisher)
                startActivity(intent)

            } else {
                Toast.makeText(context, "broh!, i am anonymous.", Toast.LENGTH_SHORT).show()
            }

        }
        viewBinding!!.txtDesc.setLinkText(videoPath.text)
        if (videoPath.url.equals("null")) {
            viewBinding!!.txtDesc.visibility = GONE
            viewBinding!!.playerView.visibility = GONE
            viewBinding!!.Imgbg.visibility = GONE
            viewBinding!!.txtDesc1.visibility = VISIBLE

            viewBinding!!.txtDesc1.setLinkText(videoPath.text)


        }

        if (videoPath.type.equals("video")) {
            Toast.makeText(context, "videoo", Toast.LENGTH_SHORT).show()
            initializePlayer()
            viewBinding!!.Imgbg.visibility = GONE
            viewBinding!!.playerView.visibility = VISIBLE
        }
        if (videoPath.type.equals("image")) {
            Toast.makeText(context, "image", Toast.LENGTH_SHORT).show()
            viewBinding!!.playerView.visibility = GONE
            viewBinding!!.Imgbg.visibility = VISIBLE

            Glide.with(activity)
                .load(videoPath.url)
                .into(viewBinding!!.Imgbg)

        }
        initTouchEvent()
    }

    private fun initTouchEvent() {
        if (videoPath.type.equals("video")) {
            viewBinding!!.flMainLayout.setOnClickListener {
                viewBinding!!.playerView.player!!.playWhenReady =
                    !viewBinding!!.playerView.player!!.isPlaying

                if (viewBinding!!.playerView.player!!.isPlaying) {
                    viewBinding!!.ivPauseVideo.visibility = GONE
                } else {
                    viewBinding!!.ivPauseVideo.visibility = VISIBLE
                }
            }
        }

        viewBinding!!.imageView17.setOnClickListener {

            AddPosts.more_btn(activity, it, videoPath)

        }

        viewBinding!!.txtDesc1.setOnLinkClickListener { linkType, matchedText ->
            if (linkType == 16) {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(matchedText)
                startActivity(openURL)
            }


        }
        viewBinding!!.txtDesc.setOnClickListener {

            if (isTextViewClicked) {
                //This will shrink textview to 2 lines if it is expanded.
                viewBinding!!.txtDesc.maxLines = 3

                isTextViewClicked = false
            } else {
                //This will expand the textview if it is of 2 lines
                viewBinding!!.txtDesc.setMaxLines(Integer.MAX_VALUE);
                isTextViewClicked = true

            }

        }
        viewBinding!!.txtDesc.setOnLinkClickListener { linkType, matchedText ->

            if (linkType == 16) {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(matchedText)
                startActivity(openURL)
            }
        }

        viewBinding!!.imageView14.setOnClickListener {
            val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
            if (viewBinding!!.imageView14.getTag() == "like") {

                if (currentFirebaseUser != null) {
                    FirebaseDatabase.getInstance().reference.child("Likes")
                        .child(videoPath.postid)
                        .child(currentFirebaseUser.uid).setValue(true)
                }
                //  addNotifications(post.getPublisher(), post.getPostid());
            } else {
                if (currentFirebaseUser != null) {
                    FirebaseDatabase.getInstance().reference.child("Likes")
                        .child(videoPath.postid)
                        .child(currentFirebaseUser.uid).removeValue()
                }
            }
        }

        viewBinding!!.imageView15.setOnClickListener {
            AddPosts.showcomments(videoPath.postid, videoPath.publisher, activity)
        }


        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val databaseReference = FirebaseDatabase.getInstance().reference
            .child("Likes")
            .child(videoPath.postid)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.childrenCount == 1L) {
                    viewBinding!!.textLikes.setText(dataSnapshot.childrenCount.toString() + "like")
                } else {
                    viewBinding!!.textLikes.setText(dataSnapshot.childrenCount.toString() + "likes")
                }
                if (dataSnapshot.child(firebaseUser!!.uid).exists()) {
                    viewBinding!!.imageView14.setImageResource(R.drawable.loved_svgrepo_com)
                    viewBinding!!.imageView14.setTag("liked")
                } else {
                    viewBinding!!.imageView14.setImageResource(R.drawable.love_svgrepo_com)
                    viewBinding!!.imageView14.setTag("like")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun initializePlayer() {

        simpleCache = ExoPlayerCaching.simpleCache!!

        simpleExoPlayer = SimpleExoPlayer.Builder(activity).build()
        val userAgent = Util.getUserAgent(activity, activity.getPackageName())
        mediaDataSourceFactory =
            DefaultDataSourceFactory(
                this.context,
                Util.getUserAgent(activity, userAgent)
            )
        val mediaSource = buildMediaSource(
            Uri.parse(videoPath.url),

            mediaDataSourceFactory as DefaultDataSourceFactory
        )

        val loopingSource = LoopingMediaSource(mediaSource)
        simpleExoPlayer.prepare(loopingSource)

        viewBinding!!.playerView.useController = false
        viewBinding!!.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
        viewBinding!!.playerView.setRepeatToggleModes(Player.REPEAT_MODE_ALL)
        viewBinding!!.playerView.player = simpleExoPlayer


    }

    private fun buildMediaSource(
        uri: Uri,
        mediaDataSourceFactory: DefaultDataSourceFactory
    ): MediaSource {

        //adding caching
        val cacheDataSourceFactory = CacheDataSourceFactory(
            simpleCache, mediaDataSourceFactory, CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
        )

        // This is the MediaSource representing the media to be played.
        val extension: String = uri.toString().substring(uri.toString().lastIndexOf("."))
        if (extension.contains("null")) {
            return ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(uri)
        } else {
            return HlsMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(uri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null



        try {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.release()
            }
        } catch (e: Exception) {
//code that handles exception
        }


    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: $positionOfFragment")
        if (viewBinding != null && viewBinding!!.playerView.player != null) {
            viewBinding!!.playerView.player!!.playWhenReady =
                !viewBinding!!.playerView.player!!.isPlaying
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: $positionOfFragment")
        if (viewBinding != null && viewBinding!!.playerView.player != null) {
            viewBinding!!.playerView.player!!.playWhenReady =
                !viewBinding!!.playerView.player!!.isPlaying
        }
    }
}