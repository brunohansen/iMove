package br.com.bhansen.refactory;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.ltk.core.refactoring.Change;

import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.TypeHelper;

public class EvaluateSumClass extends MoveMethodEvaluator  {
	
	private double oldFromValue;
	private double oldToValue;

	private double newFromValue;
	private double newToValue;

	public EvaluateSumClass(IType classFrom, String method, IType classTo, MetricFactory factory, double threshold) throws Exception {
		super(classFrom, method, classTo, factory, threshold);
		
		this.oldFromValue = factory.create(classFrom).getMetric();
		this.oldToValue = factory.create(classTo).getMetric();
		
		this.move();
	}
	
	private void move() throws Exception {
		MoveMethodRefactor refactor = new MoveMethodRefactor();
		
		Change undo = refactor.move(this.classFrom, this.iMethod, this.classTo);
		
		try {
			this.newFromValue = factory.create(this.classFrom).getMetric();
			this.newToValue = factory.create(this.classTo, new Method(this.iMethod).getMoveName(), refactor.getTypeNotUsed()).getMetric();
			
			this.valueDifference = (this.newFromValue - this.oldFromValue) + (this.newToValue - this.oldToValue);
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();

		txt.append(TypeHelper.getClassName(this.classFrom)).append(" ").append(this.oldFromValue).append(" -> ")
				.append(this.newFromValue).append("\n");
		txt.append(TypeHelper.getClassName(this.classTo)).append(" ").append(this.oldToValue).append(" -> ")
				.append(this.newToValue).append("\n");
		txt.append("Value difference: ").append(this.valueDifference).append("\n\n");

		return txt.toString();
	}

}
