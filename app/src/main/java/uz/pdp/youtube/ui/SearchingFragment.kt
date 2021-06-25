package uz.pdp.youtube.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.pdp.youtube.R
import uz.pdp.youtube.activity.MainActivity
import uz.pdp.youtube.adapters.SimpleAdapter
import uz.pdp.youtube.databinding.FragmentSearchingBinding
import uz.pdp.youtube.sharPreference.YourPreference
import uz.pdp.youtube.utils.IOnBackPressed
import java.lang.reflect.Type

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentSearchingBinding
    lateinit var yourPreference: YourPreference
    lateinit var simpleAdapter: SimpleAdapter
    lateinit var list: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchingBinding.inflate(layoutInflater)
        yourPreference = YourPreference.getInstance(binding.root.context)
        val jsonStr = yourPreference.getData("searchlist")
        if (jsonStr.isEmpty()) {
            list = ArrayList()
        } else {
            val type: Type = object : TypeToken<List<String>>() {}.type
            list = Gson().fromJson(jsonStr, type)
        }
        simpleAdapter = SimpleAdapter(list = list)
        binding.rv.adapter = simpleAdapter
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                yourPreference.saveData("search", query.toString())
                val trasection: FragmentTransaction? = fragmentManager?.beginTransaction()
                trasection?.replace(R.id.frame_layout, HomeFragment())
                trasection?.commit()
                if (jsonStr.isEmpty()) {
                    list = ArrayList()
                } else {
                    val type: Type = object : TypeToken<List<String>>() {}.type
                    list = Gson().fromJson(jsonStr, type)
                }
                list.add(query.toString())
                val toJson = Gson().toJson(list)
                yourPreference.saveData("searchlist", toJson)
                return true
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity?)?.hideBottomMenu()
    }

    override fun onStop() {
        super.onStop()
        val trasection: FragmentTransaction? = fragmentManager?.beginTransaction()
        trasection?.replace(R.id.frame_layout, HomeFragment())
        trasection?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity?)?.showBottomMenu()
    }

}