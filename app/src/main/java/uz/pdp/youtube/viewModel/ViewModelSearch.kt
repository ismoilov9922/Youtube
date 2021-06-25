package uz.pdp.youtube.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.pdp.youtube.models.search.SearchVideo
import uz.pdp.youtube.repository.YoutubeRepository
import uz.pdp.youtube.retrofite.ApiService
import uz.pdp.youtube.utils.Resource

class ViewModelSearch(private val apiService: ApiService) :
    ViewModel() {
    private val searchLiveData = MutableLiveData<Resource<SearchVideo>>()
    private val youtubeRepository = YoutubeRepository(apiService)

    init {
        getSearch()
    }

    private fun getSearch() {
        viewModelScope.launch(Dispatchers.Default) {
            searchLiveData.postValue(Resource.loading(null))
            youtubeRepository.getSearch()
                .catch { e ->
                    searchLiveData.postValue(Resource.error(e.message ?: "Error", null))
                }.collect {
                    searchLiveData.postValue(Resource.success(it))
                }
        }
    }

    fun getSearchLiveData(): MutableLiveData<Resource<SearchVideo>> {
        return searchLiveData
    }
}