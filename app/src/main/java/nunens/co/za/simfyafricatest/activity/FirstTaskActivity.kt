package nunens.co.za.simfyafricatest.activity

import android.app.ProgressDialog
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import com.android.volley.VolleyError
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_first_tast.*
import menolla.co.za.itsi_test.network.NetworkCheck
import menolla.co.za.itsi_test.utils.ToastUtil
import nunens.co.za.simfyafricatest.App
import nunens.co.za.simfyafricatest.R
import nunens.co.za.simfyafricatest.database.model.*
import nunens.co.za.simfyafricatest.listener.VolleyListener
import nunens.co.za.simfyafricatest.network.VolleyUtil
import nunens.co.za.simfyafricatest.utils.CryptoProvider
import org.apache.commons.io.FileUtils
import org.jetbrains.anko.progressDialog
import org.json.JSONObject
import java.io.File
import java.io.OutputStreamWriter
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec


class FirstTaskActivity : AppCompatActivity(), VolleyListener {
    val TAG = "FirstTaskActivity"
    var internalFilePath: String = ""
    var externalFilePath: String = ""
    lateinit var dialog: ProgressDialog
    override fun onResponse(resp: String?) {
        //convert my response to a JSONObject for parsing
        val resp = JSONObject(resp)
        Log.e("FirstTaskActivity", resp.toString())
        var respObj: TaskOneModel? = TaskOneModel(Integer.parseInt(number.text.toString()))
        respObj!!.name = resp.getString("name")
        respObj!!.abilities = resp.getJSONArray("abilities").toString()
        respObj!!.moves = resp.getJSONArray("moves").toString()
        respObj!!.stats = resp.getJSONArray("stats").toString()
        respObj!!.types = resp.getJSONArray("types").toString()
        respObj!!.weight = resp.getInt("weight")
        respObj!!.height = resp.getInt("height")
        App!!.db!!.taskOneDAO().insert(respObj)

        val fileout = openFileOutput(number.text.toString() + ".txt", Context.MODE_PRIVATE)

        val outputWriter = OutputStreamWriter(fileout)

        val sb = StringBuilder()

        sb.append("\n\n")
        sb.append("Name: " + respObj!!.name)
        sb.append("\n\n")
        sb.append("Weight: " + respObj!!.weight)
        sb.append("\n\n")
        sb.append("Height: " + respObj!!.height)
        sb.append("\n\n")
        sb.append("Stats: ")
        sb.append("\n")
        val statsList = Gson().fromJson(respObj!!.stats, StatList::class.java)
        for (i in 0 until statsList.size) {
            val stat = statsList[i]
            sb.append("Stat($i): ")
            sb.append("\n")
            sb.append("Base Stat: ${stat.base_stat}")
            sb.append("\n")
            sb.append("Effort: ${stat.effor}")
            sb.append("\n")
            sb.append("Stat Name: ${stat.stat!!.name}")
            sb.append("\n")
            sb.append("Stat URL: ${stat.stat!!.url}")
        }
        sb.append("\n\n")
        sb.append("Abilities: ")
        sb.append("\n")
        val abilityList = Gson().fromJson(respObj!!.abilities, AbilityList::class.java)
        for (i in 0 until abilityList.size) {
            val ability = abilityList[i]
            sb.append("Ability(${i + 1}): ")
            sb.append("\n")
            sb.append("Is Hidden: ${ability.is_hidden}")
            sb.append("\n")
            sb.append("Slot: ${ability.slot}")
            sb.append("\n")
            sb.append("Ability Name: ${ability.ability!!.name}")
            sb.append("\n")
            sb.append("Ability URL: ${ability.ability!!.url}")
        }
        sb.append("\n\n")
        sb.append("Moves: ")
        sb.append("\n")
        val movesList = Gson().fromJson(respObj!!.moves, MoveList::class.java)
        for (i in 0 until movesList.size) {
            val move = movesList[i]
            sb.append("Move(${i + 1}): ")
            sb.append("\n")
            sb.append("Move Name: ${move.move!!.name}")
            sb.append("\n")
            sb.append("Move URL: ${move.move!!.url}")
            sb.append("\n")
            for (i in 0 until move.version_group_details!!.size) {
                val move = move.version_group_details!![i]
                sb.append("Version Group Detail:\n")
                sb.append("Level learned at: ${move.level_learned_at}")
                sb.append("\n")
                sb.append("Move Learn Method Name: ${move.move_learn_method!!.name}")
                sb.append("\n")
                sb.append("Move Learn Method URL: ${move.move_learn_method!!.url}")
                sb.append("\n")
                sb.append("Version Group Name: ${move.version_group!!.name}")
                sb.append("\n")
                sb.append("Version Group URL: ${move.move_learn_method!!.name}")
            }
        }
        sb.append("\n\n")
        sb.append("Abilities: ")
        sb.append("\n")
        val typeList = Gson().fromJson(respObj!!.types, TypeList::class.java)
        for (i in 0 until typeList.size) {
            val type = typeList[i]
            sb.append("Ability(${i + 1}): ")
            sb.append("\n")
            sb.append("Slot: ${type.slot}")
            sb.append("\n")
            sb.append("Type Name: ${type.type!!.name}")
            sb.append("\n")
            sb.append("Type URL: ${type.type!!.url}")
        }
        sb.append("\n\n")
        Log.e("FILE:", sb.toString())
        outputWriter.write(sb.toString())
        outputWriter.flush()
        outputWriter.close()
        dialog.dismiss()
    }

