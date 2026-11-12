package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest

class AdminAuthManager(context: Context) {

  private val prefs: SharedPreferences =
    context.getSharedPreferences("it_learn_admin_prefs", Context.MODE_PRIVATE)

  private val _isAdminLoggedIn = MutableStateFlow(false)
  val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

  companion object {
    private const val KEY_ADMIN_HASH = "admin_passcode_hash"
    private const val SALT = "IT_LEARN_C23_SECURE_SALT_2026"
    // Default admin passcode: "c23admin"
    private const val DEFAULT_PASSCODE = "c23admin"
  }

  init {
    if (!prefs.contains(KEY_ADMIN_HASH)) {
      // Initialize with default secure hash
      val defaultHash = hashPasscode(DEFAULT_PASSCODE)
      prefs.edit().putString(KEY_ADMIN_HASH, defaultHash).apply()
    }
  }

  fun authenticate(passcode: String): Boolean {
    val storedHash = prefs.getString(KEY_ADMIN_HASH, null) ?: return false
    val inputHash = hashPasscode(passcode)
    val success = storedHash == inputHash
    if (success) {
      _isAdminLoggedIn.value = true
    }
    return success
  }

  fun logout() {
    _isAdminLoggedIn.value = false
  }

  fun updatePasscode(oldPasscode: String, newPasscode: String): Boolean {
    if (authenticate(oldPasscode)) {
      if (newPasscode.length >= 6) {
        val newHash = hashPasscode(newPasscode)
        prefs.edit().putString(KEY_ADMIN_HASH, newHash).apply()
        return true
      }
    }
    return false
  }

  private fun hashPasscode(passcode: String): String {
    val bytes = MessageDigest.getInstance("SHA-256")
      .digest((passcode + SALT).toByteArray(Charsets.UTF_8))
    return bytes.joinToString("") { "%02x".format(it) }
  }
}
