package br.com.bhansen.iuc.refactory;

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

import br.com.bhansen.iuc.metric.CAMCClass;
import br.com.bhansen.iuc.metric.CAMCJClass;
import br.com.bhansen.iuc.metric.CheckMoves;
import br.com.bhansen.iuc.metric.CompositeMetric;
import br.com.bhansen.iuc.metric.IUCClass;
import br.com.bhansen.iuc.metric.Metric;
import br.com.bhansen.iuc.metric.MetricClass;
import br.com.bhansen.iuc.metric.NHDMClass;
import br.com.bhansen.iuc.metric.NHDMNClass;

@SuppressWarnings("restriction")
public class EvaluateMoveMethod {
	
	private IType classFrom;
	private IType classTo;

	private double oldFromIUC;
	private double oldToIUC;

	private double newFromIUC;
	private double newToIUC;

	private double iucDifference;
	
	private String method;

	public EvaluateMoveMethod() {
	}

	public EvaluateMoveMethod(IType classFrom) throws Exception {
		this();
		this.setClassFrom(classFrom);
	}

	public EvaluateMoveMethod(IType classFrom, String method, IType classTo) throws Exception {
		this(classFrom);
		this.move(method, classTo);
	}
	
	public Metric createMetric(IType type) throws Exception {
		//return new CheckMoves();
		//return new NHDMNClass(type);
		//return new IUCClass(type); 
		//return new CAMCClass(type);
		return new NHDMClass(type);
		//return new CAMCJClass(type);
		//return new CompositeMetric(type);
	}

	public void setClassFrom(IType classFrom) throws Exception {
		this.classFrom = classFrom;

		this.oldFromIUC = createMetric(classFrom).getMetric();
	}

	public IType getClassFrom() {
		return classFrom;
	}

	public void setClassTo(IType classTo) throws Exception {
		this.classTo = classTo;

		this.oldToIUC = createMetric(classTo).getMetric();
	}

	public IType getClassTo() {
		return classTo;
	}
	
//	public void move2(String method) throws Exception {
//		IMethod iMethod = getIMethod(method);
//		
//		RefactoringExecutionStarter.startMoveMethodRefactoring(iMethod, shell);
//		
//		this.newFromIUC = new IUCClass(this.classFrom).getIUC();
//		this.newToIUC = new IUCClass(this.classTo).getIUC();
//		
//		this.iucDifference = (this.newFromIUC - this.oldFromIUC) + (this.newToIUC - this.oldToIUC);
//		
//	}

	public void move(String method) throws Exception {
		IMethod iMethod = getIMethod(method);
		
		MoveInstanceMethodProcessor processor= new MoveInstanceMethodProcessor(iMethod, JavaPreferencesSettings.getCodeGenerationSettings(iMethod.getJavaProject()));
		Refactoring refactoring= new MoveRefactoring(processor);
		
		IProgressMonitor monitor = new NullProgressMonitor();
		refactoring.checkInitialConditions(monitor);
		
		processor.setMethodName(MetricClass.getMoveMethodName(iMethod.getElementName()));
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
			this.newFromIUC = createMetric(this.classFrom).getMetric();
			this.newToIUC = createMetric(this.classTo).getMetric(MetricClass.getMoveMethodName(iMethod.getElementName()), MetricClass.getClassName(this.classFrom));
			
			this.iucDifference = (this.newFromIUC - this.oldFromIUC) + (this.newToIUC - this.oldToIUC);
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}

	private IMethod getIMethod(String method) throws Exception {
		this.method = MetricClass.generateInnerSignature(method);

		IMethod[] methods = this.classFrom.getMethods();

		for (IMethod iMethod : methods) {
			if (MetricClass.getSignature(iMethod).equals(this.method)) {
				return iMethod;
			}
		}
		
		this.method = MetricClass.generateSignature(method);

		for (IMethod iMethod : methods) {
			if (MetricClass.getSignature(iMethod).equals(this.method)) {
				return iMethod;
			}
		}

		throw new Exception("Method " + this.method + " not found!");
	}

	public void move(String method, IType classTo) throws Exception {
		this.setClassTo(classTo);
		this.move(method);
	}

	public boolean shouldMove() {
		return this.iucDifference > 0;
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();

		txt.append(MetricClass.getClassName(this.classFrom)).append(" ").append(this.oldFromIUC).append(" -> ")
				.append(this.newFromIUC).append("\n");
		txt.append(MetricClass.getClassName(this.classTo)).append(" ").append(this.oldToIUC).append(" -> ")
				.append(this.newToIUC).append("\n");
		txt.append("IUC difference: ").append(this.iucDifference).append("\n\n");

		return txt.toString();
	}

	public String toLineString() {
		return new StringBuilder().append((shouldMove()) ? "0" : "1").append("\t").append(MetricClass.getClassName(this.classFrom)).append("::").append(method)
				.append("\t").append(MetricClass.getClassName(this.classTo)).toString();
	}

}
