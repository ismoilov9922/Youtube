package uz.pdp.youtube.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.pdp.youtube.R
import uz.pdp.youtube.databinding.ActivityMainBinding
import uz.pdp.youtube.sharPreference.YourPreference
import uz.pdp.youtube.ui.HomeFragment
import uz.pdp.youtube.ui.PlaylistFragment
import uz.pdp.youtube.ui.ProfileFragment
import uz.pdp.youtube.ui.SearchFragment
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val playlistFragment = PlaylistFragment()
    private val searchFragment = SearchFragment()
    private val profileFragment = ProfileFragment()
    private lateinit var binding: ActivityMainBinding
    private lateinit var yourPreference: YourPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setLogo(R.drawable.ic_youtube)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        setFragment(homeFragment)
        yourPreference = YourPreference.getInstance(binding.root.context)
        val list = arrayListOf<String>("yangiliklar",
            "world news", "uzbek kinolari", "sport", "android",
            "tarjima kinolar", "multik", "panda multifilm")
        yourPreference.saveData("search", list[Random.nextInt(0, 8)])
        binding.menuBottom.selectedItemId = R.id.menu_bottom
        binding.menuBottom.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (item.isChecked) {
                    return true
                } else {
                    when (item.itemId) {
                        R.id.home -> {
                            setFragment(homeFragment)
                            supportActionBar?.title = "Home"
                            return true
                        }
                        R.id.playlist -> {
                            setFragment(playlistFragment)
                            supportActionBar?.title = "Playlist"
                            return true
                        }
                        R.id.search -> {
                            setFragment(searchFragment)
                            supportActionBar?.title = "Search"
                            return true
                        }
                        R.id.profile -> {
                            setFragment(profileFragment)
                            supportActionBar?.title = "Profile"
                            return true
                        }
                    }
                }
                return true
            }
        })
    }

    private fun setFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame_layout, fragment)
        ft.commit()
    }

    fun hideBottomMenu() {
        binding.bottomLayout.visibility = View.GONE
    }

    fun showBottomMenu() {
        binding.bottomLayout.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}