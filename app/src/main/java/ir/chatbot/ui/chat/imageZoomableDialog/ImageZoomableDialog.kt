package ir.chatbot.ui.chat.imageZoomableDialog


import android.app.Dialog
import android.os.Bundle


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.squareup.picasso.Picasso

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection
import ir.chatbot.R
import ir.chatbot.databinding.DialogCreateChatRoomBinding
import ir.chatbot.databinding.DialogDeleteChatRoomBinding
import ir.chatbot.databinding.DialogImageZoomableBinding
import ir.chatbot.ui.base.BaseDialog
import ir.chatbot.ui.chatroom.createChatRoomDialog.DeleteChatRoomCallback
import ir.chatbot.ui.listeners.OnChatRoomCreatedListener
import ir.chatbot.ui.listeners.OnChatRoomDeletedListener
import kotlinx.android.synthetic.main.dialog_create_chat_room.*
import kotlinx.android.synthetic.main.dialog_image_zoomable.*
import kotlinx.android.synthetic.main.dialog_image_zoomable.view.*


class ImageZoomableDialog : BaseDialog(), ImageZoomableCallback {


    lateinit var url:String

    @Inject
    lateinit var mImageZoomableViewModel: ImageZoomableViewModel

    override fun dismissDialog() {
        dismissDialog(TAG)

    }

    override fun submit() {
        dismissDialog(TAG)


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
        val binding: DialogImageZoomableBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_image_zoomable, container, false)

        val view = binding.root

        AndroidSupportInjection.inject(this)

        binding.viewModel = mImageZoomableViewModel
        mImageZoomableViewModel.navigator = this


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        url = arguments!!.getString("url")!!

        if (url.isNotEmpty()) {
                Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.photo_hint)
                    .error(R.drawable.photo_hint)
                    .into(view.imgZoomable)
        }
    }

    fun show(fragmentManager: FragmentManager) {

        super.show(fragmentManager, TAG)



    }

    companion object {

        private val TAG = ImageZoomableDialog::class.java.simpleName

        fun newInstance(url:String): ImageZoomableDialog {
            val fragment = ImageZoomableDialog()
            val bundle = Bundle()
            bundle.putString("url",url)
            fragment.arguments = bundle
            return fragment
        }
    }



}
