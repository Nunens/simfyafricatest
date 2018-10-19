package nunens.co.za.simfyafricatest.activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
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
import nunens.co.za.simfyafricatest.utils.Encryption
import nunens.co.za.simfyafricatest.utils.Util
import org.apache.commons.io.FileUtils
import org.jetbrains.anko.progressDialog
import org.json.JSONObject
import java.io.File
import java.io.OutputStreamWriter


class FirstTaskActivity : AppCompatActivity(), VolleyListener {
    //declarations
    val TAG = "FirstTaskActivity"
    var internalFilePath: String = ""
    var externalFilePath: String = ""
    lateinit var dialog: ProgressDialog
    val string_encoding = "UTF-8"

    //everride method of successful response from volley
    override fun onResponse(resp: String?) {
        //convert my response to a JSONObject for parsing
        val resp = JSONObject(resp)
        createFile(resp)
        ToastUtil.toast(applicationContext, "File successfully created")
        dialog.dismiss()
    }

    //override method of volley error
    override fun onError(volleyError: VolleyError?) {
        dialog.dismiss()
        ToastUtil.errorToast(applicationContext, "Unknown error occurred")
    }

    private fun checkEncryptionStatus() {
        encrypt.text = "Decrypt"
        val arr = ByteArray(16)
        var fileRead: String? = ""
        val file = File(externalFilePath)
        Log.e(TAG, "External File path: ${file.absoluteFile}")
        val internalFile = File(internalFilePath)
        Log.e(TAG, "Internal File path: ${internalFile.absoluteFile}")
        val encryption = Encryption!!.getDefault("Key", "Salt", arr)
        if (file.exists()) {
            fileRead = FileUtils.readFileToString(file, string_encoding)
            val decryptedFile = encryption!!.decryptOrNull(fileRead!!)
            if (decryptedFile == null)
                encrypt.text = "Encrypt"
            Log.e(TAG, "Decrypted 1: $decryptedFile")
        } else {
            fileRead = FileUtils.readFileToString(internalFile, string_encoding)
            val decryptedFile = encryption!!.decryptOrNull(fileRead!!)
            if (decryptedFile == null)
                encrypt.text = "Encrypt"
            Log.e(TAG, "Decrypted 2: $decryptedFile")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_tast)

        changeButtonsState(false)
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
                    dialog.dismiss()
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
                Util.moveFile(externalFilePath, internalFilePath)
                ToastUtil.toast(applicationContext, "File successfully moved...")
            } else {
                Log.e(TAG, "Move internal")
                Util.moveFile(internalFilePath, externalFilePath)
                ToastUtil.toast(applicationContext, "File successfully moved...")
            }
        }
        encrypt.setOnClickListener {
            val arr = ByteArray(16)
            var fileRead: String?
            val file = File(externalFilePath)
            val internalFile = File(internalFilePath)
            val encryption = Encryption!!.getDefault("Key", "Salt", arr)
            if (encrypt.text == "Encrypt") {
                if (file.exists()) {
                    fileRead = FileUtils.readFileToString(file, string_encoding)
                    val encryptedFile = encryption!!.encryptOrNull(fileRead!!)
                    Log.e(TAG, "Encrypted 1: $encryptedFile")
                    FileUtils.writeStringToFile(file, encryptedFile, string_encoding)
                    ToastUtil.toast(applicationContext, "File encrypted successfully")
                    encrypt.text = "Decrypt"
                } else {
                    fileRead = FileUtils.readFileToString(internalFile, string_encoding)
                    val encryptedFile = encryption!!.encryptOrNull(fileRead!!)
                    Log.e(TAG, "Encrypted 2: $encryptedFile")
                    FileUtils.writeStringToFile(internalFile, encryptedFile, string_encoding)
                    ToastUtil.toast(applicationContext, "File encrypted successfully")
                    encrypt.text = "Decrypt"
                }
            } else {
                if (file.exists()) {
                    fileRead = FileUtils.readFileToString(file, string_encoding)
                    val decryptedFile = encryption!!.decryptOrNull(fileRead!!)
                    Log.e(TAG, "Decrypted 1: $decryptedFile")
                    FileUtils.writeStringToFile(file, decryptedFile, string_encoding)
                    ToastUtil.toast(applicationContext, "File decrypted successfully")
                    encrypt.text = "Encrypt"
                } else {
                    fileRead = FileUtils.readFileToString(internalFile, string_encoding)
                    val decryptedFile = encryption!!.decryptOrNull(fileRead!!)
                    Log.e(TAG, "Decrypted 2: $decryptedFile")
                    FileUtils.writeStringToFile(internalFile, decryptedFile, string_encoding)
                    ToastUtil.toast(applicationContext, "File decrypted successfully")
                    encrypt.text = "Encrypt"
                }
            }
        }
        reset.setOnClickListener {
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

    //method for deleting one file
    private fun deleteFiles() {
        var status = false
        try {
            val file = File(externalFilePath)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        status = try {
            val file = File(internalFilePath)
            if (file.exists()) {
                file.delete()
            }
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

    //method for deleting file under RESET functionality
    private fun deleteFiles(internal: String, external: String) {
        try {
            val file = File(external)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val file = File(internal)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //enable and disable buttons
    private fun changeButtonsState(state: Boolean) {
        create.isEnabled = state
        delete.isEnabled = state
        move.isEnabled = state
        encrypt.isEnabled = state
    }

    //validate numbers
    private fun validateNumber(): Boolean {
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

        checkEncryptionStatus()
        return true
    }

    //initialize collapsing tool bar
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


    //creating a file
    private fun createFile(resp: JSONObject) {
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
    }

    //when the back button is pressed
    override fun onBackPressed() {
        finish()
    }
}