    override fun onError(volleyError: VolleyError?) {
        dialog.dismiss()
        ToastUtil.errorToast(applicationContext, "Unknown error occurred")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_tast)

        changeButtonsState(false)
        getDeviceEncryptionStatus()
        dialog = progressDialog(message = "Please wait a bit…", title = "Fetching data")
        dialog.isIndeterminate = true
        dialog.setCancelable(false)
        dialog.show()
        dialog.dismiss()
        initCollapsingToolbar()
        number.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (number.text.toString() != "") {
                    validateNumber()
                } else {
                    changeButtonsState(false)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        create.setOnClickListener {
            val file = File(externalFilePath)
            val internalFile = File(internalFilePath)
            if (file.exists() || internalFile.exists()) {
                ToastUtil.toast(applicationContext, "File already exists")
            } else {
                //todo: call this function on volley response ->
                if (NetworkCheck.checkWifi(applicationContext) || NetworkCheck.checkMobileNetwork(applicationContext)) {
                    dialog.show()
                    VolleyUtil.firstRequest(applicationContext, number.text.toString() + "/", this)
                } else {
                    ToastUtil.noNetworkToast(applicationContext)
                }
            }
        }
        delete.setOnClickListener {
            deleteFiles()
        }

        move.setOnClickListener {
            val file = File(externalFilePath)
            if (file.exists()) {
                Log.e(TAG, "Move external")
                moveFile(externalFilePath, internalFilePath)
            } else {
                Log.e(TAG, "Move internal")
                moveFile(internalFilePath, externalFilePath)
            }
        }
        encrypt.setOnClickListener {
            val file = File(externalFilePath)
            val internalFile = File(internalFilePath)
            if (file.exists()) {
                getDeviceEncryptionStatus(file)
            } else {
                getDeviceEncryptionStatus(internalFile)
            }

        }
        reset.setOnClickListener {
            deleteFiles()

            synchronized(this) {
                val list = App!!.db!!.taskOneDAO().getAll()
                for (i in 0 until list.size) {
                    val model: TaskOneModel = list[i]
                    val internal = applicationContext.filesDir.toString() + "/" + model.id + ".txt"
                    val external = Environment.getExternalStorageDirectory().toString() + "/" + model.id + ".txt"
                    deleteFiles(internal, external)
                }
            }
            App!!.db!!.clearAllTables()
            number.setText("")
            ToastUtil.toast(applicationContext, "App successfully reset...")
        }
    }

