package ir.chatbot.ui.chatroom.deleteChatRoomDialog


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
import ir.chatbot.databinding.DialogDeleteChatRoomBinding
import ir.chatbot.ui.base.BaseDialog
import ir.chatbot.ui.chatroom.createChatRoomDialog.DeleteChatRoomCallback
import ir.chatbot.ui.listeners.OnChatRoomCreatedListener
import ir.chatbot.ui.listeners.OnChatRoomDeletedListener
import kotlinx.android.synthetic.main.dialog_create_chat_room.*


class DeleteChatRoomDialog : BaseDialog(), DeleteChatRoomCallback {


    @Inject
    lateinit var mDeleteChatRoomViewModel: DeleteChatRoomViewModel

    override fun dismissDialog() {
        dismissDialog(TAG)

    }

    override fun submit() {
        dismissDialog(TAG)
        listener.onChatRoomDeleted()

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
        val binding: DialogDeleteChatRoomBinding=
            DataBindingUtil.inflate(inflater, R.layout.dialog_delete_chat_room, container, false)

        val view = binding.root

        AndroidSupportInjection.inject(this)

        binding.viewModel = mDeleteChatRoomViewModel

        mDeleteChatRoomViewModel.navigator = this

        return view
    }

    fun show(fragmentManager: FragmentManager) {

        super.show(fragmentManager, TAG)

    }

    companion object {

        private val TAG = DeleteChatRoomDialog::class.java.simpleName
        lateinit var listener: OnChatRoomDeletedListener
        fun newInstance(_listener: OnChatRoomDeletedListener): DeleteChatRoomDialog {
            listener = _listener
            val fragment = DeleteChatRoomDialog()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }



}
