package uz.pdp.youtube.repository

import android.content.Context
import android.support.annotation.NonNull
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.flow
import uz.pdp.youtube.retrofite.ApiService
import uz.pdp.youtube.sharPreference.YourPreference

class YoutubeRepository(var apiService: ApiService) {
    private var context: Context? = null
    private var channelId: String = "UCr0y1P0-zH2o3cFJyBSfAKg"

    fun AppModule(@NonNull context: Context?) {
        this.context = context
    }

    suspend fun getPlaylist() =
        flow { emit(apiService.getPlaylistVideo(channelId = channelId)) }

    suspend fun getSearch() =
        flow {
            val yourPreference = YourPreference.getInstance(context)
            var search = yourPreference.getData("search")
            emit(apiService.getSearch(q = search))
        }

    suspend fun getVideData() =
        flow {
            val yourPreference = YourPreference.getInstance(context)
            var videoId = yourPreference.getData("videoId")
            emit(apiService.getVideoData(id = videoId))
        }

    suspend fun getComment() =
        flow {
            val yourPreference = YourPreference.getInstance(context)
            var videoId = yourPreference.getData("videoId")
            emit(apiService.getComment(id = videoId))
        }
}