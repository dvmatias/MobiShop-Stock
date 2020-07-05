package com.cmdv.feature

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.cmdv.core.helpers.HtmlHelper
import com.cmdv.core.navigator.Navigator
import com.cmdv.domain.models.LiveDataStatusWrapper
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
        checkIfUserIsLoggedIn()
    }

    private fun setLoginUi() {
        buttonLoginRegister.text = getString(R.string.label_button_login)
        buttonLoginRegister.tag = "login"
        textViewForgot.visibility = View.VISIBLE
        textViewForgot.setOnClickListener {  }
        textViewFooter.text = HtmlHelper.fromHtml(R.string.don_t_have_an_account_register_here, this)
        textViewFooter.setOnClickListener { setRegisterUi() }
    }

    private fun setRegisterUi() {
        buttonLoginRegister.text = getString(R.string.label_button_register)
        buttonLoginRegister.tag = "register"
        textViewForgot.visibility = View.INVISIBLE
        textViewForgot.setOnClickListener(null)
        textViewFooter.text = HtmlHelper.fromHtml(R.string.already_have_an_account_login_here, this)
        textViewFooter.setOnClickListener { setLoginUi() }
    }

    private fun checkIfUserIsLoggedIn() {
        showLoading(true)
        viewModel.checkIfUserIsLoggedIn()
        viewModel.user.observe(this, Observer {
            showLoading(false)
            if (it?.data != null)
                goToMainScreen()
        })
    }

    private fun setButtonLoginRegisterListener() {
        buttonLoginRegister.setOnClickListener {
            when (it.tag) {
                "login" -> {
                    showLoading(true)
                    viewModel.login(editTextEmail.text.toString(), editTextPassword.text.toString())
                    observeLoginFlow()
                }
                "register" -> {
                    showLoading(true)
                    val email: String = editTextEmail.text.toString()
                    viewModel.checkIfEmailIsWhitelisted(email)
                    viewModel.isWhiteListed.observe(this, Observer { isWhiteListed ->
                        if (isWhiteListed) {
                            viewModel.register(editTextEmail.text.toString(), editTextPassword.text.toString())
                            observeRegisterFlow()
                        } else {
                            Snackbar.make(window.decorView.rootView, "Sorry your email is not white listed. Please contact the support team for further information and assistance.", Snackbar.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    private fun observeLoginFlow() {
        viewModel.userLoginLiveData.observe(this, Observer {
            when (it?.status) {
                LiveDataStatusWrapper.Status.ERROR -> {
                    showLoading(false)
                    Snackbar.make(window.decorView.rootView, "${it.message}", Snackbar.LENGTH_SHORT).show()
                }
                LiveDataStatusWrapper.Status.SUCCESS -> {
                    showLoading(false)
                    goToMainScreen()
                }
                else -> {}
            }
        })
    }

    private fun observeRegisterFlow() {
        viewModel.userRegisterMutableLiveData.observe(this, Observer {
            when (it?.status) {
                LiveDataStatusWrapper.Status.ERROR -> {
                    viewModel.userRegisterMutableLiveData.removeObservers(this)
                    showLoading(false)
                    Snackbar.make(window.decorView.rootView, "${it.message}", Snackbar.LENGTH_SHORT).show()
                }
                LiveDataStatusWrapper.Status.SUCCESS -> {
                    viewModel.userRegisterMutableLiveData.removeObservers(this)
                    showLoading(false)
                    if (it.data != null) {
                        goToMainScreen()
                    } else {
                        Snackbar.make(window.decorView.rootView, "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
                else -> {}
            }
        })
    }

    private fun showLoading(show: Boolean) {
        frameLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun goToMainScreen() {
        navigator.toMainScreen(this, null, null, true)
    }

}