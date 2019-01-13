package br.com.bhansen.refactory;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;

import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.utils.Type;

public class EvaluateSumClass extends MoveMethodEvaluator  {
	
	private double oldFromValue;
	private double oldToValue;

	private double newFromValue;
	private double newToValue;

	public EvaluateSumClass(Type classFrom, String method, Type classTo, MetricFactory factory, double threshold) throws Exception {
		super(classFrom, method, classTo, factory, threshold);
		
		this.oldFromValue = factory.create(classFrom).getMetric();
		this.oldToValue = factory.create(classTo).getMetric();
		
		this.move();
	}
	
	private void move() throws Exception {
		MoveMethodRefactor refactor = new MoveMethodRefactor();
		
		Change undo = refactor.move(this.classFrom, this.method, this.classTo);
		
		try {
			this.newFromValue = factory.create(this.classFrom).getMetric();
			this.newToValue = factory.create(this.classTo, this.method.getMoveName(), refactor.getTypeNotUsed()).getMetric();
			
			this.valueDifference = (this.newFromValue - this.oldFromValue) + (this.newToValue - this.oldToValue);
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();

		txt.append(this.classFrom.getName()).append(" ").append(this.oldFromValue).append(" -> ")
				.append(this.newFromValue).append("\n");
		txt.append(this.classTo.getName()).append(" ").append(this.oldToValue).append(" -> ")
				.append(this.newToValue).append("\n");
		txt.append("Value difference: ").append(this.valueDifference).append("\n\n");

		return txt.toString();
	}

}
