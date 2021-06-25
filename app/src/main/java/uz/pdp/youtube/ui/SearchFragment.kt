package uz.pdp.youtube.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import uz.pdp.youtube.activity.VideoActivity
import uz.pdp.youtube.adapters.OnItemClickListener
import uz.pdp.youtube.adapters.RvAdapter
import uz.pdp.youtube.database.AppDatabase
import uz.pdp.youtube.database.VideoHome1
import uz.pdp.youtube.databinding.FragmentSearchBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentSearchBinding
    lateinit var appendable: AppDatabase
    private lateinit var list: ArrayList<VideoHome1>
    private lateinit var rvAdapter: RvAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        appendable = AppDatabase.getInstance(binding.root.context)
        list = ArrayList()
        list = appendable.homeDao1().getVideoHome1() as ArrayList<VideoHome1>
        rvAdapter = RvAdapter(list, object : OnItemClickListener {
            override fun onItemClick(item: VideoHome1) {
                val intent = Intent(requireContext(), VideoActivity::class.java)
                intent.putExtra("item", item.videoId)
                startActivity(intent)
            }
        })
        binding.rv.adapter = rvAdapter
        return binding.root
    }
}