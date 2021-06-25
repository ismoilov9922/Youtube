package uz.pdp.youtube.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.pdp.youtube.models.playlist.YoutubePlaylist
import uz.pdp.youtube.repository.YoutubeRepository
import uz.pdp.youtube.retrofite.ApiService
import uz.pdp.youtube.utils.Resource

class ViewModelPlaylist(private val apiService: ApiService) : ViewModel() {

    private val playlistLiveData = MutableLiveData<Resource<YoutubePlaylist>>()
    private val youtubeRepository = YoutubeRepository(apiService)

    init {
        getPlaylist()
    }

    private fun getPlaylist() {
        viewModelScope.launch {
            playlistLiveData.postValue(Resource.loading(null))
            youtubeRepository.getPlaylist()
                .catch { e ->
                    playlistLiveData.postValue(Resource.error(e.message ?: "Error", null))
                }.collect {
                    playlistLiveData.postValue(Resource.success(it))
                }
        }
    }

    fun getPlaylistLiveData(): MutableLiveData<Resource<YoutubePlaylist>> {
        return playlistLiveData
    }
}