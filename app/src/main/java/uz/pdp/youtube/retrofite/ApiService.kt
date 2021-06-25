package uz.pdp.youtube.retrofite

import retrofit2.http.GET
import retrofit2.http.Query
import uz.pdp.youtube.models.getComment.GetComment
import uz.pdp.youtube.models.playlist.YoutubePlaylist
import uz.pdp.youtube.models.search.SearchVideo
import uz.pdp.youtube.models.videoData.VideoData
import uz.pdp.youtube.utils.DeveloperKey

interface ApiService {
    @GET("playlists")
    suspend fun getPlaylistVideo(
        @Query("key") key: String = DeveloperKey.KEY,
        @Query("part") part: String = "snippet,contentDetails",
        @Query("channelId") channelId: String = "UCr0y1P0-zH2o3cFJyBSfAKg",
        @Query("maxResults") maxResults: Int = 50,
    ): YoutubePlaylist

    @GET("search")
    suspend fun getSearch(
        @Query("key") key: String = DeveloperKey.KEY,
        @Query("part") part: String = "snippet",
        @Query("q") q: String = "android",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 50,
    ): SearchVideo

    @GET("videos")
    suspend fun getVideoData(
        @Query("key") key: String = DeveloperKey.KEY,
        @Query("id") id: String = "tIjt9hhcdRQ",
        @Query("part") part: String = "snippet,statistics",
    ): VideoData

    @GET("commentThreads")
    suspend fun getComment(
        @Query("key") key: String = DeveloperKey.KEY,
        @Query("videoId") id: String = "tIjt9hhcdRQ",
        @Query("part") part: String = "snippet",
        @Query("snippet.topLevelComment.snippet.textOriginal") comment: String = "",
    ): GetComment


}