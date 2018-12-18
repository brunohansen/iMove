package br.com.bhansen.refactory;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.ltk.core.refactoring.Change;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.Metric;
import br.com.bhansen.metric.MetricFactory;

public class EvaluateSumMethod extends MoveMethodEvaluator  {

	private double oldValue;
	private double newValue;
	private boolean skipIUC;
	
	public EvaluateSumMethod(IType classFrom, String method, IType classTo, MetricFactory factory, double threshold) throws Exception {
		super(classFrom, method, classTo, factory, threshold);
		
		Metric m = factory.create(classFrom, method);
		
		if((! m.isPublicMethod()) || (m.isCalledOnlyBy(classTo)) || (m.hasNoCaller()) || (m.isCalledOnlyBy(classFrom))) {
			this.skipIUC = true;
		} else {
			this.skipIUC = false;
		}
		
		this.oldValue = m.getMetric(this.skipIUC);
		
		this.move();
	}
	
	private void move() throws Exception {
		Change undo = MoveMethodRefactor.move(this.classFrom, this.iMethod, this.classTo);
		
		try {
			this.newValue = factory.create(this.classTo, AbsMetric.getMoveMethodName(AbsMetric.getName(this.mSig)), AbsMetric.getClassName(this.classFrom)).getMetric(this.skipIUC);
			
			this.valueDifference = (this.newValue - this.oldValue);
			
			if(this.skipIUC)
				this.valueDifference += 0.1;
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();

		txt.append(AbsMetric.getClassName(this.classFrom)).append(" ").append(this.oldValue).append("\n");
		txt.append(AbsMetric.getClassName(this.classTo)).append(" ").append(this.newValue).append("\n");
		txt.append("Skip IUC: ").append(this.skipIUC).append("\n");
		txt.append("Value difference: ").append(this.valueDifference).append("\n\n");

		return txt.toString();
	}

}
