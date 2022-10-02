package com.hieu.webview_application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;

import android.view.View;

import android.webkit.WebView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity  {


    private  EditText addressBar;

    private WebView webView;
    private Button buttonGo;
    private Button buttonStatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonGo =(Button)findViewById(R.id.button_go);
        buttonStatic =(Button)findViewById(R.id.button_static);

        addressBar =(EditText)findViewById(R.id.editText_addressBar);
        webView =(WebView)findViewById(R.id.webView);


        // Tùy biến WebViewClient để điều khiển các sự kiện trên WebView
        webView.setWebViewClient(new MyWebViewClient(addressBar));

        buttonGo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                goUrl();
            }
        });

        buttonStatic.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStaticContent();
            }
        });
    }

    private void goUrl()  {
        String url = addressBar.getText().toString().trim();
        if(url.isEmpty())  {
            Toast.makeText(this,"Please enter url",Toast.LENGTH_SHORT).show();
            return;
        }
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }


    private void showStaticContent()  {
        String staticContent="<h2>Select web page</h2>"
                + "<ul><li><a href='http://eclipse.org'>Eclipse</a></li>"
                +"<li><a href='http://google.com'>Google</a></li></ul>";
        webView.loadData(staticContent, "text/html", "UTF-8");
    }

}
