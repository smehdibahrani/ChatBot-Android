package ir.chatbot.ui.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

import ir.chatbot.R
import ir.chatbot.data.model.VMMessage
import ir.chatbot.ui.chat.imageZoomableDialog.ImageZoomableDialog
import ir.chatbot.ui.chatroom.ChatRoomsAdapter
import ir.chatbot.ui.chatroom.deleteChatRoomDialog.DeleteChatRoomDialog
import ir.chatbot.ui.listeners.OnChatRoomDeletedListener
import ir.chatbot.utils.Helper
import kotlinx.android.synthetic.main.fragment_chat_rooms.*
import java.lang.reflect.Array
import java.util.*
import kotlin.collections.ArrayList


class MessageAdapter(
    private val context: Context,
    private val name: String,
    private val color: Int

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val currentItem = 0
    private val VIEW_TYPE_ITEM_MESSAGE_IN = 0
    private val VIEW_TYPE_ITEM_PHOTO_IN = 1
    private val VIEW_TYPE_ITEM_MESSAGE_OUT = 2
    lateinit var listener: OnItemSelectedListener

    var tArray: TypedArray = context.resources.obtainTypedArray(R.array.colors)
    private var listMessage = ArrayList<VMMessage>()


    inner class ViewHolderItemMessageIn(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var imgChatRoomPhoto: ImageView = convertView.findViewById(R.id.imgChatRoomPhoto)
        var tvFirstCharName: TextView = convertView.findViewById(R.id.tvFirstCharName)
        var tvMessage: TextView = convertView.findViewById(R.id.tvMessage)
        var tvDateTime: TextView = convertView.findViewById(R.id.tvDateTime)


    }


    inner class ViewHolderItemPhotoIn(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var imgChatRoomPhoto: ImageView = convertView.findViewById(R.id.imgChatRoomPhoto)
        var tvFirstCharName: TextView = convertView.findViewById(R.id.tvFirstCharName)
        var imgPhoto: ImageView = convertView.findViewById(R.id.imgPhoto)
        var tvDateTime: TextView = convertView.findViewById(R.id.tvDateTime)

    }


    inner class ViewHolderItemMessageOut(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var tvMessage: TextView = convertView.findViewById(R.id.tvMessage)
        var tvDateTime: TextView = convertView.findViewById(R.id.tvDateTime)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM_MESSAGE_IN -> {
                val convertView =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message_in, parent, false)
                ViewHolderItemMessageIn(convertView)
            }
            VIEW_TYPE_ITEM_PHOTO_IN -> {
                val convertView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_photo_in, parent, false)
                ViewHolderItemPhotoIn(convertView)
            }
            else -> {
                val convertView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_out, parent, false)
                ViewHolderItemMessageOut(convertView)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        initInstances(holder, position)
    }

    override fun getItemId(position: Int): Long {

        return 0

    }

    override fun getItemCount(): Int {
        return listMessage.size
    }


    override fun getItemViewType(position: Int): Int {
        val item = listMessage!![position]
        if (item.owner == "t") {
            if (item.type == "photo")
                return VIEW_TYPE_ITEM_PHOTO_IN
            return VIEW_TYPE_ITEM_MESSAGE_IN
        } else
            return VIEW_TYPE_ITEM_MESSAGE_OUT

    }


    /// custom methods

    fun setOnItemSelectListener(listener: OnItemSelectedListener) {
        this.listener = listener
    }

    fun clear() {
        this.listMessage.clear()
        notifyDataSetChanged()
    }


    fun insert(item: VMMessage) {
        listMessage.add(item)
        notifyItemInserted(listMessage.size)

    }

    fun insert(items: List<VMMessage>) {

        listMessage.addAll(items)
        notifyDataSetChanged()


    }

    fun refreshModifedItem() {
        notifyItemChanged(currentItem)
    }

    private fun initInstances(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderItemMessageIn) {

            val item = listMessage[position]

            holder.imgChatRoomPhoto.setImageBitmap(
                Helper.color2Bitmap(context, tArray.getResourceId(color, 0))
            )
            holder.tvFirstCharName.text = name.substring(0, 1).toUpperCase()

            holder.tvMessage.text = item.data

            holder.tvDateTime.text =
                Helper.longToDateFormat(item.createAt)// item.createAtFormated!!
            holder.tvMessage.setOnLongClickListener {
                listener.onItemMessageInSelected(item.data)
                return@setOnLongClickListener  true
            }


        }

        if (holder is ViewHolderItemPhotoIn) {

            val item = listMessage[position]

            holder.imgChatRoomPhoto.setImageBitmap(
                Helper.color2Bitmap(context, tArray.getResourceId(color, 0))
            )
            holder.tvFirstCharName.text = name.substring(0, 1).toUpperCase()
            holder.tvDateTime.text =
                Helper.longToDateFormat(item.createAt)// item.createAtFormated!!

            if (item.data.isNotEmpty()) {
                Picasso.get()
                    .load(item.data)
                    .placeholder(R.drawable.photo_hint)
                    .error(R.drawable.photo_hint)
                    .into(holder.imgPhoto)
            }

            holder.imgPhoto.setOnClickListener {
                listener.onItemPhotoSelected(item.data)
            }


        } else if (holder is ViewHolderItemMessageOut) {

            val item = listMessage[position]
            holder.tvMessage.text = item.data
            holder.tvDateTime.text = item.createAtFormated
            holder.tvMessage.setOnLongClickListener {
                listener.onItemMessageOutSelected(item.data)
                return@setOnLongClickListener  true
            }

        }


    }


    interface OnItemSelectedListener {
        fun onItemPhotoSelected(url: String)
        fun onItemMessageInSelected(message: String)
        fun onItemMessageOutSelected(message: String)
    }





}


