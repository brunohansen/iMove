package br.com.bhansen.iuc.refactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.refactoring.descriptors.InlineMethodDescriptor;
import org.eclipse.jdt.core.refactoring.descriptors.MoveMethodDescriptor;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringExecutionStarter;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;
import org.eclipse.swt.widgets.Shell;

import br.com.bhansen.iuc.metric.IUCClass;

public class EvaluateMoveMethod {
	
	private Shell shell;

	private IType classFrom;
	private IType classTo;

	private float oldFromIUC;
	private float oldToIUC;

	private float newFromIUC;
	private float newToIUC;

	private float iucDifference;
	
	private String method;

	public EvaluateMoveMethod() {
	}

	public EvaluateMoveMethod(IType classFrom) throws Exception {
		this();
		this.setClassFrom(classFrom);
	}

	public EvaluateMoveMethod(Shell shell, IType classFrom, String method, IType classTo) throws Exception {
		this(classFrom);
		this.shell = shell;
		this.move(method, classTo);
	}

	public void setClassFrom(IType classFrom) throws Exception {
		this.classFrom = classFrom;

		this.oldFromIUC = new IUCClass(classFrom).getIUC();
	}

	public IType getClassFrom() {
		return classFrom;
	}

	public void setClassTo(IType classTo) throws Exception {
		this.classTo = classTo;

		this.oldToIUC = new IUCClass(classTo).getIUC();
	}

	public IType getClassTo() {
		return classTo;
	}
	
	public void move2(String method) throws Exception {
		IMethod iMethod = getIMethod(method);
		
		RefactoringExecutionStarter.startMoveMethodRefactoring(iMethod, shell);
		
		this.newFromIUC = new IUCClass(this.classFrom).getIUC();
		this.newToIUC = new IUCClass(this.classTo).getIUC();
		
		this.iucDifference = (this.newFromIUC - this.oldFromIUC) + (this.newToIUC - this.oldToIUC);
		
	}

	public void move(String method) throws Exception {
		IMethod iMethod = getIMethod(method);

		//TODO Quando tiver numero no fim fazer inline
		//TODO Pode ter colisao verificar se ja tem m�todo no destino
		MoveInstanceMethodProcessor processor= new MoveInstanceMethodProcessor(iMethod, JavaPreferencesSettings.getCodeGenerationSettings(iMethod.getJavaProject()));
		Refactoring refactoring= new MoveRefactoring(processor);
		
		IProgressMonitor monitor = new NullProgressMonitor();
		refactoring.checkInitialConditions(monitor);
		
		//processor.setMethodName("new name");
		processor.setInlineDelegator(true);
		processor.setRemoveDelegator(true);
		processor.setDeprecateDelegates(false);
		
		final IVariableBinding[] targets= processor.getCandidateTargets();
		for (int index= 0; index < targets.length; index++) {
			if (targets[index].getType().getJavaElement().equals(classTo)) {
				processor.setTarget(targets[index]);
				break;
			}
		}
		
		refactoring.checkFinalConditions(monitor);
		Change change = refactoring.createChange(monitor);
		Change undo = change.perform(monitor);;
		
		try {
			this.newFromIUC = new IUCClass(this.classFrom).getIUC();
			this.newToIUC = new IUCClass(this.classTo).getIUC();
			
			this.iucDifference = (this.newFromIUC - this.oldFromIUC) + (this.newToIUC - this.oldToIUC);
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}

	private IMethod getIMethod(String method) throws Exception {
		this.method = generateSignature(method);

		IMethod[] methods = this.classFrom.getMethods();

		for (IMethod iMethod : methods) {
			if (IUCClass.getSignature(iMethod).equals(this.method)) {
				return iMethod;
			}
			;
		}

		throw new Exception("Method " + this.method + " not found!");
	}

	public String generateSignature(String method) throws Exception {
		method = method.replaceAll("\\s", " ");// Change whitespace character: [
												// \t\n\x0B\f\r]
		method = method.replaceAll(",", ", ");// Add space after comma
		method = method.replaceAll(" {2,}", " ");// Remove more than one
		method = method.replaceAll("[a-z|A-Z|_|$]*?\\.", "");// Remove packages

		return method;
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

		txt.append(IUCClass.getClassName(this.classFrom)).append(" ").append(this.oldFromIUC).append(" -> ")
				.append(this.newFromIUC).append("\n");
		txt.append(IUCClass.getClassName(this.classTo)).append(" ").append(this.oldToIUC).append(" -> ")
				.append(this.newToIUC).append("\n");
		txt.append("IUC difference: ").append(this.iucDifference).append("\n\n");

		return txt.toString();
	}

	public String toLineString() {
		return new StringBuilder().append(IUCClass.getClassName(this.classFrom)).append("::").append(method)
				.append("\t").append(IUCClass.getClassName(this.classTo)).append("\t")
				.append((shouldMove()) ? "IOK" : "INOK").toString();
	}

}
