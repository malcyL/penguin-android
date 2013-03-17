package uk.co.blackpepper.penguin.android;

import static uk.co.blackpepper.penguin.android.PreferenceUtils.AUTHOR_NAME_KEY;
import static uk.co.blackpepper.penguin.android.PreferenceUtils.SERVER_URL_KEY;

import java.net.MalformedURLException;
import java.net.URL;

import uk.co.blackpepper.penguin.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_general);

		setupAuthorNamePreference();
		setupServerUrlPreference(this);
	}

	private void setupAuthorNamePreference()
	{
		String defaultAuthorName = getResources().getString(R.string.pref_default_display_name);
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String authorName = sharedPreferences.getString(AUTHOR_NAME_KEY, defaultAuthorName);
		
		EditTextPreference authorNamePref = (EditTextPreference) getPreferenceScreen().findPreference(AUTHOR_NAME_KEY);
		authorNamePref.setSummary(authorName);

		authorNamePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				String newAuthorName = newValue.toString();
				preference.setSummary(newAuthorName);
				return true;
			}
		});
	}

	private void setupServerUrlPreference(final Context context)
	{
		String defaultServerUrl = getResources().getString(R.string.pref_default_server_url);
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String serverUrl = sharedPreferences.getString(SERVER_URL_KEY, defaultServerUrl);
		
		EditTextPreference serverUrlPref = (EditTextPreference) getPreferenceScreen().findPreference(SERVER_URL_KEY);
		serverUrlPref.setSummary(serverUrl);

		serverUrlPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				String newServerUrl = newValue.toString();
				boolean valid = isUrl(newServerUrl);
				
				if (valid)
				{
					preference.setSummary(newServerUrl);
				}
				else
				{
					Toast.makeText(context, R.string.toast_invalid_server_url, Toast.LENGTH_SHORT).show();
				}

				return valid;
			}
		});
	}

	private static boolean isUrl(String url)
	{
		try
		{
			new URL(url);
			return true;
		}
		catch (MalformedURLException exception)
		{
			return false;
		}
	}
}
