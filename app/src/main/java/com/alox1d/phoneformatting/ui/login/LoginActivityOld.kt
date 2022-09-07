//package com.alox1d.phoneformatting.ui.login
//
//import android.app.Activity
//import android.os.Bundle
//import android.os.Handler
//import android.telephony.PhoneNumberFormattingTextWatcher
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.View
//import android.view.inputmethod.EditorInfo
//import android.widget.EditText
//import android.widget.Toast
//import androidx.annotation.StringRes
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import com.alox1d.phoneformatting.R
//import com.alox1d.phoneformatting.databinding.ActivityLoginBinding
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import java.io.*
//import java.net.InetAddress
//import java.net.InetSocketAddress
//import java.net.ServerSocket
//import java.net.Socket
//import java.util.*
//
//
//class LoginActivity : AppCompatActivity() {
//
//    private lateinit var loginViewModel: LoginViewModel
//    private lateinit var binding: ActivityLoginBinding
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val username = binding.username
//        val password = binding.password
//        val login = binding.login
//        val loading = binding.loading
//        val theText = binding.theText
//        theText?.text = "First Second"
//
//
//        login.setOnClickListener {
//            // 1. Socket sender
////            val messageSender = MessageSender()
////            messageSender.execute("Text")
//
//            // 2. Socket server
//            GlobalScope.launch { MyServerThread().run() }
//
//            // Default
////                loading.visibility = View.VISIBLE
////                loginViewModel.login(username.text.toString(), password.text.toString())
//        }
//
//        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
//            .get(LoginViewModel::class.java)
//
//        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
//            val loginState = it ?: return@Observer
//
//            // disable login button unless both username / password is valid
//            login.isEnabled = loginState.isDataValid
//
//            if (loginState.usernameError != null) {
//                username.error = getString(loginState.usernameError)
//            }
//            if (loginState.passwordError != null) {
//                password.error = getString(loginState.passwordError)
//            }
//        })
//
//        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
//            val loginResult = it ?: return@Observer
//
//            loading.visibility = View.GONE
//            if (loginResult.error != null) {
//                showLoginFailed(loginResult.error)
//            }
//            if (loginResult.success != null) {
//                updateUiWithUser(loginResult.success)
//            }
//            setResult(Activity.RESULT_OK)
//
//            //Complete and destroy login activity once successful
//            finish()
//        })
//
//        username.afterTextChanged {
//            loginViewModel.loginDataChanged(
//                username.text.toString(),
//                password.text.toString()
//            )
//        }
//
//        password.apply {
//            var mFormatting = false; // this is a flag which prevents the  stack overflow.
//            var mAfter = 0;
////            afterTextChanged {
//////
//////                loginViewModel.loginDataChanged(
//////                    username.text.toString(),
//////                    password.text.toString()
//////                )
//////                // Make sure to ignore calls to afterTextChanged caused by the work done below
//////                // Make sure to ignore calls to afterTextChanged caused by the work done below
//////                if (!mFormatting) {
//////                    mFormatting = true
//////                    // using US or RU formatting...
//////                    if (mAfter != 0) // in case back space ain't clicked...
//////                    {
//////                        val num: String = it
//////                        val data = PhoneNumberUtils.formatNumber(num, "RU")
//////                        if (data != null) {
////////                            it.clear()
////////                            it.append(data)
//////                            Log.i("Number", data) //8 (999) 123-45-67 or +7 999 123-45-67
//////                        }
//////                    }
//////                    mFormatting = false
//////                }
////            }
////            doBeforeTextChanged { text, start, count, after ->
////                mAfter = after; // flag to detect backspace.. }
////            }
//            setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE ->
//                        loginViewModel.login(
//                            username.text.toString(),
//                            password.text.toString()
//                        )
//                }
//                false
//            }
//
//
//            addTextChangedListener(PhoneNumberFormattingTextWatcher("RU"))
//        }
//    }
//
//    inner class MyServerThread: Runnable {
//        lateinit var s: Socket
//        lateinit var ss: ServerSocket
//        lateinit var isr: InputStreamReader
//        lateinit var bufferedReader: BufferedReader
//        lateinit var pw: PrintWriter
//        var h = Handler(this@LoginActivity.mainLooper)
//
//        lateinit var message: String
//        override fun run() {
//            try {
//                val port = 8484
//                ss = ServerSocket(port)
//                while (true){
//                    s = ss.accept()
//                    isr = InputStreamReader(s.getInputStream())
//                    bufferedReader = BufferedReader(isr)
//                    message = bufferedReader.readLine()
//
//                    val sockaddr: InetSocketAddress =
//                        s.remoteSocketAddress as InetSocketAddress
//                    val addr: InetAddress = sockaddr.getAddress()
//                    h.post {
//                        Toast.makeText(this@LoginActivity, "Тебе стучат в ${port}", Toast.LENGTH_SHORT).show()
//                    }
//                    pw = PrintWriter(s.getOutputStream())
//                    val messageSender = MessageSender()
//                    val httpResponse = ("HTTP/1.1 200 OK\r\n"
//                            + "Access-Control-Allow-Origin: https://nekr-localhost-test.herokuapp.com\r\n"
//                            + "Access-Control-Allow-Methods: GET\r\n"
//                            + "<h1>Hi</h1>"
//                            )
//                    val response = "<h1>Hi</h1> + ${Date()}"
//                    pw.println("HTTP/1.1 200 OK");
//                    pw.println(httpResponse)
//                    pw.println("Content-Type: text/html");
//                    pw.println("Content-Length: " + response.length);
//                    pw.println();
//                    pw.println(response);
//                    pw.flush();
//                    pw.close();
//                    s.close();
//
////                    messageSender.execute(
////                        httpResponse, sockaddr.port
////                    )
////                    s.getOutputStream().write(httpResponse.toByteArray());
//
//                }
//            } catch (e: IOException){
//                e.printStackTrace()
//            }
//        }
//    }
//
//    private fun updateUiWithUser(model: LoggedInUserView) {
//        val welcome = getString(R.string.welcome)
//        val displayName = model.displayName
//        // TODO : initiate successful logged in experience
//        Toast.makeText(
//            applicationContext,
//            "$welcome $displayName",
//            Toast.LENGTH_LONG
//        ).show()
//    }
//
//    private fun showLoginFailed(@StringRes errorString: Int) {
//        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
//    }
//}
//
///**
// * Extension function to simplify setting an afterTextChanged action to EditText components.
// */
//fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
//    this.addTextChangedListener(object : TextWatcher {
//        override fun afterTextChanged(editable: Editable?) {
//            afterTextChanged.invoke(editable.toString())
//        }
//
//        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//    })
//}