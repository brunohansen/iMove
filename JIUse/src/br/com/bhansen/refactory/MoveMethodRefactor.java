package br.com.bhansen.refactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringExecutionStarter;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;
import org.eclipse.swt.widgets.Shell;

import br.com.bhansen.metric.AbsMetric;

@SuppressWarnings("restriction")
public class MoveMethodRefactor {

	public static Change move(IType classFrom, IMethod iMethod, IType classTo) throws Exception {
		MoveInstanceMethodProcessor processor = new MoveInstanceMethodProcessor(iMethod,
				JavaPreferencesSettings.getCodeGenerationSettings(iMethod.getJavaProject()));
		Refactoring refactoring = new MoveRefactoring(processor);

		IProgressMonitor monitor = new NullProgressMonitor();
		refactoring.checkInitialConditions(monitor);

		processor.setMethodName(AbsMetric.getMoveMethodName(iMethod.getElementName()));
		processor.setInlineDelegator(true);
		processor.setRemoveDelegator(true);
		processor.setDeprecateDelegates(false);
		
		//Em vez de getCandidateTargets()
		final IVariableBinding[] targets = processor.getPossibleTargets();
		IVariableBinding target = null;
		for (int index = 0; index < targets.length; index++) {
			
			if (targets[index].getType().getJavaElement().equals(classTo)) {
							
				//Pego o primeiro independente de qualquer coisa
				if (target == null) {
					target = targets[index];
				}
					
				processor.setTarget(targets[index]);	
				
				//Troco pelo primeiro que não precisa de parametro, porem podem ter outros melhores
				if(! processor.needsTargetNode()) {
					target = targets[index];
					break;
				}
				
				
			}
		}

		if (target == null)
			throw new Exception("Invalid target!");

		processor.setTarget(target);
		refactoring.checkFinalConditions(monitor);
		Change change = refactoring.createChange(monitor);
		Change undo = change.perform(monitor);

		return undo;

	}

	public static void moveWizard(IMethod iMethod, Shell shell) throws Exception {
		RefactoringExecutionStarter.startMoveMethodRefactoring(iMethod, shell);
	}

}
