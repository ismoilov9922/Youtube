package uz.pdp.youtube.models.search

data class Item(
    val etag: String,
    val id: Id,
    val kind: String,
    val snippet: Snippet
)