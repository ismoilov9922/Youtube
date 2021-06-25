package uz.pdp.youtube.models.getComment

data class Item(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet
)