package uz.pdp.youtube.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.pdp.youtube.models.videoData.VideoData
import uz.pdp.youtube.repository.YoutubeRepository
import uz.pdp.youtube.retrofite.ApiService
import uz.pdp.youtube.utils.Resource

class ViewModelVideo(private val apiService: ApiService) :
    ViewModel() {
    private val videoLiveData = MutableLiveData<Resource<VideoData>>()
    private val youtubeRepository = YoutubeRepository(apiService)

    init {
        getVideoData()
    }

    private fun getVideoData() {
        viewModelScope.launch {
            videoLiveData.postValue(Resource.loading(null))
            youtubeRepository.getVideData()
                .catch { e ->
                    videoLiveData.postValue(Resource.error(e.message ?: "Error", null))
                }.collect {
                    videoLiveData.postValue(Resource.success(it))
                }
        }
    }

    fun getVideData(): MutableLiveData<Resource<VideoData>> {

        return videoLiveData
    }
}