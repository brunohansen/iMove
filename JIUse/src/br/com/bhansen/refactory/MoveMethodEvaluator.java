package br.com.bhansen.refactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.MetricFactory;

public abstract class MoveMethodEvaluator {
	
	protected double threshold;
	
	protected IType classFrom;
	protected IType classTo;
	protected String method;
	
	protected double valueDifference;
	
	protected MetricFactory factory;
	
	public MoveMethodEvaluator(IType classFrom, String method, IType classTo, MetricFactory factory, double threshold) {
		super();
		this.classFrom = classFrom;
		this.classTo = classTo;
		this.method = method;
		
		this.threshold = threshold;
		
		this.factory = factory;
		
		this.valueDifference = 0;
	}
	
	public boolean shouldMove() {
		return this.valueDifference > threshold;
	}

	public String toLineString() {
		return new StringBuilder().append(new BigDecimal(this.valueDifference).setScale(6, RoundingMode.HALF_EVEN) + "-").append((shouldMove()) ? "0" : "1").append("\t").append(AbsMetric.getClassName(this.classFrom)).append("::").append(method)
				.append("\t").append(AbsMetric.getClassName(this.classTo)).toString();
	}

}