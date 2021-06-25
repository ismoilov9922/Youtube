package uz.pdp.youtube.ui

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import uz.pdp.youtube.R
import uz.pdp.youtube.activity.VideoActivity
import uz.pdp.youtube.adapters.OnItemClickListener
import uz.pdp.youtube.adapters.RvAdapter
import uz.pdp.youtube.database.AppDatabase
import uz.pdp.youtube.database.VideoHome
import uz.pdp.youtube.database.VideoHome1
import uz.pdp.youtube.databinding.FragmentProfileBinding
import java.lang.Appendable

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentProfileBinding
    lateinit var appendable: AppDatabase
    private lateinit var list: ArrayList<VideoHome1>
    private lateinit var rvAdapter: RvAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        appendable = AppDatabase.getInstance(binding.root.context)
        binding.imageView3.setImageResource(R.drawable.android)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_app, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}