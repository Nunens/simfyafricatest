package menolla.co.za.itsi_test.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import menolla.co.za.itsi_test.utils.ToastUtil
import nunens.co.za.simfyafricatest.R
import nunens.co.za.simfyafricatest.database.model.SecondTaskListModel
import nunens.co.za.simfyafricatest.listener.AdapterClickListener


class SecondTaskAdapter(var itemList: ArrayList<SecondTaskListModel>) : RecyclerView.Adapter<SecondTaskAdapter.ViewHolder>() {
    companion object {
        lateinit var drawableT: TextDrawable
        lateinit var colorGenerator: ColorGenerator
        lateinit var ctx: Context
        lateinit var adapterListener: AdapterClickListener
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondTaskAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.second_task_item, parent, false)
        colorGenerator = ColorGenerator.MATERIAL
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: SecondTaskAdapter.ViewHolder, position: Int) {
        holder.bindItems(itemList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return itemList.size
    }

    //used to initialize the group list and notify data set
    fun setItems(context: Context, items: List<SecondTaskListModel>) {
        ctx = context
        itemList = items as ArrayList<SecondTaskListModel>
        notifyDataSetChanged()
    }

    //used to initialize the group list and notify data set
    fun setItems(context: Context, items: List<SecondTaskListModel>, listener: AdapterClickListener) {
        adapterListener = listener
        ctx = context
        itemList = items as ArrayList<SecondTaskListModel>
        notifyDataSetChanged()
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var container: RelativeLayout = itemView.findViewById(R.id.container)
        var title: TextView = itemView.findViewById(R.id.title)
        var description: TextView = itemView.findViewById(R.id.description)
        var image: ImageView = itemView.findViewById(R.id.image)

        fun bindItems(g: SecondTaskListModel) {
            drawableT = TextDrawable.builder().buildRect("", colorGenerator.getColor(colorGenerator.randomColor))
            container.background = drawableT
            container.setOnClickListener {
                adapterListener.onClick(g)
            }
            title.text = g.title
            description.text = g.description
            try {
                Glide.with(ctx)
                        .load(g.image)
                        .into(image)
                        .apply {
                            RequestOptions()
                                    .error(R.drawable.placeholder)
                                    .centerCrop()
                        }
            } catch (e: Exception) {
                ToastUtil.errorToast(ctx!!, "Unable to load image")
            }
        }
    }

}