    private fun deleteFiles() {
        var status = false
        try {
            FileUtils.deleteDirectory(File(externalFilePath))
        } catch (e: Exception) {
            e.printStackTrace()
            status = true
        }
        status = try {
            FileUtils.deleteDirectory(File(internalFilePath))
            false
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
        if (status) {
            ToastUtil.errorToast(applicationContext, "Error deleting files")
        } else {
            App.db!!.taskOneDAO().delete(Integer.parseInt(number.text.toString()))
            ToastUtil.toast(applicationContext, "File successfully deleted...")
        }
    }

    private fun deleteFiles(internal: String, external: String) {
        var status = false
        try {
            FileUtils.deleteDirectory(File(external))
        } catch (e: Exception) {
            e.printStackTrace()
            status = true
        }
        status = try {
            FileUtils.deleteDirectory(File(internal))
            false
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

    private fun changeButtonsState(state: Boolean) {
        create.isEnabled = state
        delete.isEnabled = state
        move.isEnabled = state
        encrypt.isEnabled = state
    }

    fun validateNumber(): Boolean {
        try {
            var value = Integer.parseInt(number.text.toString())
            if (value < 1 || value > 100) {
                ToastUtil.errorToast(applicationContext, "Please enter number between 1 and 100")
                number.setText("")
                changeButtonsState(false)
                return false
            }
        } catch (e: NumberFormatException) {
            ToastUtil.errorToast(applicationContext, "Please enter a valid number")
            number.setText("")
            changeButtonsState(false)
            return false
        }
        changeButtonsState(true)
        //internalFilePath = "/mnt/sdcard/Android/data/" + applicationContext.packageName + "/files/" + number.text.toString() + ".txt"
        internalFilePath = applicationContext.filesDir.toString() + "/" + number.text.toString() + ".txt"

        externalFilePath = Environment.getExternalStorageDirectory().toString() + "/" + number.text.toString() + ".txt"
        return true
    }

    private fun initCollapsingToolbar() {
        val collapsingToolbar = findViewById<View>(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        collapsingToolbar.title = " "
        appbar.setExpanded(true)

        // hiding & showing the title when toolbar expanded & collapsed
        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    titletext.visibility = View.VISIBLE
                    collapsingToolbar.title = "Task 1"
                    isShow = true
                } else if (isShow) {
                    titletext.visibility = View.GONE
                    collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    fun moveFile(src: String, des: String) {
        Log.e(TAG, "Source: $src")
        Log.e(TAG, "Destination: $des")
        val srcDir = File(src)
        val destDir = File(des.substring(0, des.lastIndexOf('/')))
        try {
            FileUtils.moveFileToDirectory(srcDir, destDir, true)
            ToastUtil.toast(applicationContext, "File successfully moved...")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDeviceEncryptionStatus() {

        var status = DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED

        if (Build.VERSION.SDK_INT >= 11) {
            val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            if (dpm != null) {
                status = dpm.storageEncryptionStatus
            }
        }
        if (status == 3 || status == 4) {
            encrypt.text = "Decrypt"
        } else {
            encrypt.text = "Encrypt"
        }
    }

    private fun getDeviceEncryptionStatus(file: File) {

        var status = DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED

        if (Build.VERSION.SDK_INT >= 11) {
            val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            if (dpm != null) {
                status = dpm.storageEncryptionStatus
            }
        }
        Log.e(TAG, "Encryption status $status")
        if (status == 3 || status == 4) {
            try {
                decrypt(file.readBytes())
            } catch (i: IllegalArgumentException) {
                ToastUtil.errorToast(applicationContext, "Error decrypting file")
            } catch (e: Exception) {
                ToastUtil.errorToast(applicationContext, "Error decrypting file")
            }
        } else {
            try {
                encrypt(file.readBytes())
            } catch (i: IllegalArgumentException) {
                ToastUtil.errorToast(applicationContext, "Error decrypting file")
            } catch (e: Exception) {
                ToastUtil.errorToast(applicationContext, "Error decrypting file")
            }
        }
    }

    @Throws(Exception::class)
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
    fun encrypt(message: ByteArray): String {
        val encodedString = Base64.encode(message, Base64.DEFAULT)
        val salt = generateKey("simfyAfrica")
        val key = SecretKeySpec(salt, "AES")
        val c = Cipher.getInstance("AES")
        c.init(Cipher.ENCRYPT_MODE, key)
        val encVal = c.doFinal(encodedString)
        ToastUtil.toast(applicationContext, "File successfully encrypted...")
        return Base64.encodeToString(encVal, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    fun decrypt(message: ByteArray): String {
        val salt = generateKey("simfyAfrica")
        val c = Cipher.getInstance("AES")
        val key = SecretKeySpec(salt, "AES")
        c.init(Cipher.DECRYPT_MODE, key)
        val decordedValue = Base64.decode(message, Base64.DEFAULT)
        val decValue = c.doFinal(decordedValue)
        val decryptedValue = String(decValue)
        ToastUtil.toast(applicationContext, "File successfully decrypted...")
        return String(Base64.decode(decryptedValue, Base64.DEFAULT))
    }

    override fun onBackPressed() {
        finish()
    }
}
