package uk.co.blackpepper.penguin.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

final class PreferenceUtils
{
	// constants --------------------------------------------------------------
	
	public static final String SERVER_URL_KEY = "penguin_server_url";
	
	public static final String AUTHOR_NAME_KEY = "display_name";
	
	private static final String API_PATH = "/api";
	
	// constructors -----------------------------------------------------------

	private PreferenceUtils()
	{
		throw new AssertionError();
	}
	
	// public methods ---------------------------------------------------------
	
	public static String getServerApiUrl(Context context)
	{
		return getServerUrl(context) + API_PATH;
	}
	
	public static String getServerUrl(Context context)
	{
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String serverUrl = sharedPrefs.getString(SERVER_URL_KEY, null);
		return serverUrl;
	}
}
