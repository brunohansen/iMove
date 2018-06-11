package br.com.bhansen.refactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.MetricFactory;

public abstract class MoveMethodEvaluator {
	
	protected double threshold;
	
	protected IType classFrom;
	protected IType classTo;
	protected IMethod iMethod;
	protected String mSig;
	
	protected double valueDifference;
	
	protected MetricFactory factory;
	
	public MoveMethodEvaluator(IType classFrom, String method, IType classTo, MetricFactory factory, double threshold) throws Exception {
		super();
		this.classFrom = classFrom;
		this.classTo = classTo;
				
		this.threshold = threshold;
		
		this.factory = factory;
		
		this.valueDifference = 0;
		
		setMethod(method);
	}
	
	private void setMethod(String method) throws Exception {
		
		this.mSig = AbsMetric.generateInnerSignature(method);

		IMethod[] methods = this.classFrom.getMethods();

		for (IMethod iMethod : methods) {
			if (AbsMetric.getSignature(iMethod).equals(this.mSig)) {
				this.iMethod = iMethod;
				return;
			}
		}
		
		this.mSig = AbsMetric.generateSignature(method);

		for (IMethod iMethod : methods) {
			if (AbsMetric.getSignature(iMethod).equals(this.mSig)) {
				this.iMethod = iMethod;
				return;
			}
		}

		throw new Exception("Method " + method + " not found!");
	}
	
	public boolean shouldMove() {
		return this.valueDifference >= threshold;
	}

	public String toLineString() {
		return new StringBuilder().append(new BigDecimal(this.valueDifference).setScale(6, RoundingMode.HALF_EVEN) + "-").append((shouldMove()) ? "0" : "1").append("\t").append(AbsMetric.getClassName(this.classFrom)).append("::").append(this.mSig)
				.append("\t").append(AbsMetric.getClassName(this.classTo)).toString();
	}

}