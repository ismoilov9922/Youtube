package uz.pdp.youtube.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import uz.pdp.youtube.R
import uz.pdp.youtube.activity.VideoActivity
import uz.pdp.youtube.adapters.HomeRoomAdapter
import uz.pdp.youtube.adapters.SearchAdapter
import uz.pdp.youtube.database.AppDatabase
import uz.pdp.youtube.database.VideoHome
import uz.pdp.youtube.databinding.FragmentHomeBinding
import uz.pdp.youtube.isNetwork.NetworkHelper
import uz.pdp.youtube.models.search.Item
import uz.pdp.youtube.repository.YoutubeRepository
import uz.pdp.youtube.retrofite.ApiClient
import uz.pdp.youtube.sharPreference.YourPreference
import uz.pdp.youtube.utils.Status
import uz.pdp.youtube.viewModel.ViewModelFactory
import uz.pdp.youtube.viewModel.ViewModelSearch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModelHome: ViewModelSearch
    private lateinit var searAdapter: SearchAdapter
    private lateinit var homeRoomAdapter: HomeRoomAdapter
    private lateinit var videoList: ArrayList<Item>
    private lateinit var appDatabase: AppDatabase
    private lateinit var yourPreference: YourPreference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        videoList = ArrayList()
        appDatabase = AppDatabase.getInstance(binding.root.context)
        YoutubeRepository(ApiClient.apiService).AppModule(binding.root.context)
        viewModelHome = ViewModelProvider(this,
            ViewModelFactory(ApiClient.apiService))[ViewModelSearch::class.java]
        yourPreference = YourPreference.getInstance(binding.root.context)

        searAdapter = SearchAdapter(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(item: Item) {
                val intent = Intent(requireContext(), VideoActivity::class.java)
                intent.putExtra("item", item.id.videoId)
                startActivity(intent)
            }
        })
        appDatabase = AppDatabase.getInstance(binding.root.context)
        homeRoomAdapter =
            HomeRoomAdapter(object : HomeRoomAdapter.OnItemClickListener {
                override fun onItemClick(item: VideoHome) {
                    val intent = Intent(requireContext(), VideoActivity::class.java)
                    intent.putExtra("item", item.videoId)
                    startActivity(intent)
                }
            })
        setVideo()
        binding.searchView.setOnClickListener {
            val trasection: FragmentTransaction? = fragmentManager?.beginTransaction()
            trasection?.replace(R.id.frame_layout, SearchingFragment())
            trasection?.commit()
        }
        return binding.root
    }

    private fun setVideo() {
        if (NetworkHelper(binding.root.context).isNetworkConnected()) {
            viewModelHome.getSearchLiveData().observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        binding.progress.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        binding.progress.visibility = View.INVISIBLE
                        searAdapter.submitList(it.data?.items)
                        var list = ArrayList<VideoHome>()
                        it.data?.items?.forEach { item ->
                            val videoHome = VideoHome(item.id.videoId,
                                item.snippet.title,
                                item.snippet.description,
                                item.snippet.thumbnails.high.url)
                            list.add(videoHome)
                        }
                        appDatabase.homeDao().insertAll(list)
                    }
                }
            })
            binding.rv.adapter = searAdapter
        } else {
            binding.progress.visibility = View.GONE
            var roomList = appDatabase.homeDao().getVideoHome() as ArrayList<VideoHome>
            homeRoomAdapter.submitList(roomList)
            binding.rv.adapter = homeRoomAdapter
        }
    }

    override fun onResume() {
        super.onResume()
    }
    fun getDate(): String? {
        val currentDate: Date = Calendar.getInstance().time
        val format: DateFormat = SimpleDateFormat("yyyy/MM/dd/kk:mm:ss")
        return format.format(currentDate)
    }
}