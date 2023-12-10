package ai.travel.app.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.datastore: DataStore<Preferences> by preferencesDataStore("pref")

class UserDatastore(private val context: Context) {
    companion object {
        val phoneNumber = stringPreferencesKey("phoneNumber")
        val userName = stringPreferencesKey("userName")
        val gender = stringPreferencesKey("gender")
        val isLoggedIn = booleanPreferencesKey("login")
        val pfp = stringPreferencesKey("pfp")
    }

    val getNumber: Flow<String> = context.datastore.data.map {
        it[phoneNumber] ?: ""
    }

    suspend fun saveNumber(number: String) {
        context.datastore.edit {
            it[phoneNumber] = number
        }
    }

    val getPfp: Flow<String> = context.datastore.data.map {
        it[pfp] ?: ""
    }

    suspend fun savePfp(pfp: String) {
        context.datastore.edit {
            it[UserDatastore.pfp] = pfp
        }
    }

    suspend fun saveLoginStatus(status: Boolean) {
        context.datastore.edit {
            it[isLoggedIn] = status
        }
    }

    val getLoginStatus: Flow<Boolean> = context.datastore.data.map {
        it[isLoggedIn] ?: false
    }

    val getGender: Flow<String> = context.datastore.data.map {
        it[gender] ?: ""
    }

    suspend fun saveGender(userGender: String) {
        context.datastore.edit {
            it[gender] = userGender
        }
    }


    suspend fun saveName(name: String) {
        context.datastore.edit {
            it[userName] = name
        }
    }


    val getName: Flow<String> = context.datastore.data.map {
        it[userName] ?: ""
    }
}