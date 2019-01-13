package br.com.bhansen.refactory;

import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.utils.Type;

public class EvaluateOr extends MoveMethodEvaluator {
	
	private MoveMethodEvaluator evaluator;
		
	public EvaluateOr(Type classFrom, String method, Type classTo, MetricFactory fac1, MetricFactory fac2) throws Exception {
		super(classFrom, method, classTo, fac2, 0);
		
		this.evaluator = new EvaluateSumMethod(classFrom, method, classTo, fac1, 0);
		
		if(! this.evaluator.shouldMove()) {
			this.evaluator = new EvaluateSumMethod(classFrom, method, classTo, fac2, 0);
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
