package br.com.bhansen.refactory;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;

import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.utils.MethodWithCallers;
import br.com.bhansen.utils.Type;

public class EvaluateSumMethod extends MoveMethodEvaluator  {

	private double oldValue;
	private double newValue;
	
	public EvaluateSumMethod(Type classFrom, String method, Type classTo, MetricFactory factory, double threshold) throws Exception {
		super(classFrom, method, classTo, factory, threshold);
		
		boolean skipIUC = false;
		
		MethodWithCallers m = this.method.getMethodWithCallers();
		
		if(! m.hasCaller()) { // || m.isCalledOnlyBy(classFrom) || m.isCalledOnlyBy(classTo)) {
			skipIUC = true;
		} else {
			skipIUC = false;
		}
		
		this.oldValue = factory.create(classFrom, method, skipIUC).getMetric();
		
		this.move();
	}
	
	private void move() throws Exception {
		MoveMethodRefactor refactor = new MoveMethodRefactor();
		
		Change undo = refactor.move(this.classFrom, this.method, this.classTo);
		
		try {
			this.newValue = factory.create(this.classTo, this.method.getMoveName(), refactor.getTypeNotUsed()).getMetric();
			
			this.valueDifference = (this.newValue - this.oldValue);
			
//			if(this.factory.skipIUC())
//				this.valueDifference += 0.1;
			
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();

		txt.append(this.classFrom.getName()).append(" ").append(this.oldValue).append("\n");
		txt.append(this.classTo.getName()).append(" ").append(this.newValue).append("\n");
		txt.append("Skip IUC: ").append(this.factory.skipIUC()).append("\n");
		txt.append("Value difference: ").append(this.valueDifference).append("\n\n");

		return txt.toString();
	}

}
