package br.com.bhansen.metric;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import br.com.bhansen.config.Config;
import br.com.bhansen.config.Config.MetricContext;
import br.com.bhansen.config.MoveMethodConfig;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.camc.CAMCClass;
import br.com.bhansen.metric.camc.CAMCMethod;
import br.com.bhansen.metric.cci.CCiClass;
import br.com.bhansen.metric.cci.CCiMethod;
import br.com.bhansen.metric.cci.UCCiClass;
import br.com.bhansen.metric.cci.UCCiMethod;
import br.com.bhansen.metric.ic.ICClass;
import br.com.bhansen.metric.ic.ICMethod;
import br.com.bhansen.metric.ic.UICClass;
import br.com.bhansen.metric.ic.UICMethod;
import br.com.bhansen.metric.iscomi.ISCOMiClass;
import br.com.bhansen.metric.iscomi.ISCOMiMethod;
import br.com.bhansen.metric.iscomi.UISCOMiClass;
import br.com.bhansen.metric.iscomi.UISCOMiMethod;
import br.com.bhansen.metric.iuc.IUCClass;
import br.com.bhansen.metric.iuc.IUCMethod;
import br.com.bhansen.metric.nhd.NHDClass;
import br.com.bhansen.metric.nhd.NHDMethod;
import br.com.bhansen.metric.nhd.UNHDClass;
import br.com.bhansen.metric.nhd.UNHDMethod;
import br.com.bhansen.metric.nhdm.NHDMClass;
import br.com.bhansen.metric.nhdm.NHDMMethod;
import br.com.bhansen.metric.nhdm.UNHDMClass;
import br.com.bhansen.metric.nhdm.UNHDMMethod;
import br.com.bhansen.metric.wic.UWICClass;
import br.com.bhansen.metric.wic.UWICMethod;
import br.com.bhansen.metric.wic.WICClass;
import br.com.bhansen.metric.wic.WICMethod;

public abstract class MetricFactory {
	
	private boolean skipUsage = false;
	
	public boolean skipUsage() {
		return skipUsage;
	}

	public Metric create(Type type, IProgressMonitor monitor) throws Exception {
		return create(type, null, monitor);
	}
		
	public Metric create(Type type, String method, IProgressMonitor monitor) throws Exception {
		return create(type, method, this.skipUsage, monitor);
	}
	
	public Metric create(Type type, String method, boolean skipUsage, IProgressMonitor monitor) throws Exception {
		this.skipUsage = skipUsage;
		return create(type, method, null, skipUsage, monitor);
	}
	
	public Metric create(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		return create(type, method, parameter, this.skipUsage, monitor);
	}
	
	public abstract Metric create(Type type, String method, String parameter, boolean skipUsage, IProgressMonitor monitor) throws Exception;
	
	public static MetricFactory createClassMetricFactory(MoveMethodConfig.Metric metric, Config.MetricContext context) throws Exception {
		if(context == MetricContext.DATA)
			return createClassMetricFactory(metric);
		else
			return createUClassMetricFactory(metric);
	}
	
	public static MetricFactory createMethodMetricFactory(MoveMethodConfig.Metric metric, Config.MetricContext context) throws Exception {
		if(context == MetricContext.DATA)
			return createMethodMetricFactory(metric);
		else
			return createUMethodMetricFactory(metric);
	}
	
	public static MetricFactory createSkipUsageMethodMetricFactory(MoveMethodConfig.Metric metric) throws Exception {
		return new MetricFactory() {

			@Override
			public Metric create(Type type, String method, String parameter, boolean skipUsage, IProgressMonitor monitor) throws Exception {
				if(skipUsage)
					return createMethodMetricFactory(metric).create(type, method, parameter, skipUsage, monitor);
				else
					return createCompositeMethodMetricFactory(metric).create(type, method, parameter, skipUsage, monitor);
			}
		};
	}
	
