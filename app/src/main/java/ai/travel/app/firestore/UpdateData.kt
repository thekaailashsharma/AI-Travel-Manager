package ai.travel.app.firestore

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

fun updateInfoToFirebase(
    context: Context,
    name: String?,
    phoneNumber: String?,
    gender: String?,
    imageUrl: String?,

    ) {
    val profile = ProfileInfo(
        name,
        phoneNumber,
        gender,
        imageUrl
    )

    val db = FirebaseFirestore.getInstance()
    phoneNumber?.let {
        db.collection("ProfileInfo").document(it).set(profile)
            .addOnSuccessListener {

                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Process Failed : " + exception.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

}
