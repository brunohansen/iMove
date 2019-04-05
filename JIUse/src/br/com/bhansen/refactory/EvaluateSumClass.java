package br.com.bhansen.refactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.ltk.core.refactoring.Change;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.MetricFactory;

public class EvaluateSumClass extends MoveMethodEvaluator  {
	
	private double oldFromValue;
	private double oldToValue;

	private double newFromValue;
	private double newToValue;

	public EvaluateSumClass(Type classFrom, String method, Type classTo, MetricFactory factory, IProgressMonitor monitor) throws Exception {
		super(classFrom, method, classTo, factory);
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		this.oldFromValue = factory.create(classFrom, subMonitor.split(20)).getMetric();
		this.oldToValue = factory.create(classTo, subMonitor.split(20)).getMetric();
		
		this.move(subMonitor.split(60));
	}
	
	private void move(IProgressMonitor monitor) throws Exception {
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		MoveMethodRefactor refactor = new MoveMethodRefactor();
		
		Change undo = refactor.move(this.classFrom, this.method, this.classTo, subMonitor.split(40));
		
		try {
			this.newFromValue = factory.create(this.classFrom, subMonitor.split(30)).getMetric();
			this.newToValue = factory.create(this.classTo, this.method.getMoveName(), refactor.getTypeNotUsed(), subMonitor.split(30)).getMetric();
			
			this.valueDifference = (this.newFromValue - this.oldFromValue) + (this.newToValue - this.oldToValue);
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();
		
		txt.append(shouldMove()? " Move!" : "Don't Move!");
		txt.append("\n");
		txt.append(this.classFrom.getName()).append(" ").append(this.oldFromValue).append(" -> ")
				.append(this.newFromValue).append("\n");
		txt.append(this.classTo.getName()).append(" ").append(this.oldToValue).append(" -> ")
				.append(this.newToValue).append("\n");
		txt.append("Value difference: ").append(this.valueDifference).append("\n\n");

		return txt.toString();
	}

}
