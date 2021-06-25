package uz.pdp.youtube.models.getComment

data class GetComment(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo
)