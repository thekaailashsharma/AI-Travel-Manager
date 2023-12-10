package ai.travel.app.firestore

data class ProfileInfo(
    val name: String?,
    val phoneNumber: String?,
    val gender: String?,
    val imageUrl: String?,
) {
    constructor() : this("", "", "", "")
}