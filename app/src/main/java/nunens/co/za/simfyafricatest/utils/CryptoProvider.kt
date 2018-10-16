package nunens.co.za.simfyafricatest.utils

import java.security.Provider


class CryptoProvider : Provider("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)") {
    /**
     * Creates a Provider and puts parameters
     */
    init {
        put("SecureRandom.SHA1PRNG",
                "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl")
        put("SecureRandom.SHA1PRNG ImplementedIn", "Software")
    }
}