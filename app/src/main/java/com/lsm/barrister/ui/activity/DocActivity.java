package com.lsm.barrister.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.lsm.barrister.R;


/**
 *Title: DocActivity.java
 *Description:  
 *@author lsm
 *@date 2015-4-2
 */
public class DocActivity extends BaseActivity {

	public static final String KEY_TITLE = "key.title";
	public static final String KEY_FILE = "key.mPickingFile";
	
	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doc);

		String title = getIntent().getStringExtra(KEY_TITLE);
		String file = getIntent().getStringExtra(KEY_FILE);
		
		initActionBar();
		
		setTitle(title);
		
		initWebView(file);
	}

	/**
	 * 初始化WebView
	 * @param url
	 */
	public void initWebView(String url) {
		
		webView = (WebView) findViewById(R.id.webview_doc);
		
		WebSettings webSettings = webView.getSettings(); 
		webSettings.setLoadWithOverviewMode(true); 
		webSettings.setUseWideViewPort(true);
		webView.loadUrl(url);
		
	}
	
	
	/**
	 * 设置标题
	 * @param title
	 */
	private void setTitle(String title){
		titleView.setText(title);
	}
	
	TextView titleView;
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		// TODO Auto-generated method stub
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		titleView = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.setVisibility(View.GONE);
		webView.destroy();
	}

	
	
}
