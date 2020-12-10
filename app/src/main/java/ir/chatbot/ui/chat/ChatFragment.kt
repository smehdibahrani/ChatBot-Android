package ir.chatbot.ui.chat


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Gravity

import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.chatbot.BR
import ir.chatbot.R
import ir.chatbot.data.model.VMMessage
import ir.chatbot.databinding.FragmentChatBinding
import ir.chatbot.rt.MySocket

import ir.chatbot.ui.base.BaseFragment
import ir.chatbot.utils.Helper
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.recyclerView

import javax.inject.Inject
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.ui.chatroom.ChatRoomsAdapter
import ir.chatbot.ui.listeners.OnNewMessageListener
import java.util.*
import ir.chatbot.ui.base.BaseViewModel
import ir.chatbot.ui.chat.imageZoomableDialog.ImageZoomableDialog
import ir.chatbot.ui.listeners.OnChatRoomCreatedListener
import ir.chatbot.ui.listeners.OnChatRoomDeletedListener
import ir.chatbot.utils.Notify


class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(), ChatNavigator {


    companion object {
        val TAG: String = ChatFragment::class.java.simpleName
        var listener: OnNewMessageListener? = null
        lateinit var chatId: String
        fun newInstance(
            chatId: String,
            chatRoomToken: String,
            name: String,
            color: Int

        ): ChatFragment {
            val args = Bundle()
            val fragment = ChatFragment()
            args.putString("chatRoomToken", chatRoomToken)
            args.putString("name", name)
            args.putInt("color", color)
            args.putString("chatId", chatId)
            fragment.arguments = args
            return fragment
        }
    }
     private lateinit var  powerMenu:PowerMenu

    private lateinit var adapter: MessageAdapter
    private lateinit var tArray: TypedArray

    private lateinit var chatRoomToken: String
    private lateinit var name: String

    private var color: Int = 0
    lateinit var mFragmentChatBinding: FragmentChatBinding

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var mChatViewModel: ChatViewModel

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_chat
    }

    override fun getViewModel(): ChatViewModel {
        return mChatViewModel
    }


    override fun handleError(message: Int) {
        showToast(message)
    }

    override fun handleError(message: String) {
        showToast(message)
    }

    override fun deletedChatRoom() {
        activity!!.onBackPressed()
    }


    override fun deletedMessages() {
        showToast("all messages deleted")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChatViewModel.navigator = this
        Notify.cancelAll(context!!)
        chatRoomToken = arguments!!.getString("chatRoomToken")!!
        name = arguments!!.getString("name")!!
        color = arguments!!.getInt("color")
        chatId = arguments!!.getString("chatId")!!

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentChatBinding = viewDataBinding
        setUpUi()


    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun updateList(list: List<VMMessage>) {
        adapter.insert(list)
        recyclerView.scrollToPosition(adapter.itemCount - 1)
        mChatViewModel.seenMessagesByChatId(chatId)
    }

    private fun setUpUi() {
        val decor: View = activity!!.window.decorView
        decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity!!.window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )


        tArray = context!!.resources.obtainTypedArray(R.array.colors)
        imgChatRoomPhoto.setImageBitmap(
            Helper.color2Bitmap(
                context!!,
                tArray.getResourceId(color, 0))
        )
        tvFirstCharName.text = name.substring(0, 1).toUpperCase()
        tvName.text = name
        tvDesc.text = name


        adapter = MessageAdapter(context!!, name, color)

        adapter.setOnItemSelectListener(object : MessageAdapter.OnItemSelectedListener {
            override fun onItemPhotoSelected(url: String) {
                ImageZoomableDialog.newInstance(url).show(activity!!.supportFragmentManager)
            }

            override fun onItemMessageInSelected(message: String) {
                val clipboard =
                    activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(message, message)
                clipboard.primaryClip = clip
                showToast(R.string.textCopied)

            }

            override fun onItemMessageOutSelected(message: String) {
                etMessage.setText(message)
            }
        })
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter


        imgSend.setOnClickListener {
            if (!isNetworkConnected) {
                showToast(R.string.internet_connection_error)
                return@setOnClickListener
            }
            if (etMessage.text.trim().isNotEmpty()) {
                val msg = VMMessage()
                msg._id = UUID.randomUUID().toString()
                msg.data = etMessage.text.toString().trim()
                msg.chatId = chatId
                msg.owner = "a"
                msg.seen = 1
                msg.type = "text"
                msg.createAt = Date().time
                msg.createAtFormated = Helper.dateTimeNow()

                MySocket.instance.sendMessage(msg.type, msg.data, chatRoomToken)

                etMessage.setText("")
                mChatViewModel.insertMessage(msg)
                adapter.insert(msg)
                recyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }

        imgDetail.setOnClickListener {
            powerMenu = PowerMenu.Builder(context!!)

                .addItem(PowerMenuItem("Clear History"))
                //  .addItem(PowerMenuItem("Delete Chat Room"))
                .setAnimation(MenuAnimation.FADE)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(ContextCompat.getColor(context!!, R.color.gray))
                .setTextGravity(Gravity.START)
                .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                .setOnMenuItemClickListener{position, item ->
                    if (position == 0) {
                        adapter.clear()
                        mChatViewModel.apiDeleteMessages(chatId)
                    } else {
                        adapter.clear()
                        //  mChatViewModel.apiDeleteChatRoom(chatId)
                    }
                    powerMenu.dismiss()
                }
                .build()

            powerMenu.showAsDropDown(it)
        }
        imgBack.setOnClickListener {
            activity!!.onBackPressed()
        }
        listener = object : OnNewMessageListener {
            override fun onNewMessage(message: VMMessage) {

                    adapter.insert(message)
                    mChatViewModel.seenMessagesByChatId(message.chatId)
                    recyclerView.scrollToPosition(adapter.itemCount - 1)

            }
        }

        // activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        mChatViewModel.getListMessage(chatId)




    }



}

