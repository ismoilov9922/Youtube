package uz.pdp.youtube.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayerFragment
import com.squareup.picasso.Picasso
import uz.pdp.youtube.R
import uz.pdp.youtube.adapters.CommentAdapter
import uz.pdp.youtube.adapters.HomeRoomAdapter
import uz.pdp.youtube.adapters.SearchAdapter
import uz.pdp.youtube.database.AppDatabase
import uz.pdp.youtube.database.VideoHome
import uz.pdp.youtube.database.VideoHome1
import uz.pdp.youtube.databinding.ActivityVideoBinding
import uz.pdp.youtube.databinding.ItemBottomSheetBinding
import uz.pdp.youtube.isNetwork.NetworkHelper
import uz.pdp.youtube.models.search.Item
import uz.pdp.youtube.repository.YoutubeRepository
import uz.pdp.youtube.retrofite.ApiClient
import uz.pdp.youtube.sharPreference.YourPreference
import uz.pdp.youtube.utils.DeveloperKey
import uz.pdp.youtube.utils.Status
import uz.pdp.youtube.viewModel.ViewModelFactory
import uz.pdp.youtube.viewModel.ViewModelGetComment
import uz.pdp.youtube.viewModel.ViewModelSearch
import uz.pdp.youtube.viewModel.ViewModelVideo
import kotlin.random.Random

class VideoActivity : AppCompatActivity() {
    private var videoId: String = "gXWXKjR-qII"
    private lateinit var binding: ActivityVideoBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var viewModelSearch: ViewModelSearch
    private lateinit var viewModelVideo: ViewModelVideo
    private lateinit var viewModelGetComment: ViewModelGetComment
    private lateinit var homeRoomAdapter: HomeRoomAdapter
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var list: ArrayList<Item>
    private lateinit var yourPreference: YourPreference
    private var likeCount: Int = 0
    private var islikeCount: Boolean = false
    private var disLikeCount: Int = 0
    private var isfollow: Boolean = false
    private lateinit var videoHome: VideoHome1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        videoId = intent.getStringExtra("item").toString()
        yourPreference = YourPreference.getInstance(binding.root.context)
        yourPreference.saveData("videoId", videoId)
        binding.comment.isSelected = true
        list = ArrayList()

        YoutubeRepository(ApiClient.apiService).AppModule(binding.root.context)
        viewModelVideo = ViewModelProvider(this,
            ViewModelFactory(ApiClient.apiService))[ViewModelVideo::class.java]
        viewModelGetComment = ViewModelProvider(this,
            ViewModelFactory(ApiClient.apiService))[ViewModelGetComment::class.java]

