package br.com.bhansen.config;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

import br.com.bhansen.jdt.MethodWithCallers;
import br.com.bhansen.jdt.Type;

public class UsageMetricConfig extends Config {

	public enum UsageScope {
		WORK_SPACE, PROJECT, SOURCE_CODE;
		
		public String toString() {
			return name().replaceAll("_", " ");
		}
	}

    static final String HIDE_METHODS = "hideMethods";
    static final String INTERNAL_CALLS = "internalCalls";
	static final String USAGE_SCOPE = "usageScope";
	
    static final boolean HIDE_METHODS_DEF = true;
    static final boolean INTERNAL_CALLS_DEF = true;
	static final UsageScope USAGE_SCOPE_DEF = UsageScope.WORK_SPACE;
	
	public static boolean use(MethodWithCallers method, Type type) throws Exception {
		
		// Dont add not called
		if (hideMethods() && ! method.hasCaller())
			return false;
		
		// Dont add fake public
		if(hideMethods() && method.isCalledOnlyBy(type))
			return false;
		
		return true;
	}
	
	public static boolean hideMethods() {
		return getPrefs().getBoolean(HIDE_METHODS, HIDE_METHODS_DEF);
	}
	
	public static boolean useInternalCalls() {
		return getPrefs().getBoolean(INTERNAL_CALLS, INTERNAL_CALLS_DEF);
	}

	public static UsageScope getUsageScope() {
		return UsageScope.valueOf(getPrefs().get(USAGE_SCOPE, USAGE_SCOPE_DEF.name()));
	}
	
	public static void setUsageMetricConfig(boolean hideMethods, boolean internalCalls, UsageScope usageScope) throws BackingStoreException {
		IEclipsePreferences prefs = getPrefs();
		
		prefs.putBoolean(HIDE_METHODS, hideMethods);
		prefs.putBoolean(INTERNAL_CALLS, internalCalls);
		prefs.put(UsageMetricConfig.USAGE_SCOPE, usageScope.name());
		
		prefs.flush();
	}

}
