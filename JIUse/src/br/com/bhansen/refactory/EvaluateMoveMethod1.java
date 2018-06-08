package br.com.bhansen.refactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.MetricFactory;

@SuppressWarnings("restriction")
public class EvaluateMoveMethod1 extends MoveMethodEvaluator  {
	
	private double threshold;

	private double oldFromValue;
	private double oldToValue;

	private double newFromValue;
	private double newToValue;

	private double valueDifference;
	
	private MetricFactory factory;
	
	public EvaluateMoveMethod1(IType classFrom, String method, IType classTo, MetricFactory factory, double threshold) throws Exception {
		super(classFrom, method, classTo);
		
		this.threshold = threshold;
		
		this.factory = factory;
		
		this.oldFromValue = factory.create(classFrom).getMetric();
		this.oldToValue = factory.create(classTo).getMetric();
		
		this.move(method);
	}
	
//	public void move2(String method) throws Exception {
//		IMethod iMethod = getIMethod(method);
//		
//		RefactoringExecutionStarter.startMoveMethodRefactoring(iMethod, shell);
//		
//		this.newFromValue = new ValueClass(this.classFrom).getValue();
//		this.newToValue = new ValueClass(this.classTo).getValue();
//		
//		this.ValueDifference = (this.newFromValue - this.oldFromValue) + (this.newToValue - this.oldToValue);
//		
//	}

	private void move(String method) throws Exception {
		IMethod iMethod = getIMethod(method);
		
		MoveInstanceMethodProcessor processor= new MoveInstanceMethodProcessor(iMethod, JavaPreferencesSettings.getCodeGenerationSettings(iMethod.getJavaProject()));
		Refactoring refactoring= new MoveRefactoring(processor);
		
		IProgressMonitor monitor = new NullProgressMonitor();
		refactoring.checkInitialConditions(monitor);
		
		processor.setMethodName(AbsMetric.getMoveMethodName(iMethod.getElementName()));
		processor.setInlineDelegator(true);
		processor.setRemoveDelegator(true);
		processor.setDeprecateDelegates(false);
		
		final IVariableBinding[] targets= processor.getCandidateTargets();
		IVariableBinding target = null;
		for (int index= 0; index < targets.length; index++) {
			if (targets[index].getType().getJavaElement().equals(classTo)) {
				target = targets[index];
				break;
			}
		}
		
		if(target == null)
			throw new Exception("Invalid target!");
		
		processor.setTarget(target);
		refactoring.checkFinalConditions(monitor);
		Change change = refactoring.createChange(monitor);
		Change undo = change.perform(monitor);
		
		try {
			this.newFromValue = factory.create(this.classFrom).getMetric();
			this.newToValue = factory.create(this.classTo, AbsMetric.getMoveMethodName(iMethod.getElementName()), AbsMetric.getClassName(this.classFrom)).getMetric();
			
			this.valueDifference = (this.newFromValue - this.oldFromValue) + (this.newToValue - this.oldToValue);
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}

	private IMethod getIMethod(String method) throws Exception {
		this.method = AbsMetric.generateInnerSignature(method);

		IMethod[] methods = this.classFrom.getMethods();

		for (IMethod iMethod : methods) {
			if (AbsMetric.getSignature(iMethod).equals(this.method)) {
				return iMethod;
			}
		}
		
		this.method = AbsMetric.generateSignature(method);

		for (IMethod iMethod : methods) {
			if (AbsMetric.getSignature(iMethod).equals(this.method)) {
				return iMethod;
			}
		}

		throw new Exception("Method " + this.method + " not found!");
	}

	public boolean shouldMove() {
		return this.valueDifference > threshold;
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();

		txt.append(AbsMetric.getClassName(this.classFrom)).append(" ").append(this.oldFromValue).append(" -> ")
				.append(this.newFromValue).append("\n");
		txt.append(AbsMetric.getClassName(this.classTo)).append(" ").append(this.oldToValue).append(" -> ")
				.append(this.newToValue).append("\n");
		txt.append("Value difference: ").append(this.valueDifference).append("\n\n");

		return txt.toString();
	}

	public String toLineString() {
		return new StringBuilder().append(new BigDecimal(this.valueDifference).setScale(6, RoundingMode.HALF_EVEN) + "-").append((shouldMove()) ? "0" : "1").append("\t").append(AbsMetric.getClassName(this.classFrom)).append("::").append(method)
				.append("\t").append(AbsMetric.getClassName(this.classTo)).toString();
	}

}
