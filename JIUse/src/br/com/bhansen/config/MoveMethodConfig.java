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
	
	protected static final Metric METRIC_DEF = Metric.IC;
	protected static final double THRESHOLD_DEF = 0;
	protected static final double WEIGHT_DEF = 0.5;
	
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

	public static void set(double threshold, double mucWeight, double mdcWeight, Metric metric) throws BackingStoreException {
		IEclipsePreferences prefs = Config.getPrefs();
	
		prefs.putDouble(THRESHOLD, threshold);		
		prefs.putDouble(MUC_WEIGHT, mucWeight);
		prefs.putDouble(MDC_WEIGHT, mdcWeight);	
		prefs.put(METRIC, metric.name());
		
		prefs.flush();
	}

}
