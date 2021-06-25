package uz.pdp.youtube.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import uz.pdp.youtube.adapters.PlaylistAdapter
import uz.pdp.youtube.databinding.FragmentPlaylistBinding
import uz.pdp.youtube.models.playlist.Item
import uz.pdp.youtube.retrofite.ApiClient
import uz.pdp.youtube.utils.Status
import uz.pdp.youtube.viewModel.ViewModelFactory
import uz.pdp.youtube.viewModel.ViewModelPlaylist

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PlaylistFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var viewModelPlaylist: ViewModelPlaylist
    private lateinit var list: ArrayList<Item>
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaylistBinding.inflate(layoutInflater)
        list = ArrayList()
        playlistAdapter = PlaylistAdapter(object : PlaylistAdapter.OnItemClickListener {
            override fun onItemClick(item: Item) {

            }
        })
        viewModelPlaylist = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiClient.apiService)
        )[ViewModelPlaylist::class.java]

        viewModelPlaylist.getPlaylistLiveData().observe(viewLifecycleOwner, {
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
                    playlistAdapter.submitList(it.data?.items)
                }
            }
        })
        binding.rv.adapter = playlistAdapter
        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            PlaylistFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}