        viewModelVideo.getVideData()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        Toast.makeText(this,
                            it.message,
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                    Status.SUCCESS -> {
                        val item: uz.pdp.youtube.models.videoData.Item? = it.data?.items?.get(0)
                        Picasso.get().load(item?.snippet?.thumbnails?.medium?.url)
                            .placeholder(R.drawable.android)
                            .error(R.drawable.android)
                            .into(binding.imageVideo)
                        binding.viewCount.text =
                            "${item?.statistics?.viewCount} Пpосмотpов ${item?.snippet?.publishedAt}"
                        binding.channelPotpis.text = "Подписчики ${item?.statistics?.viewCount}"
                        binding.desc.text = "${item?.snippet?.description}"
                        binding.title.text = "${item?.snippet?.title}"
                        binding.likeTv.text = "${item?.statistics?.likeCount}"
                        binding.channelName.text = "${item?.snippet?.channelTitle}"
                        binding.dislikeTv.text = "${item?.statistics?.dislikeCount}"
                        binding.comment.text = "Комментарии ${item?.statistics?.commentCount}"
                        likeCount = item?.statistics?.likeCount?.toInt()!!
                        disLikeCount = item.statistics.dislikeCount.toInt()
                        videoHome = VideoHome1(videoId,
                            item.snippet.title,
                            item.snippet.description,
                            item.snippet.thumbnails.high.url)
                        yourPreference.saveData("search", item.snippet.title)
                    }
                }
            })
        viewModelSearch = ViewModelProvider(this,
            ViewModelFactory(ApiClient.apiService))[ViewModelSearch::class.java]
        val youTubePlayerFragment =
            fragmentManager.findFragmentById(R.id.youtube_fragment1) as YouTubePlayerFragment
        youTubePlayerFragment.initialize(DeveloperKey.KEY,
            object : com.google.android.youtube.player.YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: com.google.android.youtube.player.YouTubePlayer.Provider?,
                    p1: com.google.android.youtube.player.YouTubePlayer?,
                    p2: Boolean,
                ) {
                    p1?.loadVideo(videoId)
                }

                override fun onInitializationFailure(
                    p0: com.google.android.youtube.player.YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?,
                ) {
                }
            })

        searchAdapter = SearchAdapter(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(item: Item) {
                val intent = Intent(this@VideoActivity, VideoActivity::class.java)
                intent.putExtra("item", item.id.videoId)
                intent.putExtra("title", item.snippet.title)
                intent.putExtra("desc", item.snippet.description)
                startActivity(intent)
                finish()
            }
        })

        appDatabase = AppDatabase.getInstance(binding.root.context)
        homeRoomAdapter =
            HomeRoomAdapter(object : HomeRoomAdapter.OnItemClickListener {
                override fun onItemClick(item: VideoHome) {
                    val intent = Intent(this@VideoActivity, VideoActivity::class.java)
                    intent.putExtra("item", item.videoId)
                    intent.putExtra("title", item.title)
                    intent.putExtra("desc", item.description)
                    startActivity(intent)
                    finish()
                }
            })
        setVide()
    }

    fun setVide() {
        if (NetworkHelper(binding.root.context).isNetworkConnected()) {
            viewModelSearch.getSearchLiveData()
                .observe(this, {
                    when (it.status) {
                        Status.LOADING -> {
                            binding.progress.visibility = View.VISIBLE
                        }
                        Status.ERROR -> {
                            binding.progress.visibility = View.INVISIBLE
                            Toast.makeText(this,
                                it.message,
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                        Status.SUCCESS -> {
                            binding.progress.visibility = View.INVISIBLE
                            searchAdapter.submitList(it.data?.items)
                        }
                    }
                })
            binding.rv.adapter = searchAdapter
        } else {
            Toast.makeText(binding.root.context,
                "Internet not connection!!!",
                Toast.LENGTH_SHORT)
                .show()
            binding.progress.visibility = View.GONE
            val roomList = appDatabase.homeDao().getVideoHome() as ArrayList<VideoHome>
            homeRoomAdapter.submitList(roomList)
            binding.rv.adapter = homeRoomAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        binding.likeCont.setOnClickListener {
            if (!islikeCount) {
                islikeCount = true
                binding.like.setImageResource(R.drawable.ic_liked)
                binding.dislike.setImageResource(R.drawable.ic_dislike)
                binding.likeTv.text = "${++likeCount}"
            } else {
                binding.like.setImageResource(R.drawable.ic_like)
                binding.likeTv.text = "${--likeCount}"
                islikeCount = false
            }
        }
        binding.dislikeCon.setOnClickListener {
            if (!islikeCount) {
                binding.dislike.setImageResource(R.drawable.ic_disliked)
                binding.dislikeTv.text = "${++disLikeCount}"
                binding.like.setImageResource(R.drawable.ic_like)
                islikeCount = true
            } else {
                binding.dislike.setImageResource(R.drawable.ic_dislike)
                binding.likeTv.text = "${--disLikeCount}"
                islikeCount = false
            }
        }
        binding.forward.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Youtube")
            intent.putExtra(Intent.EXTRA_TEXT,
                "https://www.youtube.com/watch?v=${videoId}")
            startActivity(Intent.createChooser(intent, "choose one"))
        }
        binding.savePlaylist.setOnClickListener {
            val appDatabase = AppDatabase.getInstance(binding.root.context)
            appDatabase.homeDao1().insert(videoHome)
            Toast.makeText(binding.root.context, "Success save playlist", Toast.LENGTH_SHORT).show()
        }
        binding.following.setOnClickListener {
            if (!isfollow){
                isfollow=true
                binding.following.setTextColor(Color.parseColor("#515151"))
                binding.following.textSize = 17F
            }else{
                binding.following.setTextColor(Color.parseColor("#ffcc0000"))
                binding.following.textSize = 20F
                isfollow=false
            }
        }
        commentAdapter = CommentAdapter()
        binding.comment.setOnClickListener {
            val bottomSheetDialog =
                BottomSheetDialog(this, R.style.ThemeOverlay_MaterialComponents_BottomSheetDialog)
            val mBottomSheetBinding = ItemBottomSheetBinding.inflate(layoutInflater, null, false)
            viewModelGetComment.getVideData()
                .observe(this, {
                    when (it.status) {
                        Status.LOADING -> {
                            binding.progress.visibility = View.VISIBLE
                        }
                        Status.ERROR -> {
                            binding.progress.visibility = View.INVISIBLE
                            Toast.makeText(this,
                                it.message,
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                        Status.SUCCESS -> {
                            binding.progress.visibility = View.INVISIBLE
                            commentAdapter.submitList(it.data?.items)
                        }
                    }
                })
            bottomSheetDialog.setContentView(mBottomSheetBinding.root)
            mBottomSheetBinding.rv.adapter = commentAdapter
            bottomSheetDialog.show()
        }
    }
}