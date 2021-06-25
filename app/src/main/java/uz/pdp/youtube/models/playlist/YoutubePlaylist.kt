package uz.pdp.youtube.models.playlist

data class YoutubePlaylist(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo
)