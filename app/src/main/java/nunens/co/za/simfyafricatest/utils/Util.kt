package nunens.co.za.simfyafricatest.utils

import org.apache.commons.io.FileUtils
import java.io.File

class Util {
    companion object {

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