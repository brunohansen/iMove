package br.com.bhansen.refactory;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.config.Config;
import br.com.bhansen.jdt.Project;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.utils.Movement;

public class EvaluatorFactory {
		
	public static MoveMethodEvaluator create(Project project, String movement, IProgressMonitor monitor) throws Exception {
		return create(project.findClassFrom(movement), Movement.getMethod(movement), project.findClassTo(movement), monitor);
	}
	
	public static MoveMethodEvaluator create(Type classFrom, String method, Type classTo, IProgressMonitor monitor) throws Exception {
//		return new EvaluateSumClass(classFrom, method, classTo, MetricFactory.createSkipUsageMethodMetricFactory(Config.getMetric()), monitor);
		return new EvaluateSumMethod(classFrom, method, classTo, MetricFactory.createSkipUsageMethodMetricFactory(Config.getMetric()), monitor);
	}
		
}
