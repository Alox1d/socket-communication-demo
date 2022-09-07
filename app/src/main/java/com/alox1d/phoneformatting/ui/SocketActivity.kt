package com.alox1d.phoneformatting.ui

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alox1d.phoneformatting.databinding.ActivitySocketBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*


class SocketActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySocketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySocketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.port
        val start = binding.start
        val loading = binding.loading
        val theText = binding.theText
        theText?.text = "First Second"


        start.setOnClickListener {
            // 1. Socket sender
//            val messageSender = MessageSender()
//            messageSender.execute("Text")

            // 2. Socket server
            GlobalScope.launch(Dispatchers.IO) { SocketThread(password.text.toString()).run() }

        }
    }

    inner class SocketThread(private val userPort: String) : Runnable {
        lateinit var s: Socket
        lateinit var ss: ServerSocket
        lateinit var isr: InputStreamReader
        lateinit var bufferedReader: BufferedReader
        lateinit var pw: PrintWriter
        var h = Handler(this@SocketActivity.mainLooper)

        lateinit var message: String
        override fun run() {
            try {
                val port = userPort.toIntOrNull() ?: 8484
                ss = ServerSocket(port)
                while (true){
                    s = ss.accept()
                    isr = InputStreamReader(s.getInputStream())
                    bufferedReader = BufferedReader(isr)
                    message = bufferedReader.readLine()

                    val sockaddr: InetSocketAddress =
                        s.remoteSocketAddress as InetSocketAddress
                    val addr: InetAddress = sockaddr.getAddress()
                    h.post {
                        Toast.makeText(this@SocketActivity, "Тебе стучат в ${port}", Toast.LENGTH_SHORT).show()
                    }
                    pw = PrintWriter(s.getOutputStream())

                    val httpResponse = ("HTTP/1.1 200 OK\r\n"
                            + "Access-Control-Allow-Origin: https://nekr-localhost-test.herokuapp.com\r\n"
                            + "Access-Control-Allow-Methods: GET\r\n"
                            + "<h1>Hi</h1>"
                            )
                    val response = "<h1>Hi</h1> + ${Date()}"
                    pw.println("HTTP/1.1 200 OK");
                    pw.println(httpResponse)
                    pw.println("Content-Type: text/html");
                    pw.println("Content-Length: " + response.length);
                    pw.println();
                    pw.println(response);
                    pw.flush();
                    pw.close();
                    s.close();
                }
            } catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

}