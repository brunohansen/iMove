package br.com.bhansen.refactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.utils.Type;

public class EvaluateOr extends MoveMethodEvaluator {
	
	private MoveMethodEvaluator evaluator;
		
	public EvaluateOr(Type classFrom, String method, Type classTo, MetricFactory fac1, MetricFactory fac2, IProgressMonitor monitor) throws Exception {
		super(classFrom, method, classTo, fac2);
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		this.evaluator = new EvaluateSumMethod(classFrom, method, classTo, fac1, subMonitor.split(50));
		
		if(! this.evaluator.shouldMove()) {
			this.evaluator = new EvaluateSumMethod(classFrom, method, classTo, fac2, subMonitor.split(50));
		}
		
	}
		
	public boolean shouldMove() {
		return evaluator.shouldMove();
	}

	@Override
	public String toString() {
		return evaluator.toString();
	}

	public String toLineString() {
		return evaluator.toLineString();
	}

}
