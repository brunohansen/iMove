package br.com.bhansen.refactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import br.com.bhansen.metric.CompositeMetric;
import br.com.bhansen.metric.Metric;
import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.metric.camc.CAMCClass;
import br.com.bhansen.metric.ic.ICMethod;
import br.com.bhansen.metric.ic.UICMethod;
import br.com.bhansen.metric.iuc.IUCClass;
import br.com.bhansen.metric.nhdm.NHDMClass;
import br.com.bhansen.utils.Movement;
import br.com.bhansen.utils.Project;
import br.com.bhansen.utils.Type;

public class EvaluatorFactory {
	
	public static MoveMethodEvaluator create(Project project, String movement, String type, String metric, IProgressMonitor monitor) throws Exception {
		return create(project.findClassFrom(movement), Movement.getMethod(movement), project.findClassTo(movement), type, metric, monitor);
	}
	
	public static MoveMethodEvaluator create(Type classFrom, String method, Type classTo, String type, String metric, IProgressMonitor monitor) throws Exception {	
		switch (type) {
		case "class":
			return new EvaluateSumClass(classFrom, method, classTo, createMetricFactory(type, metric), monitor);
		case "method":
			return new EvaluateSumMethod(classFrom, method, classTo, createMetricFactory(type, metric), monitor);
		default:
			throw new Exception("Invalid type: " + type + "!");
		}		
	}

	public static MetricFactory createMetricFactory(String eType, String metric) throws Exception {
		return new MetricFactory() {

			@Override
			public Metric create(Type type, String method, String parameter, boolean skipIUC, IProgressMonitor monitor) throws Exception {

				switch (eType) {
				case "class":
					switch (metric) {
					case "IUC":
						return new IUCClass(type, monitor);
					case "CAMC":
						return new CAMCClass(type, monitor);
					case "NHDM":
						return new NHDMClass(type, monitor);
					case "IUC + CAMC": 
					{
						SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
						return new CompositeMetric(new IUCClass(type, subMonitor.split(50)), new CAMCClass(type, subMonitor.split(50)));
					}
					case "IUC + NHDM":
					{
						SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
						return new CompositeMetric(new IUCClass(type, subMonitor.split(50)), new NHDMClass(type, subMonitor.split(50)));
					}
					default:
						throw new Exception("Invalid metric: " + metric + "!");
					}
				case "method":
					switch (metric) {
					case "IUC":
						return new UICMethod(type, method, monitor);
					case "CAMC":
						return new ICMethod(type, method, parameter, monitor);
					case "IUC + CAMC":
						if(skipIUC)
//							return new ISCOMMethod(type, method, parameter, monitor);
							return new ICMethod(type, method, parameter, monitor);
//							return new CCMethod(type, method, parameter, monitor);
//							return new NHDMMethod(type, method, parameter, monitor);
						else {
							SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
//							return new CompositeMetric(new UISCOMMethod(type, method, subMonitor.split(50)), new ISCOMMethod(type, method, parameter, subMonitor.split(50)));
							return new CompositeMetric(new UICMethod(type, method, subMonitor.split(50)), new ICMethod(type, method, parameter, subMonitor.split(50)));
//							return new CompositeMetric(new UCCMethod(type, method, subMonitor.split(50)), new CCMethod(type, method, parameter, subMonitor.split(50)));
//							return new CompositeMetric(new UNHDMMethod(type, method, subMonitor.split(50)), new NHDMMethod(type, method, parameter, subMonitor.split(50)));
						}
					default:
						throw new Exception("Invalid metric: " + metric + "!");
					}
				default:
					throw new Exception("Invalid type: " + eType + "!");
				}

			}

		};
	}

}
