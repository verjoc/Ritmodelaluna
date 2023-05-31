package hu.verjoc.ritmodelaluna

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import hu.verjoc.ritmodelaluna.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest
        ): Boolean {
            if (URLUtil.isNetworkUrl(request.url.toString())) {
                view.loadUrl(request.url.toString())
                progressBar.visibility=View.VISIBLE
                webView.visibility=View.INVISIBLE

            }
            else {


                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(request.url.toString()))
                if (intent.resolveActivity(packageManager)!=null) {
                    startActivity(intent)
                    view.loadData("loading", "text/html; charsetutf-8", null)
                }
                else {
                    Toast.makeText(applicationContext, "Nem lehet megnyitni a hivatkozást!", Toast.LENGTH_SHORT).show()
                    view.loadData("error", "text/html; charsetutf-8", null)
                    return false
                }
            }
            return true

        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.visibility= View.GONE
            webView.visibility=View.VISIBLE
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.loadUrl("https://www.ritmodelaluna.hu")
        webView.webViewClient= MyWebViewClient()
        webView.settings.apply {
            javaScriptEnabled=true
            setSupportZoom(true)
            domStorageEnabled=true
            setAppCacheEnabled(true)
            loadsImagesAutomatically=true
            mixedContentMode=WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }


    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
        else {
            super.onBackPressed()
        }
    }
}