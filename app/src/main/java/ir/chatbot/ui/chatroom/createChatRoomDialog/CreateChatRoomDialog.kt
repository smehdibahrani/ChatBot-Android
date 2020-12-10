package ir.chatbot.ui.chatroom.createChatRoomDialog


import android.os.Bundle


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection
import ir.chatbot.R
import ir.chatbot.databinding.DialogCreateChatRoomBinding
import ir.chatbot.ui.base.BaseDialog
import ir.chatbot.ui.listeners.OnChatRoomCreatedListener
import kotlinx.android.synthetic.main.dialog_create_chat_room.*


class CreateChatRoomDialog : BaseDialog(), CreateChatRoomCallback {


    @Inject
    lateinit var mCreateChatRoomViewModel: CreateChatRoomViewModel

    override fun dismissDialog() {
        dismissDialog(TAG)
        listener.onChatRoomCreated()
    }

    override fun submit() {
        val name = etChatRoomName.text.toString().trim()
        if (name.isNotEmpty()) {
            mCreateChatRoomViewModel.apiListChatRoom(name)
        }
    }


    override fun handleError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun handleError(message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DialogCreateChatRoomBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_create_chat_room, container, false)

        val view = binding.root

        AndroidSupportInjection.inject(this)

        binding.viewModel = mCreateChatRoomViewModel

        mCreateChatRoomViewModel.navigator = this

        return view
    }

    fun show(fragmentManager: FragmentManager) {

        super.show(fragmentManager, TAG)

    }

    companion object {

        private val TAG = CreateChatRoomDialog::class.java.simpleName
        lateinit var listener: OnChatRoomCreatedListener
        fun newInstance(_listener: OnChatRoomCreatedListener): CreateChatRoomDialog {
            listener = _listener
            val fragment = CreateChatRoomDialog()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }



}
