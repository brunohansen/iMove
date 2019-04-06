package br.com.bhansen.config;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

public class Config {

	private static IEclipsePreferences prefs;
	
	public enum Metric {
		CAMC_IUC, CCi, IC, ISCOMi, NHD, NHDM, WIC;
		
		public String toString() {
			return name().replaceAll("_", "/");
		}
	}
	
	public enum MetricType {
		REGULAR, TIGHT;
	}
	
	public enum MetricContext {
		DATA, USAGE;
	}

	public enum UsageScope {
		WORK_SPACE, PROJECT, SOURCE_CODE;
		
		public String toString() {
			return name().replaceAll("_", " ");
		}
	}
	
	private static final String METRIC = "metric";
	private static final Metric METRIC_DEF = Metric.IC;
	
	private static final String METRIC_TYPE = "metricType";
	private static final MetricType METRIC_TYPE_DEF = MetricType.REGULAR;

	private static final String THRESHOLD = "threshold";
	private static final double THRESHOLD_DEF = 0;

	private static final String MUC_WEIGHT = "mucWeight";
	private static final String MDC_WEIGHT = "mdcWeight";
	private static final double WEIGHT_DEF = 0.5;

	private static final String USAGE_SCOPE = "usageScope";
	private static final UsageScope USAGE_SCOPE_DEF = UsageScope.WORK_SPACE;

	private static IEclipsePreferences getPrefs() {
		if (prefs == null) {
			prefs = InstanceScope.INSTANCE.getNode("iMove");
		}

		return prefs;
	}
	
	public static Metric getMetric() {
		return Metric.valueOf(getPrefs().get(METRIC, METRIC_DEF.name()));
	}
	
	public static MetricType getMetricType() {
		return MetricType.valueOf(getPrefs().get(METRIC_TYPE, METRIC_TYPE_DEF.name()));
	}
	
	public static boolean isMetricRegular() {
		return getMetricType() == MetricType.REGULAR;
	}
	
	public static boolean isMetricTight() {
		return getMetricType() == MetricType.TIGHT;
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

	public static UsageScope getUsageScope() {
		return UsageScope.valueOf(getPrefs().get(USAGE_SCOPE, USAGE_SCOPE_DEF.name()));
	}

	public static void set(double threshold, double mucWeight, double mdcWeight, UsageScope usageScope, MetricType metricType, Metric metric) throws BackingStoreException {
		IEclipsePreferences prefs = getPrefs();

		prefs.putDouble(THRESHOLD, threshold);
		
		prefs.putDouble(MUC_WEIGHT, mucWeight);
		prefs.putDouble(MDC_WEIGHT, mdcWeight);
		
		prefs.put(USAGE_SCOPE, usageScope.name());
		
		prefs.put(METRIC_TYPE, metricType.name());
		
		prefs.put(METRIC, metric.name());
		
		prefs.flush();
	}

}
