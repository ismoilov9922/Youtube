package uz.pdp.youtube.models.getComment

data class TopLevelComment(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: SnippetX
)