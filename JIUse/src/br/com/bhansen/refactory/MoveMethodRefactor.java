package br.com.bhansen.refactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringExecutionStarter;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveStaticMembersProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;
import org.eclipse.swt.widgets.Shell;

import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.TypeHelper;

@SuppressWarnings("restriction")
public class MoveMethodRefactor {
	
	private String typeNotUsed = null;
	
	public Change move(IType classFrom, IMethod iMethod, IType classTo) throws Exception {
		if (Flags.isStatic(iMethod.getFlags())) {
			return moveStatic(iMethod, classTo);
		} else {
			return moveInstance(iMethod, classTo);
		}
	}

	public Change moveInstance(IMethod iMethod, IType classTo) throws Exception {
		MoveInstanceMethodProcessor processor = new MoveInstanceMethodProcessor(iMethod,
				JavaPreferencesSettings.getCodeGenerationSettings(iMethod.getJavaProject()));
		Refactoring refactoring = new MoveRefactoring(processor);

		refactoring.checkInitialConditions(new NullProgressMonitor());

		processor.setMethodName(new Method(iMethod).getMoveName());
		processor.setInlineDelegator(true);
		processor.setRemoveDelegator(true);
		processor.setDeprecateDelegates(false);
		
		List<IVariableBinding> selectedTargets = selectTargets(classTo, processor);

		if (selectedTargets.size() == 0)
			throw new Exception("Invalid target!");
		
		if (selectedTargets.size() == 1) {
			return performRefactoring(processor, refactoring, selectedTargets.get(0));
		} else {
			IVariableBinding bestTarget = getBestTarget(classTo, processor, refactoring, selectedTargets);
					
			return performRefactoring(processor, refactoring, bestTarget);
		}

	}

	private IVariableBinding getBestTarget(IType classTo, MoveInstanceMethodProcessor processor, Refactoring refactoring, List<IVariableBinding> selectedTargets)
			throws IllegalArgumentException, Exception {
		IVariableBinding bestTarget = null;
		int numParameters = Integer.MAX_VALUE;
		
		for (IVariableBinding iVariableBinding : selectedTargets) {
			Change undo = performRefactoring(processor, refactoring, iVariableBinding);
			
			IMethod method = TypeHelper.getMovedMethod(classTo, processor.getMethodName());
			
			String type = (processor.needsTargetNode())? getTypeIfNotUsed(method, processor.getTargetName()) : null;
			
			Set<String> parameters = new Method(method).getMethodWithParameters(type).getParameters();
			
			if(parameters.size() < numParameters) {
				numParameters = parameters.size();
				bestTarget = iVariableBinding;
				this.typeNotUsed = type;
			}
			
			undo.perform(new NullProgressMonitor());
		}
		return bestTarget;
	}
	
	public static String getTypeIfNotUsed(IMethod method, String parameter) {
		
        class GetTypeIfUsed extends ASTVisitor {
        	
        	IBinding binding = null;
        	boolean used = false;
        	boolean mthdFound = false;
        	
        	@SuppressWarnings("unchecked")
			@Override
            public boolean visit(final MethodDeclaration node) {
            	
            	if((! mthdFound) && (method.equals(node.resolveBinding().getJavaElement()))) {
            		mthdFound = true;
            		
            		for (SingleVariableDeclaration param : (List<SingleVariableDeclaration>) node.parameters()) {
            			if(parameter.equals(param.getName().getFullyQualifiedName())) {
            				binding = param.resolveBinding();
            				break;
            			}
    				}
            		
            		if(binding != null) {
            			node.getBody().accept(new ASTVisitor() {
                        	
                        	@Override
                        	public boolean visit(SimpleName node) {
                        		
                        		if((! used) && (binding.equals(node.resolveBinding()))) {
                        			used = true;
                        		}                   		
                        		
                        		return false;
                        	}
        				}); 
            		}
                                       
            	}    
                
                return false;
            }
        }
		
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		
		parser.setSource(method.getCompilationUnit());

        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        
        GetTypeIfUsed visitor = new GetTypeIfUsed();
        
        cu.accept(visitor);
        
        return (! visitor.used)? br.com.bhansen.utils.Signature.normalizeInnerSignature(Signature.toString(((ILocalVariable) visitor.binding.getJavaElement()).getTypeSignature()).replaceAll("/", ".")) : null;
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
	
	public static Change moveStatic(IMethod iMethod, IType classTo) throws Exception {
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

	public String getTypeNotUsed() {
		return typeNotUsed;
	}

}
