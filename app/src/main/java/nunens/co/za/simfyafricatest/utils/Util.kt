package nunens.co.za.simfyafricatest.utils

import android.util.Base64
import org.apache.commons.io.FileUtils
import java.io.File
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

class Util {
    companion object {
        @Throws(Exception::class)
        //Method for generating secret key
        fun generateKey(password: String): ByteArray {
            val keyStart = password.toByteArray(charset("UTF-8"))

            val kgen = KeyGenerator.getInstance("AES")
            val sr = SecureRandom.getInstance("SHA1PRNG", CryptoProvider())
            sr.setSeed(keyStart)
            kgen.init(128, sr)
            val skey = kgen.generateKey()
            return skey.encoded
        }

        @Throws(Exception::class)
        //method for encrypting
        fun encrypt(message: ByteArray): String {
            val encodedString = Base64.encode(message, Base64.DEFAULT)
            val salt = generateKey("simfyAfrica")
            val key = SecretKeySpec(salt, "AES")
            val c = Cipher.getInstance("AES")
            c.init(Cipher.ENCRYPT_MODE, key)
            val encVal = c.doFinal(encodedString)
            return Base64.encodeToString(encVal, Base64.DEFAULT)
        }

        @Throws(Exception::class)
        //method for decrypting
        fun decrypt(message: ByteArray): String {
            val salt = generateKey("simfyAfrica")
            val c = Cipher.getInstance("AES")
            val key = SecretKeySpec(salt, "AES")
            c.init(Cipher.DECRYPT_MODE, key)
            val decordedValue = Base64.decode(message, Base64.DEFAULT)
            val decValue = c.doFinal(decordedValue)
            val decryptedValue = String(decValue)
            return String(Base64.decode(decryptedValue, Base64.DEFAULT))
        }

        //function for moving files
        fun moveFile(src: String, des: String) {
            val srcDir = File(src)
            val destDir = File(des.substring(0, des.lastIndexOf('/')))
            try {
                FileUtils.moveFileToDirectory(srcDir, destDir, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}