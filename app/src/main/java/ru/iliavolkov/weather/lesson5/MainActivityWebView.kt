package ru.iliavolkov.weather.lesson5

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import ru.iliavolkov.weather.databinding.ActivityMainWebviewBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collector
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivityWebView : AppCompatActivity() {

    private lateinit var binding: ActivityMainWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOk.setOnClickListener{
            request(binding.edUrl.text.toString())
        }

    }

    private fun request(path:String) {
        val handler = Handler(mainLooper)
        Thread{
            val url = URL(path)
            val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
                requestMethod = "GET"
                readTimeout = 5000
            }
            val bufferedReader = BufferedReader(InputStreamReader(httpsURLConnection.inputStream))
            val result = convertBufferToResult(bufferedReader)
            handler.post{
                binding.webView.loadDataWithBaseURL(null,result,"text/html; charset=utf-8","utf-8",null)
            }
            httpsURLConnection.disconnect()
        }.start()
    }

    private fun convertBufferToResult(buffer: BufferedReader):String {
        return buffer.lines().collect(Collectors.joining("\n"))
    }
}