package com.mxcrtr.notepadtest

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import java.lang.Exception
import java.lang.RuntimeException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

class MainActivity : AppCompatActivity() {

    private val AndroidKeyStore = "AndroidKeyStore"
    private val KeyName = "example_key"

    private var fingerprintManager : FingerprintManager ? = null
    private var keyguardManager : KeyguardManager ? = null
    private var keyStore : KeyStore? = null
    private var keyGenerator : KeyGenerator? = null

    private var cipher: Cipher? = null
    private var cryptoObject: FingerprintManager.CryptoObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (getManager()) {
            generateKey()
            if (initCipher()) {
                cipher?.let {
                    cryptoObject = FingerprintManager.CryptoObject(it)

                    val helper = FingerprintHandler(this, this)

                    if (fingerprintManager != null && cryptoObject != null) {
                        helper.startAuth(fingerprintManager!!, cryptoObject!!)
                    }

                    Toast.makeText(this, "Fingerprint required to access homework", Toast.LENGTH_LONG).show()

                }
            }
        }

    }

    private fun getManager() : Boolean {
        keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        fingerprintManager = getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager

        if (keyguardManager?.isKeyguardSecure == false) {
            Toast.makeText(this, "Lock screen not secure", Toast.LENGTH_SHORT).show()
            return false
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Fingerprint permission is not enabled", Toast.LENGTH_SHORT).show()
            return false;
        }

        if (fingerprintManager?.hasEnrolledFingerprints() == false) {
            Toast.makeText(this, "No registered fingerprints found", Toast.LENGTH_SHORT).show()
            return false
        }
        return true;

    }

    private fun generateKey() {
        try {
            keyStore = KeyStore.getInstance(AndroidKeyStore)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore)
        } catch (ex: Exception) {
            throw RuntimeException("Failed to get KeyGenerator instance " + ex)
        }

        try {
            keyStore?.load(null)
            keyGenerator?.init(KeyGenParameterSpec.Builder(KeyName, KeyProperties.PURPOSE_ENCRYPT or
                    KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC).setUserAuthenticationRequired(true).
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build())
            keyGenerator?.generateKey()
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }

    }

    private fun initCipher() : Boolean {
        try {
            cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }

        try {
            keyStore?.load(null)
            val key = keyStore?.getKey(KeyName, null) as SecretKey
            cipher?.init(Cipher.ENCRYPT_MODE, key)
            return true
        } catch (ex: Exception) {
            return false
        }

    }

    fun onFingerprintSuccess() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

}
