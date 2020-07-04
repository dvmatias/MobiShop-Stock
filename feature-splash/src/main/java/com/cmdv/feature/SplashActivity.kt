package com.cmdv.feature

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.cmdv.core.navigator.Navigator
import com.cmdv.core.utils.logErrorMessage
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashActivityViewModel by viewModel()

    private val navigator: Navigator by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setLoginUi()
        setButtonLoginRegisterListener()
    }

    private fun setLoginUi() {
        buttonLoginRegister.text = "Login"
        buttonLoginRegister.tag = "login"
        textViewForgot.visibility = View.VISIBLE
        textViewForgot.setOnClickListener {  }
        textViewFooter.text = "Don't have an account.\nRegister here."
        textViewFooter.setOnClickListener { setRegisterUi() }
    }

    private fun setRegisterUi() {
        buttonLoginRegister.text = "Sign Up"
        buttonLoginRegister.tag = "register"
        textViewForgot.visibility = View.INVISIBLE
        textViewForgot.setOnClickListener(null)
        textViewFooter.text = "Already have an account.\nLogin here."
        textViewFooter.setOnClickListener { setLoginUi() }
    }

    private fun setButtonLoginRegisterListener() {
        buttonLoginRegister.setOnClickListener {
            when (it.tag) {
                "login" -> {
                    viewModel.login(editTextEmail.text.toString(), editTextPassword.text.toString())
                    observeLoginFlow()
                }
                "register" -> {
                    viewModel.register(editTextEmail.text.toString(), editTextPassword.text.toString())
                    observeRegisterFlow()
                }
            }
        }
    }

    private fun observeLoginFlow() {
        viewModel.userLoginLiveData.observe(this, Observer {
            if (it.data != null)
                goToMainScreen()
            else
                Snackbar.make(window.decorView.rootView, "${it?.message}", Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun observeRegisterFlow() {
        viewModel.userRegisterMutableLiveData.observe(this, Observer {
            if (it.data != null)
                goToMainScreen()
            else
                Snackbar.make(window.decorView.rootView, "${it?.message}", Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun goToMainScreen() {
        navigator.toMainScreen(this, null, null, true)
    }

}