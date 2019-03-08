package br.com.bhansen.refactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.ltk.core.refactoring.Change;

import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.utils.Type;

public class EvaluateSumMethod extends MoveMethodEvaluator  {

	private double oldValue;
	private double newValue;
	
	public EvaluateSumMethod(Type classFrom, String method, Type classTo, MetricFactory factory, IProgressMonitor monitor) throws Exception {
		super(classFrom, method, classTo, factory);
		
		boolean skipIUC = false;
		
//		MethodWithCallers m = this.method.getMethodWithCallers();
//		
//		if(! m.hasCaller() || m.isPrivate() || m.isCalledOnlyBy(classTo) || m.isCalledOnlyBy(classFrom)) {
//			skipIUC = true;
//		} else {
//			skipIUC = false;
//		}
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		this.oldValue = factory.create(classFrom, method, skipIUC, subMonitor.split(30)).getMetric();
		
		this.move(subMonitor.split(70));
	}
	
	private void move(IProgressMonitor monitor) throws Exception {
		MoveMethodRefactor refactor = new MoveMethodRefactor();
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		Change undo = refactor.move(this.classFrom, this.method, this.classTo, subMonitor.split(50));
		
		try {
			this.newValue = factory.create(this.classTo, this.method.getMoveName(), refactor.getTypeNotUsed(), subMonitor.split(50)).getMetric();
			
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
		
		txt.append(shouldMove()? " Move!" : "Don't Move!");
		txt.append("\n");
		txt.append(this.classFrom.getName()).append(" ").append(this.oldValue).append("\n");
		txt.append(this.classTo.getName()).append(" ").append(this.newValue).append("\n");
		txt.append("Skip MUC: ").append(this.factory.skipIUC()).append("\n");
		txt.append("Value difference: ").append(this.valueDifference).append("\n\n");

		return txt.toString();
	}

}
