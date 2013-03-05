package uk.co.blackpepper.penguin.android;

import uk.co.blackpepper.penguin.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class QueueListActivity extends Activity
{
	private String currentServerUrl = null;

	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_queue_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_queue_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.menu_settings:
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				startActivity(settingsIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume()
	{
		loadWebview();
		super.onResume();
	}

	private void loadWebview()
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String serverUrl = sharedPrefs.getString("penguin_server_url", "http://malcyl.github.com/penguin-android/");
		if (!serverUrl.equals(currentServerUrl))
		{
			currentServerUrl = serverUrl;
			Log.i("loadWebView", String.format("Loading webview with url %s", currentServerUrl));

			webView = (WebView) findViewById(R.id.webView1);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl(currentServerUrl);
		}
	}
}
