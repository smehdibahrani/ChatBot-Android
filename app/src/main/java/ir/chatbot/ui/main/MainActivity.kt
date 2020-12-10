package ir.chatbot.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build

import android.os.Bundle
import android.util.Log

import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ir.chatbot.BR
import ir.chatbot.R
import ir.chatbot.databinding.ActivityMainBinding
import ir.chatbot.ui.base.BaseActivity
import ir.chatbot.ui.chatroom.ChatRoomsFragment
import ir.chatbot.ui.login.LoginFragment


import javax.inject.Inject
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import ir.chatbot.App
import ir.chatbot.rt.MyService
import ir.chatbot.rt.MySocket
import ir.chatbot.utils.Notify


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator,
    HasSupportFragmentInjector {



    var loggedIn = false
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private var mActivityMainBinding: ActivityMainBinding? = null


    private var mMainViewModel: MainViewModel? = null

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainViewModel {
        mMainViewModel =
            ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel::class.java)
        return mMainViewModel!!
    }

    override fun handleError(throwable: Throwable) {
        // handle error
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBackPressed() {
        val decor:View  = window.decorView
        decor.systemUiVisibility = 0
        intent = null
        super.onBackPressed()


    }


    override fun onFragmentDetached(tag: String) {
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            fragmentManager
                .beginTransaction()
                .disallowAddToBackStack()
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right)
                .remove(fragment)
                .commitNow()

        }
    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentDispatchingAndroidInjector
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Notify.cancelAll(this)
        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mActivityMainBinding = viewDataBinding
        mMainViewModel!!.navigator = this

        mMainViewModel!!.startSeeding()


    }


    override fun openLoginFragment() {

        supportFragmentManager
            .beginTransaction()
            .disallowAddToBackStack()
            .add(R.id.fragmentHolder, LoginFragment.newInstance(), LoginFragment.TAG)
            .commit()
    }

    override fun openChatRoomsFragment(token: String?) {

        supportFragmentManager
            .beginTransaction()
            .disallowAddToBackStack()
            .add(R.id.fragmentHolder, ChatRoomsFragment.newInstance(), ChatRoomsFragment.TAG)
            .commit()
    }


}
