package br.com.bhansen.refactory;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.ltk.core.refactoring.Change;

import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.utils.MethodHelper;
import br.com.bhansen.utils.TypeHelper;

public class EvaluateSumMethod extends MoveMethodEvaluator  {

	private double oldValue;
	private double newValue;
	
	public EvaluateSumMethod(IType classFrom, String method, IType classTo, MetricFactory factory, double threshold) throws Exception {
		super(classFrom, method, classTo, factory, threshold);
		
		boolean skipIUC = false;
		
//		if(! MethodHelper.isPublic(classFrom, method) || MethodHelper.isCalledOnlyBy(classFrom, method, classTo) || MethodHelper.hasNoCaller(classFrom, method) || MethodHelper.isCalledOnlyBy(classFrom, method, classFrom)) {
//			skipIUC = true;
//		} else {
//			skipIUC = false;
//		}
		
		this.oldValue = factory.create(classFrom, method, skipIUC).getMetric();
		
		this.move();
	}
	
	private void move() throws Exception {
		MoveMethodRefactor refactor = new MoveMethodRefactor();
		
		Change undo = refactor.move(this.classFrom, this.iMethod, this.classTo);
		
		try {
			this.newValue = factory.create(this.classTo, MethodHelper.getMoveMethodName(MethodHelper.getMethodName(this.mSig)), refactor.getTypeNotUsed()).getMetric();
			
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

		txt.append(TypeHelper.getClassName(this.classFrom)).append(" ").append(this.oldValue).append("\n");
		txt.append(TypeHelper.getClassName(this.classTo)).append(" ").append(this.newValue).append("\n");
		txt.append("Skip IUC: ").append(this.factory.skipIUC()).append("\n");
		txt.append("Value difference: ").append(this.valueDifference).append("\n\n");

		return txt.toString();
	}

}
