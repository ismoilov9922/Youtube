package uz.pdp.youtube.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.pdp.youtube.models.getComment.GetComment
import uz.pdp.youtube.repository.YoutubeRepository
import uz.pdp.youtube.retrofite.ApiService
import uz.pdp.youtube.utils.Resource

class ViewModelGetComment(private val apiService: ApiService) :
    ViewModel() {
    private val videoLiveComment = MutableLiveData<Resource<GetComment>>()
    private val youtubeRepository = YoutubeRepository(apiService)

    init {
        getComment()
    }

    private fun getComment() {
        viewModelScope.launch() {
            videoLiveComment.postValue(Resource.loading(null))
            youtubeRepository.getComment()
                .catch { e ->
                    videoLiveComment.postValue(Resource.error(e.message ?: "Error", null))
                }.collect {
                    videoLiveComment.postValue(Resource.success(it))
                }
        }
    }

    fun getVideData(): MutableLiveData<Resource<GetComment>> {
        return videoLiveComment
    }
}