	public static MetricFactory createCompositeMethodMetricFactory(MoveMethodConfig.Metric metric) throws Exception {
		return new MetricFactory() {

			@Override
			public CompositeMetric create(Type type, String method, String parameter, boolean skipUsage, IProgressMonitor monitor) throws Exception {
				SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
				
				switch (metric) {
					case CAMC_IUC:
						return new CompositeMetric(new IUCMethod(type, method, subMonitor.split(50)), new CAMCMethod(type, method, parameter, subMonitor.split(50)));
					case CCi:
						return new CompositeMetric(new UCCiMethod(type, method, subMonitor.split(50)), new CCiMethod(type, method, parameter, subMonitor.split(50)));
					case IC:
						return new CompositeMetric(new UICMethod(type, method, subMonitor.split(50)), new ICMethod(type, method, parameter, subMonitor.split(50)));
					case ISCOMi:
						return new CompositeMetric(new UISCOMiMethod(type, method, subMonitor.split(50)), new ISCOMiMethod(type, method, parameter, subMonitor.split(50)));
					case NHD:
						return new CompositeMetric(new UNHDMethod(type, method, subMonitor.split(50)), new NHDMethod(type, method, parameter, subMonitor.split(50)));
					case NHDM:
						return new CompositeMetric(new UNHDMMethod(type, method, subMonitor.split(50)), new NHDMMethod(type, method, parameter, subMonitor.split(50)));
					case WIC:
						return new CompositeMetric(new UWICMethod(type, method, subMonitor.split(50)), new WICMethod(type, method, parameter, subMonitor.split(50)));
					default:
						throw new Exception("Invalid metric: " + metric + "!");
				}
			}
		};
	}
	
	public static MetricFactory createClassMetricFactory(MoveMethodConfig.Metric metric) throws Exception {
		return new MetricFactory() {

			@Override
			public DeclarationMetric create(Type type, String method, String parameter, boolean skipUsage, IProgressMonitor monitor) throws Exception {
				switch (metric) {
					case CAMC_IUC:
						return new CAMCClass(type, monitor);
					case CCi:
						return new CCiClass(type, monitor);
					case IC:
						return new ICClass(type, monitor);
					case ISCOMi:
						return new ISCOMiClass(type, monitor);
					case NHD:
						return new NHDClass(type, monitor);
					case NHDM:
						return new NHDMClass(type, monitor);
					case WIC:
						return new WICClass(type, monitor);
					default:
						throw new Exception("Invalid metric: " + metric + "!");
				}
			}
		};
	}
	
	public static MetricFactory createUClassMetricFactory(MoveMethodConfig.Metric metric) throws Exception {
		return new MetricFactory() {

			@Override
			public UsageMetric create(Type type, String method, String parameter, boolean skipUsage, IProgressMonitor monitor) throws Exception {
				switch (metric) {
					case CAMC_IUC:
						return new IUCClass(type, monitor);
					case CCi:
						return new UCCiClass(type, monitor);
					case IC:
						return new UICClass(type, monitor);
					case ISCOMi:
						return new UISCOMiClass(type, monitor);
					case NHD:
						return new UNHDClass(type, monitor);
					case NHDM:
						return new UNHDMClass(type, monitor);
					case WIC:
						return new UWICClass(type, monitor);
					default:
						throw new Exception("Invalid metric: " + metric + "!");
				}
			}
		};
	}
	
	public static MetricFactory createMethodMetricFactory(MoveMethodConfig.Metric metric) throws Exception {
		return new MetricFactory() {

			@Override
			public DeclarationMetric create(Type type, String method, String parameter, boolean skipUsage, IProgressMonitor monitor) throws Exception {
				switch (metric) {
					case CAMC_IUC:
						return new CAMCMethod(type, method, parameter, monitor);
					case CCi:
						return new CCiMethod(type, method, parameter, monitor);
					case IC:
						return new ICMethod(type, method, parameter, monitor);
					case ISCOMi:
						return new ISCOMiMethod(type, method, parameter, monitor);
					case NHD:
						return new NHDMethod(type, method, parameter, monitor);
					case NHDM:
						return new NHDMMethod(type, method, parameter, monitor);
					case WIC:
						return new WICMethod(type, method, parameter, monitor);
					default:
						throw new Exception("Invalid metric: " + metric + "!");
				}
			}
		};
	}
	
	public static MetricFactory createUMethodMetricFactory(MoveMethodConfig.Metric metric) throws Exception {
		return new MetricFactory() {

			@Override
			public UsageMetric create(Type type, String method, String parameter, boolean skipUsage, IProgressMonitor monitor) throws Exception {
				switch (metric) {
					case CAMC_IUC:
						return new IUCMethod(type, method, monitor);
					case CCi:
						return new UCCiMethod(type, method, monitor);
					case IC:
						return new UICMethod(type, method, monitor);
					case ISCOMi:
						return new UISCOMiMethod(type, method, monitor);
					case NHD:
						return new UNHDMethod(type, method, monitor);
					case NHDM:
						return new UNHDMMethod(type, method, monitor);
					case WIC:
						return new UWICMethod(type, method, monitor);
					default:
						throw new Exception("Invalid metric: " + metric + "!");
				}
			}
		};
	}

}
