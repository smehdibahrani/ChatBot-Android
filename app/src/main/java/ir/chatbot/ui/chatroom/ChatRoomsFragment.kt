package ir.chatbot.ui.chatroom


import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import ir.chatbot.BR
import ir.chatbot.data.model.VMChatRoom
import ir.chatbot.databinding.FragmentChatRoomsBinding
import ir.chatbot.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_chat_rooms.*
import javax.inject.Inject
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ir.chatbot.R
import ir.chatbot.data.model.VMChatRoomMessageJoined
import ir.chatbot.data.model.VMMessage
import ir.chatbot.rt.MySocket
import ir.chatbot.rt.NetworkReceiver
import ir.chatbot.ui.chat.ChatFragment
import ir.chatbot.ui.chatroom.createChatRoomDialog.CreateChatRoomDialog
import ir.chatbot.ui.chatroom.deleteChatRoomDialog.DeleteChatRoomDialog
import ir.chatbot.ui.listeners.*
import ir.chatbot.ui.login.LoginFragment
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat_rooms.recyclerView
import kotlinx.android.synthetic.main.fragment_chat_rooms.tvName
import java.lang.Exception


class ChatRoomsFragment : BaseFragment<FragmentChatRoomsBinding, ChatRoomsViewModel>(),
    ChatRoomsNavigator {




    companion object {
        var listener: OnNewMessageListener? = null
        val TAG: String = ChatRoomsFragment::class.java.simpleName
        var connectionListener: OnConnectionChangedListener? = null
        var NetworkReceiverListener: NetworkReceiver.OnNetworkStateChangedListener? = null
        var conStatus: ConnectionStatus = ConnectionStatus.OFFLINE

        fun newInstance(): ChatRoomsFragment {
            val args = Bundle()
            val fragment = ChatRoomsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var adapter: ChatRoomsAdapter? = null

    lateinit var mFragmentChatRoomsBinding: FragmentChatRoomsBinding

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mChatRoomsViewModel: ChatRoomsViewModel

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_chat_rooms
    }

    override fun getViewModel(): ChatRoomsViewModel {
        return mChatRoomsViewModel
    }

    override fun onDetach() {
        super.onDetach()
        connectionListener = null
        NetworkReceiverListener = null
        listener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChatRoomsViewModel.navigator = this

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentChatRoomsBinding = viewDataBinding
        val intent = activity!!.intent

        if (intent != null) {
            if (intent.extras != null && intent.extras!!.containsKey("fromNotification")) {
                mChatRoomsViewModel.getChatRoom(intent.extras!!.getString("chatId")!!)
            }
        }
        setUpUi()


    }

    override fun handleError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun handleError(message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun logout() {
        openLoginFragment()
    }





    override fun updateList(list: List<VMChatRoomMessageJoined>) {
        adapter!!.clear()
        adapter!!.insert(list)
    }

    override fun deleteChatRoom() {
        adapter!!.remove()
    }

    private fun setUpUi() {
        tvName.text = mChatRoomsViewModel.dataManager.name
        tvEmail.text = mChatRoomsViewModel.dataManager.email


        NetworkReceiverListener = object : NetworkReceiver.OnNetworkStateChangedListener {
            override fun onChange(status: ConnectionStatus) {
                conStatus =  if (MySocket.instance.isSocketConnected)  ConnectionStatus.CONNECTED else if(isNetworkConnected) ConnectionStatus.CONNECTING else ConnectionStatus.OFFLINE
                updateConnectionStatusUi(conStatus)
            }
        }
        val bsr = NetworkReceiver(NetworkReceiverListener)

        if (adapter == null) {
            adapter = ChatRoomsAdapter(context!!)
            adapter!!.setOnItemSelectListener(listener = object :
                ChatRoomsAdapter.OnItemSelectListener {
                override fun onItemSelect(vmChatRoom: VMChatRoomMessageJoined) {
                    val item = VMChatRoom()
                    item._id = vmChatRoom._id
                    item.accountId = vmChatRoom.accountId
                    item.name = vmChatRoom.name
                    item.color = vmChatRoom.color
                    item.token = vmChatRoom.token
                    openChatFragment(item)
                }

            })

            adapter!!.setOnItemSelectListener(listener = object :
                ChatRoomsAdapter.OnDeleteItemListener {
                override fun onDeleteItem(vmChatRoom: VMChatRoomMessageJoined) {

                    DeleteChatRoomDialog.newInstance(object : OnChatRoomDeletedListener {
                        override fun onChatRoomDeleted() {
                            mChatRoomsViewModel.apiDeleteChatRoom(vmChatRoom._id)
                        }
                    }).show(activity!!.supportFragmentManager)


                }

            })

            activity!!.registerReceiver(bsr, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

            connectionListener = object : OnConnectionChangedListener {
                override fun onOnConnectionChanged(status: ConnectionStatus) {
                    conStatus = status
                    updateConnectionStatusUi(status)
                }
            }


        }
        mChatRoomsViewModel.getListChatRoom()

        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.visibility == View.VISIBLE) {
                    fab.hide()
                } else if (dy < 0 && fab.visibility != View.VISIBLE) {
                    fab.show()
                }
            }
        })

        fab.setOnClickListener {
            CreateChatRoomDialog.newInstance(object : OnChatRoomCreatedListener {
                override fun onChatRoomCreated() {
                    mChatRoomsViewModel.getListChatRoom()
                }
            }).show(activity!!.supportFragmentManager)

        }

        imgLogOut.setOnClickListener {

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(activity!!, gso);
            googleSignInClient.signOut().addOnCompleteListener {
                mChatRoomsViewModel.logout()
            }


        }


        if (mChatRoomsViewModel.dataManager.picUrl.isNotEmpty()) {
            Picasso.get()
                .load(mChatRoomsViewModel.dataManager.picUrl)
                .placeholder(R.drawable.profile_icon)
                .error(R.drawable.profile_icon)
                .into(imgProfile)
        }


        listener = object : OnNewMessageListener {
            override fun onNewMessage(message: VMMessage) {
                mChatRoomsViewModel.getListChatRoom()
            }
        }

        updateConnectionStatusUi(conStatus)


    }

    private fun updateConnectionStatusUi(status: ConnectionStatus) {
        try {
            when (status) {
                ConnectionStatus.OFFLINE -> imgConnectionStatus.setImageResource(R.drawable.ic_signal_gray_24dp)
                ConnectionStatus.CONNECTING -> imgConnectionStatus.setImageResource(R.drawable.ic_signal_blue_24dp)
                ConnectionStatus.CONNECTED -> imgConnectionStatus.setImageResource(R.drawable.ic_signal_green_24dp)
                ConnectionStatus.AUTHENTICATION_FAILURE -> imgConnectionStatus.setImageResource(R.drawable.ic_signal_red_24dp)
            }
        } catch (ex: Exception) {

        }
    }

    override fun openChatFragment(charRoom: VMChatRoom) {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentHolder,
                ChatFragment.newInstance(
                    charRoom._id,
                    charRoom.token,
                    charRoom.name,
                    charRoom.color
                ),
                ChatFragment.TAG
            )
            .addToBackStack(ChatFragment.TAG)
            .commit()
    }

    private fun openLoginFragment() {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentHolder, LoginFragment.newInstance(), ChatFragment.TAG)
            .disallowAddToBackStack()
            .commit()
    }


}
