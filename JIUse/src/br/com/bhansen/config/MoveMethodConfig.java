package br.com.bhansen.config;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

public class MoveMethodConfig extends Config {

	public enum Metric {
		CAMC_IUC, CCi, IC, ISCOMi, NHD, NHDM, WIC;
		
		public String toString() {
			return name().replaceAll("_", "/");
		}
	}

	protected static final String METRIC = "metric";
	protected static final String THRESHOLD = "threshold";
	protected static final String MUC_WEIGHT = "mucWeight";
	protected static final String MDC_WEIGHT = "mdcWeight";
	
	protected static final String REMOVE_NOT_USED = "removeNotUsed";
	protected static final String REMOVE_ATTRIBUTE = "removeAttribute";
	
	protected static final String SKIP_PRIVATE = "skipPrivate";
	protected static final String SKIP_ENVIED_DESTINATION = "skipEnviedDestination";
	
	protected static final Metric METRIC_DEF = Metric.IC;
	protected static final double THRESHOLD_DEF = 0;
	protected static final double WEIGHT_DEF = 0.5;
	
	protected static final boolean REMOVE_NOT_USED_DEF = true;
	protected static final boolean REMOVE_ATTRIBUTE_DEF = false;
	
	protected static final boolean SKIP_PRIVATE_DEF = true;
	protected static final boolean SKIP_ENVIED_DESTINATION_DEF = true;
	
	public static Metric getMetric() {
		return Metric.valueOf(Config.getPrefs().get(METRIC, METRIC_DEF.name()));
	}

	public static Double getThreshold() {
		return Config.getPrefs().getDouble(THRESHOLD, THRESHOLD_DEF);
	}

	public static Double getMucWeight() {
		return Config.getPrefs().getDouble(MUC_WEIGHT, WEIGHT_DEF);
	}

	public static Double getMdcWeight() {
		return Config.getPrefs().getDouble(MDC_WEIGHT, WEIGHT_DEF);
	}
	
	public static boolean removeIfNotUsed() {
		return Config.getPrefs().getBoolean(REMOVE_NOT_USED, REMOVE_NOT_USED_DEF);
	}
	
	public static boolean removeIfAttribute() {
		return Config.getPrefs().getBoolean(REMOVE_ATTRIBUTE, REMOVE_ATTRIBUTE_DEF);
	}
	
	public static boolean skipIfPrivate() {
		return Config.getPrefs().getBoolean(SKIP_PRIVATE, SKIP_PRIVATE_DEF);
	}
	public static boolean skipIfEnviedByDestination() {
		return Config.getPrefs().getBoolean(SKIP_ENVIED_DESTINATION, SKIP_ENVIED_DESTINATION_DEF);
	}

	public static void setMoveMethodConfig(double threshold, double mucWeight, double mdcWeight, Metric metric, boolean removeIfNotUsed, boolean removeIfAttribute, boolean skipIfPrivate, boolean skipIfEnviedByDestination) throws BackingStoreException {
		IEclipsePreferences prefs = Config.getPrefs();
	
		prefs.putDouble(THRESHOLD, threshold);		
		prefs.putDouble(MUC_WEIGHT, mucWeight);
		prefs.putDouble(MDC_WEIGHT, mdcWeight);	
		prefs.put(METRIC, metric.name());
		
		prefs.putBoolean(REMOVE_NOT_USED, removeIfNotUsed);
		prefs.putBoolean(REMOVE_ATTRIBUTE, removeIfAttribute);
		
		prefs.putBoolean(SKIP_PRIVATE, skipIfPrivate);
		prefs.putBoolean(SKIP_ENVIED_DESTINATION, skipIfEnviedByDestination);
		
		prefs.flush();
	}

}
