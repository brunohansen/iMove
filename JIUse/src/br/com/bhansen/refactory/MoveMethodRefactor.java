package br.com.bhansen.refactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringExecutionStarter;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveStaticMembersProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;
import org.eclipse.swt.widgets.Shell;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.DeclarationMetric;

@SuppressWarnings("restriction")
public class MoveMethodRefactor {
	
	public static Change move(IType classFrom, IMethod iMethod, IType classTo) throws Exception {
		if (Flags.isStatic(iMethod.getFlags())) {
			return moveStatic(classFrom, iMethod, classTo);
		} else {
			return moveInstance(classFrom, iMethod, classTo);
		}
	}

	public static Change moveInstance(IType classFrom, IMethod iMethod, IType classTo) throws Exception {
		MoveInstanceMethodProcessor processor = new MoveInstanceMethodProcessor(iMethod,
				JavaPreferencesSettings.getCodeGenerationSettings(iMethod.getJavaProject()));
		Refactoring refactoring = new MoveRefactoring(processor);

		refactoring.checkInitialConditions(new NullProgressMonitor());

		processor.setMethodName(AbsMetric.getMoveMethodName(iMethod.getElementName()));
		processor.setInlineDelegator(true);
		processor.setRemoveDelegator(true);
		processor.setDeprecateDelegates(false);
		
		List<IVariableBinding> selectedTargets = selectFirstTarget(classTo, processor);

		if (selectedTargets.size() == 0)
			throw new Exception("Invalid target!");
		
		if (selectedTargets.size() == 1) {
			return performRefactoring(processor, refactoring, selectedTargets.get(0));
		} else {
			IVariableBinding bestTarget = getBestTarget(classTo, processor, refactoring, selectedTargets);
					
			return performRefactoring(processor, refactoring, bestTarget);
		}

	}

	private static IVariableBinding getBestTarget(IType classTo, MoveInstanceMethodProcessor processor, Refactoring refactoring, List<IVariableBinding> selectedTargets)
			throws IllegalArgumentException, Exception {
		IVariableBinding bestTarget = null;
		int numParameters = Integer.MAX_VALUE;
		
		for (IVariableBinding iVariableBinding : selectedTargets) {
			Change undo = performRefactoring(processor, refactoring, iVariableBinding);
			
			Set<String> parameters = DeclarationMetric.createParametersSet(classTo, AbsMetric.getMovedMethod(classTo, processor.getMethodName()));
			
			if(parameters.size() < numParameters) {
				numParameters = parameters.size();
				bestTarget = iVariableBinding;
			}
			
			undo.perform(new NullProgressMonitor());
		}
		return bestTarget;
	}

	private static Change performRefactoring(MoveInstanceMethodProcessor processor, Refactoring refactoring, IVariableBinding target) throws CoreException {
		IProgressMonitor monitor = new NullProgressMonitor();
		
		processor.setTarget(target);
		
		refactoring.checkFinalConditions(monitor);
		Change change = refactoring.createChange(monitor);
		Change undo = change.perform(monitor);
		return undo;
	}
	
	private static List<IVariableBinding> selectFirstTarget(IType classTo, MoveInstanceMethodProcessor processor) {
		//Em vez de getCandidateTargets()
		final IVariableBinding[] targets = processor.getPossibleTargets();
		
		List<IVariableBinding> selectedTargets = new ArrayList<>();
		
		for (int index = 0; index < targets.length; index++) {
			
			if (targets[index].getType().getJavaElement().equals(classTo)) {
												
				selectedTargets.add(targets[index]);
				
				return selectedTargets;
				
			}
		}
		
		return selectedTargets;
	}

	private static List<IVariableBinding> selectTargets(IType classTo, MoveInstanceMethodProcessor processor) {
		//Em vez de getCandidateTargets()
		final IVariableBinding[] targets = processor.getPossibleTargets();
		
		List<IVariableBinding> selectedTargets = new ArrayList<>();
		boolean need = true;
		
		for (int index = 0; index < targets.length; index++) {
			
			if (targets[index].getType().getJavaElement().equals(classTo)) {
												
				processor.setTarget(targets[index]);	
				
				if( need && processor.needsTargetNode() ) {
					selectedTargets.add(targets[index]);
				} else {
					if( need && ! processor.needsTargetNode() ) {
						need = false;
						selectedTargets.clear();
					}
					
					selectedTargets.add(targets[index]);
				}
				
			}
		}
		return selectedTargets;
	}
	
	public static Change moveStatic(IType classFrom, IMethod iMethod, IType classTo) throws Exception {
		IMember [] members = {iMethod};
		MoveStaticMembersProcessor processor = new MoveStaticMembersProcessor(members, JavaPreferencesSettings.getCodeGenerationSettings(iMethod.getJavaProject()));
		
		Refactoring refactoring = new MoveRefactoring(processor);

		IProgressMonitor monitor = new NullProgressMonitor();
		refactoring.checkInitialConditions(monitor);

		processor.setDelegateUpdating(false);
		processor.setDeprecateDelegates(false);
		
		processor.setDestinationTypeFullyQualifiedName(classTo.getFullyQualifiedName());

		refactoring.checkFinalConditions(monitor);
		Change change = refactoring.createChange(monitor);
		Change undo = change.perform(monitor);

		return undo;

	}

	public static void moveWizard(IMethod iMethod, Shell shell) throws Exception {
		RefactoringExecutionStarter.startMoveMethodRefactoring(iMethod, shell);
		//RefactoringExecutionStarter.startMoveStaticMembersRefactoring(members, shell);
	}

}
