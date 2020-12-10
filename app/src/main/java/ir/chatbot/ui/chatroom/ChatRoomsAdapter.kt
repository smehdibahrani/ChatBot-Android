package ir.chatbot.ui.chatroom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.fitness.data.Goal
import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.utils.Helper
import java.util.ArrayList
import ir.chatbot.R
import ir.chatbot.data.model.VMChatRoomMessageJoined


class ChatRoomsAdapter(private val context: Context) :
    RecyclerView.Adapter<ChatRoomsAdapter.MyViewHolder>() {
    var tArray: TypedArray = context.resources.obtainTypedArray(R.array.colors)

    private val listAddress = ArrayList<VMChatRoomMessageJoined>()
    private lateinit var listener: OnItemSelectListener
    private lateinit var deleteListener: OnDeleteItemListener
    private var lastSelectedPosition = -1

    companion object {
        var unread = 0
    }

    fun insert(item: VMChatRoomMessageJoined) {
        listAddress.add(item)
        notifyDataSetChanged()
    }

    fun insert(items: List<VMChatRoomMessageJoined>) {
        listAddress.addAll(items)
        notifyDataSetChanged()
    }

    fun remove() {
        //
        notifyItemRemoved(lastSelectedPosition)
        listAddress.removeAt(lastSelectedPosition)
    }

    fun insertFirst(item: VMChatRoomMessageJoined) {
        listAddress.add(0, item)
        notifyDataSetChanged()
    }

    fun setOnItemSelectListener(listener: OnItemSelectListener) {
        this.listener = listener
    }

    fun setOnItemSelectListener(listener: OnDeleteItemListener) {
        this.deleteListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val convertView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_room, parent, false)
        return MyViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        initData(holder, position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItemCount(): Int {
        return listAddress.size
    }


    @SuppressLint("DefaultLocale")
    private fun initData(viewHolder: MyViewHolder, position: Int) {
        val item = listAddress[viewHolder.adapterPosition]

        viewHolder.tvName.text = item.name
        if (item.type == "photo")
            viewHolder.tvLastMessage.text = context.resources.getString(R.string.photo)
        else
            viewHolder.tvLastMessage.text = item.data
        if (item.unread == 0)
            viewHolder.tvBadge.visibility = View.GONE
        else {
            viewHolder.tvBadge.visibility = View.VISIBLE
            viewHolder.tvBadge.text = item.unread.toString()
        }

        if (item.createAt != null && item.createAt!! > 0 )
            viewHolder.tvLastMessageDateTime.text = Helper.longToDateFormat(item.createAt!!)
        else
            viewHolder.tvLastMessageDateTime.text = ""

        viewHolder.imgChatRoomPhoto.setImageBitmap(
            Helper.color2Bitmap(
                context,
                tArray.getResourceId(item.color, 2)
            )
        )

        viewHolder.tvFirstCharName.text = item.name.substring(0, 1).toUpperCase()

        viewHolder.imgChatRoomPhoto.drawable
        viewHolder.root.setOnClickListener {
            lastSelectedPosition = position
            notifyDataSetChanged()
            listener.onItemSelect(item)
        }

        viewHolder.root.setOnLongClickListener {
//            lastSelectedPosition = position
//            notifyDataSetChanged()
//            deleteListener.onDeleteItem(item)
            return@setOnLongClickListener true
        }


    }

    fun clear() {
        listAddress.clear()
    }

    inner class MyViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var imgChatRoomPhoto: ImageView = convertView.findViewById(R.id.imgChatRoomPhoto)
        var tvName: TextView = convertView.findViewById(R.id.tvName)
        var tvLastMessage: TextView = convertView.findViewById(R.id.tvLastMessage)
        var tvLastMessageDateTime: TextView = convertView.findViewById(R.id.tvLastMessageDateTime)
        var tvFirstCharName: TextView = convertView.findViewById(R.id.tvFirstCharName)
        var tvBadge: TextView = convertView.findViewById(R.id.tvBadge)
        var root: View = convertView.findViewById(R.id.root)


    }


    interface OnItemSelectListener {
        fun onItemSelect(vmChatRoom: VMChatRoomMessageJoined)
    }

    interface OnDeleteItemListener {
        fun onDeleteItem(vmChatRoom: VMChatRoomMessageJoined)
    }


}
