package uz.pdp.youtube.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.pdp.youtube.retrofite.ApiService

class ViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelPlaylist::class.java)) {
            return ViewModelPlaylist(apiService) as T
        }
        if (modelClass.isAssignableFrom(ViewModelSearch::class.java)) {
            return ViewModelSearch(apiService) as T
        }
        if (modelClass.isAssignableFrom(ViewModelVideo::class.java)) {
            return ViewModelVideo(apiService) as T
        }
        if (modelClass.isAssignableFrom(ViewModelGetComment::class.java)) {
            return ViewModelGetComment(apiService) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}