package com.alox1d.phoneformatting.data

import android.os.AsyncTask
import android.util.Log
import kotlinx.coroutines.*
import java.io.DataOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket
import kotlin.coroutines.CoroutineContext

class MessageSender: CoroutineScope {
    private lateinit var s: Socket
    private lateinit var dos: DataOutputStream
    private lateinit var pw: PrintWriter

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job // to run code in Main(UI) Thread

    // call this method to cancel a coroutine when you don't need it anymore,
    // e.g. when user closes the screen
    fun cancel() {
        job.cancel()
    }

    fun execute(message: String,port: Int) = launch {

        onPreExecute()
        val result = doInBackground(message, port) // runs in background thread without blocking the Main Thread
        onPostExecute(result)
    }

    private suspend fun doInBackground(message: String, port: Int): String = withContext(Dispatchers.IO) { // to run code in Background Thread
        // do async work
//        delay(1000) // simulate async work
        try {
            s = Socket("10.0.2.2", port)
            pw = PrintWriter(s.getOutputStream())
            pw.write(message)
            pw.flush()
            s.close()
        } catch (e: Exception) {
            Log.e("doInBackground", "doInBackground: ",e )
        }
        return@withContext "SomeResult"
    }

    // Runs on the Main(UI) Thread
    private fun onPreExecute() {
        // show progress
    }

    // Runs on the Main(UI) Thread
    private fun onPostExecute(result: String) {
        // hide progress
    }
}