package br.com.bhansen.config;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

public class Config {

	private static IEclipsePreferences prefs;

	public enum MUCScope {
		WORK_SPACE, PROJECT, SOURCE_CODE;
		
		public String toString() {
			return name().replaceAll("_", " ");
		}
	}

	private static final String THRESHOLD = "threshold";
	private static final double THRESHOLD_DEF = 0;

	private static final String MUC_WEIGHT = "mucWeight";
	private static final String MDC_WEIGHT = "mdcWeight";
	private static final double WEIGHT_DEF = 0.5;

	private static final String MUC_SCOPE = "mucScope";
	private static final MUCScope MUC_SCOPE_DEF = MUCScope.WORK_SPACE;

	private static IEclipsePreferences getPrefs() {
		if (prefs == null) {
			prefs = InstanceScope.INSTANCE.getNode("iMove");
		}

		return prefs;
	}

	public static Double getThreshold() {
		return getPrefs().getDouble(THRESHOLD, THRESHOLD_DEF);
	}

	public static Double getMucWeight() {
		return getPrefs().getDouble(MUC_WEIGHT, WEIGHT_DEF);
	}

	public static Double getMdcWeight() {
		return getPrefs().getDouble(MDC_WEIGHT, WEIGHT_DEF);
	}

	public static MUCScope getMucScope() {
		return MUCScope.valueOf(getPrefs().get(MUC_SCOPE, MUC_SCOPE_DEF.name()));
	}

	public static void set(double threshold, double mucWeight, double mdcWeight, MUCScope mucScope) throws BackingStoreException {
		IEclipsePreferences prefs = getPrefs();

		prefs.putDouble(THRESHOLD, threshold);
		
		prefs.putDouble(MUC_WEIGHT, mucWeight);
		prefs.putDouble(MDC_WEIGHT, mdcWeight);
		
		prefs.put(MUC_SCOPE, mucScope.name());
		
		prefs.flush();
	}

}
