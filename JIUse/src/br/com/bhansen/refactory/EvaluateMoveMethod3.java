package br.com.bhansen.refactory;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.ltk.core.refactoring.Change;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.MetricFactory;

public class EvaluateMoveMethod3 extends MoveMethodEvaluator  {

	private double oldValue;

	private double newValue;
	
	public EvaluateMoveMethod3(IType classFrom, String method, IType classTo, MetricFactory factory, double threshold) throws Exception {
		super(classFrom, method, classTo, factory, threshold);
		
		this.oldValue = factory.create(classFrom, method).getMetric();
		
		this.move(method);
	}
	
	private void move(String method) throws Exception {
		Change undo = MoveMethodRefactor.move(this.classFrom, method, this.classTo);
		
		try {
			this.newValue = factory.create(this.classTo, AbsMetric.getMoveMethodName(AbsMetric.getName(method)), AbsMetric.getClassName(this.classFrom)).getMetric();
			
			this.valueDifference = (this.newValue - this.oldValue);
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();

		txt.append(AbsMetric.getClassName(this.classFrom)).append(" ").append(this.oldValue).append("\n");
		txt.append(AbsMetric.getClassName(this.classTo)).append(" ").append(this.oldValue).append("\n");
		txt.append("Value difference: ").append(this.valueDifference).append("\n\n");

		return txt.toString();
	}

}
