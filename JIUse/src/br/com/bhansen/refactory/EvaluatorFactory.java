package br.com.bhansen.refactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import br.com.bhansen.metric.CompositeMetric;
import br.com.bhansen.metric.Metric;
import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.metric.camc.CAMCClass;
import br.com.bhansen.metric.camc.CAMCJMethod;
import br.com.bhansen.metric.iuc.IUCClass;
import br.com.bhansen.metric.iuc.IUCJMethod;
import br.com.bhansen.metric.nhdm.NHDMClass;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.Project;
import br.com.bhansen.utils.Type;

public class EvaluatorFactory {
	
	public static MoveMethodEvaluator create(Project project, String movement, String type, String metric, IProgressMonitor monitor) throws Exception {
		return create(project.findClassFrom(movement), Method.getSignature(movement), project.findClassTo(movement), type, metric, monitor);
	}
	
	public static MoveMethodEvaluator create(Type classFrom, String method, Type classTo, String type, String metric, IProgressMonitor monitor) throws Exception {	
		switch (type) {
		case "class":
			return new EvaluateSumClass(classFrom, method, classTo, createMetricFactory(type, metric), 0, monitor);
		case "method":
			return new EvaluateSumMethod(classFrom, method, classTo, createMetricFactory(type, metric), 0, monitor);
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
						return new CAMCClass(type, method, parameter, monitor);
					case "NHDM":
						return new NHDMClass(type, method, parameter, monitor);
					case "IUC + CAMC": 
					{
						SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
						return new CompositeMetric(new IUCClass(type, subMonitor.split(50)), new CAMCClass(type, method, parameter, subMonitor.split(50)));
					}
					case "IUC + NHDM":
					{
						SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
						return new CompositeMetric(new IUCClass(type, subMonitor.split(50)), new NHDMClass(type, method, parameter, subMonitor.split(50)));
					}
					default:
						throw new Exception("Invalid metric: " + metric + "!");
					}
				case "method":
					switch (metric) {
					case "IUC":
						return new IUCJMethod(type, method, monitor);
					case "CAMC":
						return new CAMCJMethod(type, method, parameter, monitor);
					case "IUC + CAMC":
						if(skipIUC)
							return new CAMCJMethod(type, method, parameter, monitor);
						else {
							SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
							return new CompositeMetric(new IUCJMethod(type, method, subMonitor.split(50)), new CAMCJMethod(type, method, parameter, subMonitor.split(50)));
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
