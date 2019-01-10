package br.com.bhansen.handler.select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.ILocalVariable;
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
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.refactory.MoveMethodEvaluator;

public class SelectMove extends SelectionHandler {
	
	private static IType classFrom; 
	private static String method; 
	private static IType classTo;
	
	static {
		classFrom = null; 
		method = null; 
		classTo = null; 
	}

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {
		
		IMethod method =  getMethod();
		
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		
		parser.setSource(getMethod().getCompilationUnit());

        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        cu.accept(new ASTVisitor() {
        	
        	@Override
            public boolean visit(final MethodDeclaration node) {
            	
            	if(method.equals(node.resolveBinding().getJavaElement())) {
            		Map<IBinding, Integer> bindings = new HashMap<>();
            		
            		for (SingleVariableDeclaration param : (List<SingleVariableDeclaration>) node.parameters()) {
    					bindings.put(param.resolveBinding(), 0);
    				}
            		
                    node.getBody().accept(new ASTVisitor() {
                    	
                    	@Override
                    	public boolean visit(SimpleName node) {
                    		IBinding binding = node.resolveBinding();
                    		
                    		
                    		if(bindings.containsKey(binding)) {
                    			bindings.put(binding, bindings.get(binding) + 1);
                    		}
                    		
                    		return super.visit(node);
                    	}
    				});
                    bindings.forEach((k,v) -> System.out.println(Signature.getSignatureSimpleName(((ILocalVariable) k.getJavaElement()).getTypeSignature()) + ": " + v));
                    
            	}
                
            	
                
                return false;
            }

        });
		
//		if(classFrom == null) {
//			
//			try {
//				classFrom = getType();
//				method = AbsMetric.getSignature(getMethod());
//				
//				MessageDialog.openInformation(window.getShell(), "iMove - Method Selected!", method + "\n\n\n Open the 'To Class' and click on the select to move menu again!");
//				
//				return null;
//			} catch (Exception e) {
//				classFrom = null;
//				throw e;
//			}					
//
//		}
//		
//		if(classFrom != null) {
//			try {
//				
//				classTo = getType();
//				
//				MessageDialog.openInformation(window.getShell(), "iMove - Class To Selected!", AbsMetric.getClassName(classTo) + "\n\n\n The result dialog will open in a while!" );
//								
//				MoveMethodEvaluator evaluator = createEvaluator(classFrom, method, classTo, type, metric);
//												
//				MessageDialog.openInformation(window.getShell(), evaluator.shouldMove()? "Move!!!" : "Don't Move!!!", evaluator.toString());
//				
//			} finally {
//				classFrom = null;
//			}
//		}
		
		return null;
	}
	
}
