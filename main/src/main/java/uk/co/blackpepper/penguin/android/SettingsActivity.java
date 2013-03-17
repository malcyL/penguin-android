package uk.co.blackpepper.penguin.android;

import static uk.co.blackpepper.penguin.android.PreferenceUtils.AUTHOR_NAME_KEY;
import static uk.co.blackpepper.penguin.android.PreferenceUtils.PENGUIN_SERVER_URL_KEY;

import java.net.MalformedURLException;
import java.net.URL;

import uk.co.blackpepper.penguin.R;
import android.content.Context;
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
		EditTextPreference autorUrlPref = (EditTextPreference) getPreferenceScreen().findPreference(AUTHOR_NAME_KEY);
		String defaultAuthorName = getResources().getString(R.string.pref_default_display_name);
		autorUrlPref.setSummary(PreferenceManager.getDefaultSharedPreferences(this).getString(AUTHOR_NAME_KEY,
			defaultAuthorName));

		autorUrlPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				String stringValue = newValue.toString();
				preference.setSummary(stringValue);
				return true;
			}
		});
	}

	private void setupServerUrlPreference(final Context context)
	{
		EditTextPreference serverNamePref = (EditTextPreference) getPreferenceScreen()
			.findPreference(PENGUIN_SERVER_URL_KEY);
		String defaultServerUrl = getResources().getString(R.string.pref_default_server_url);
		serverNamePref.setSummary(PreferenceManager.getDefaultSharedPreferences(this).getString(PENGUIN_SERVER_URL_KEY,
			defaultServerUrl));

		serverNamePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				String newServerUrl = newValue.toString();
				Boolean valid = true;
				
				if (!validServerUrl(newServerUrl))
				{
					Toast.makeText(context, R.string.toast_invalid_server_url, Toast.LENGTH_SHORT).show();
					valid = false;
				}
				else
				{
					preference.setSummary(newServerUrl);
				}

				return valid;
			}
		});
	}

	private boolean validServerUrl(String serverUrl)
	{
		try
		{
			new URL(serverUrl);
			return true;
		}
		catch (MalformedURLException e)
		{
			return false;
		}
	}
}
