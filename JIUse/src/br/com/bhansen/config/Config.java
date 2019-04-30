package br.com.bhansen.config;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class Config {

	private static IEclipsePreferences prefs;
	
	public enum MetricContext {
		DATA, USAGE;
	}

	protected static IEclipsePreferences getPrefs() {
		if (prefs == null) {
			prefs = InstanceScope.INSTANCE.getNode("iMove");
		}

		return prefs;
	}

}
