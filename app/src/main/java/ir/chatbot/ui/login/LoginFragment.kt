package ir.chatbot.ui.login



import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import ir.chatbot.databinding.FragmentLoginBinding
import ir.chatbot.BR
import ir.chatbot.R
import ir.chatbot.ui.base.BaseFragment
import ir.chatbot.ui.chatroom.ChatRoomsFragment
import javax.inject.Inject
import android.app.Activity.RESULT_OK
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(), LoginNavigator,
    GoogleApiClient.OnConnectionFailedListener {


    companion object {
        const val RC_SIGN_IN = 101
        val TAG: String = LoginFragment::class.java.simpleName

        fun newInstance(): LoginFragment {
            val args = Bundle()
            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var mFragmentLoginBinding: FragmentLoginBinding
    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var mLoginViewModel: LoginViewModel

    private var googleSignInClient: GoogleSignInClient? = null


    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun getViewModel(): LoginViewModel {
        return mLoginViewModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoginViewModel.navigator = this

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentLoginBinding = viewDataBinding
        mFragmentLoginBinding.btnSignIN.setOnClickListener {
            signIn()
        }

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("connection", "failed")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i("mehdi",resultCode.toString())
        Log.i("mehdi",requestCode.toString())
       // Log.i("mehdi",data!!.dataString!!)
      //  if (resultCode == RESULT_OK)
            if (requestCode == RC_SIGN_IN) {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                    onLoggedIn(account)
                } catch (e: ApiException) {
                    Log.w(TAG, "signInResult:failed code=" + e.statusCode);
                }
            }

    }




    override fun handleError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun handleError(message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun openChatRoomsFragment() {
        activity!!.supportFragmentManager
            .beginTransaction()
            .disallowAddToBackStack()
            .replace(R.id.fragmentHolder, ChatRoomsFragment.newInstance(), ChatRoomsFragment.TAG)
            .commit()
    }


    private fun onLoggedIn(account: GoogleSignInAccount) {

        val displayName = account.displayName!!
        val email = account.email!!
        val photoUrl = account.photoUrl.toString()
        mLoginViewModel.apiLogin(email, displayName, photoUrl)


    }


    private fun signIn() {

        val signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)


    }


}
