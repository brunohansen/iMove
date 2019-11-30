package br.com.bhansen.refactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.config.MoveMethodConfig;
import br.com.bhansen.jdt.Method;
import br.com.bhansen.jdt.Signature;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.MetricFactory;

public abstract class MoveMethodEvaluator {
	
	protected Type classFrom;
	protected Type classTo;
	protected Method method;
	protected String mSig;
	
	protected double valueDifference;
	
	protected MetricFactory factory;
	
	public MoveMethodEvaluator(Type classFrom, String method, Type classTo, MetricFactory factory) throws Exception {
		super();
		this.classFrom = classFrom;
		this.classTo = classTo;
				
		this.factory = factory;
		
		this.valueDifference = 0;
		
		setMethod(method);
	}
	
	private void setMethod(String method) throws Exception {
		
		this.mSig = Signature.normalizeInnerSignature(method);

		IMethod[] methods = this.classFrom.getIType().getMethods();

		for (IMethod iMethod : methods) {
			if (Method.getSignature(iMethod).equals(this.mSig)) {
				this.method = new Method(iMethod);
				return;
			}
		}
		
		this.mSig = Signature.normalizeSignature(method);

		for (IMethod iMethod : methods) {
			if (Method.getSignature(iMethod).equals(this.mSig)) {
				this.method = new Method(iMethod);
				return;
			}
		}

		throw new Exception("Method " + method + " not found!");
	}
	
	public boolean shouldMove() {
		return this.valueDifference >= MoveMethodConfig.getThreshold();
	}
	
	public String getValueText() {
		return new BigDecimal(this.valueDifference).setScale(6, RoundingMode.HALF_EVEN).toString();
	}
	
	public String getMessage() { 
		if(shouldMove()) {
			return "Value difference is greater than or equals to " + MoveMethodConfig.getThreshold() +"!";
		} else {
			return "Value difference is less than " + MoveMethodConfig.getThreshold() +"!";
		}
	}

	public String toLineString() throws Exception {
		return new StringBuilder()
				.append((shouldMove()) ? "0" : "1").append("\t")
				.append(getValueText()).append("\t")
				.append(this.classFrom.getName()).append("::").append(this.mSig).append("\t").append(this.classTo.getName()).append("\t")
				.append("Message: " + getMessage()).toString();
	}

}