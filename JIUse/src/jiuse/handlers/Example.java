package jiuse.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.callhierarchy.CallHierarchy;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

import br.com.bhansen.handler.SelectDlg;
import br.com.bhansen.iuc.metric.IUCClass;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class Example extends AbstractHandler {
	
	private static IUCClass clazz;
	private static IUCClass clazz2;
	private static String method;
	
	static {
		clazz = null;
		clazz2 = null;
		method = null;
	}

	@Override
	public Object execute(ExecutionEvent event) {

		try {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
						
			try {
				if(method != null) {
					clazz = null;
					clazz2 = null;
					method = null;
				}
				
				if(clazz == null) {
					clazz = new IUCClass(getSelection());
					
					System.out.println(clazz);
					
					MessageDialog.openInformation(window.getShell(), "Primeira classe selecionada!", clazz.getName());
					
					return null;
				}
				
				if(clazz2 == null) {
					clazz2 = new IUCClass(getSelection());
					MessageDialog.openInformation(window.getShell(), "Segunda classe selecionada!", clazz2.getName());
					
					String [] mtds = clazz.getMethods().keySet().toArray(new String[clazz.getMethods().size()]);
							
					SelectDlg dlg = new SelectDlg(window.getShell(), "Qual método deseja mover?", "Método", mtds);
					dlg.open();
					
					method = dlg.getSelection();
					
					float iuc11 = clazz.getIUC();
					float iuc21 = clazz2.getIUC();
					
					//clazz.move(method, clazz2);
					
					float iuc12 = clazz.getIUC();
					float iuc22 = clazz2.getIUC();
					
					float iucGanho = (iuc12 - iuc11) + (iuc22 - iuc21);
					
					StringBuilder txt = new StringBuilder();
					txt.append(clazz.getName()).append(" ").append(iuc11).append(" -> ").append(iuc12).append("\n");
					txt.append(clazz2.getName()).append(" ").append(iuc21).append(" -> ").append(iuc22).append("\n");
					txt.append("IUC ganho: ").append(iucGanho).append("\n\n");
										
					MessageDialog.openInformation(window.getShell(), (iucGanho > 0)? "Mova!!!!" : "Não mova!!!", txt.toString());
				}								
			} catch (Exception e) {
				e.printStackTrace();
				
				MessageDialog.openInformation(window.getShell(), "JIUse", e.getMessage());
			}	
		} catch (Exception e) {
			e.printStackTrace();
						
			MessageDialog.openInformation(null, "JIUse", e.getMessage());
		}
		
		return null;
	}
	
	public IType getSelection() throws Exception {
		ITextEditor editor = (ITextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		IEditorInput editorInput = editor.getEditorInput();
		IJavaElement elem = JavaUI.getEditorInputJavaElement(editorInput);
		if (elem instanceof ICompilationUnit) {
			ICompilationUnit unit = (ICompilationUnit) elem;
			IType[] allTypes = unit.getAllTypes();
			
			if((allTypes != null) & (allTypes.length > 0)) {
				return allTypes[0];
			} 
		}
		
		throw new Exception("There's no class openned!");
	}

	public void selection() throws JavaModelException {
		ITextEditor editor = (ITextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();

//		ITextSelection selection = (ITextSelection) editor.getSelectionProvider().getSelection();

		IEditorInput editorInput = editor.getEditorInput();
		IJavaElement elem = JavaUI.getEditorInputJavaElement(editorInput);
		if (elem instanceof ICompilationUnit) {
			ICompilationUnit unit = (ICompilationUnit) elem;
//			IJavaElement selected = unit.getElementAt(selection.getOffset());
//
//			System.out.println("selected=" + selected);
//			System.out.println("selected.class=" + selected.getClass());

			calcIUC(unit);
		}
	}
	
	public float calcIUC(ICompilationUnit unit) throws JavaModelException {
		IType[] allTypes = unit.getAllTypes();
		System.out.println();
		System.out.println("Num. of classes: " + allTypes.length);
		for (IType type : allTypes) {
			HashMap<String, Integer> lulu = new HashMap<>();
			IMethod[] methods = type.getMethods();
			System.out.println();
			System.out.println(type.getElementName() + " {");
			System.out.println();
			System.out.println("\t Num. of methods: " + methods.length);
			System.out.println();
			for (IMethod method : methods) {
				HashSet<String> callerClasses = new HashSet<>();
				HashSet<IMethod> callerMethods = getCallersOf(method);
				System.out.println("\t " + callerMethods.size() + " -> " + method.getElementName() + method.getSignature());
				for (IMethod callerMethod : callerMethods) {
					String callerClass = callerMethod.getPath().toOSString();
					callerClasses.add(callerClass);
					System.out.println("\t \t " + callerClass);
				}
				
				for (String classs : callerClasses) {
					Integer count = lulu.get(classs);
					count = (count == null)? 1 : count + 1;
					lulu.put(classs, count);
				}
			}
			System.out.println();
			System.out.println("\t Num. of caller classes: " + lulu.size());
			System.out.println();
			
			float iuc = 0.0f;
			
			for (Map.Entry<String, Integer> callerClasse : lulu.entrySet()) {
				float value = callerClasse.getValue();
				float nM = methods.length;
				float usage = (value / nM);

				System.out.printf("Usage: %.6f", usage);
				System.out.println();
				System.out.println("\t \t " + callerClasse.getKey() + ":" + callerClasse.getValue());
				iuc = iuc + usage; 
				System.out.printf("IUCl: %.6f", iuc);
				System.out.println();
			}
			
			iuc = iuc / lulu.size();
			
			System.out.println();
			System.out.printf("IUC: %.6f", iuc);
			System.out.println();
			
			System.out.println("}");
		}
		
		return 0;
	}
	


	public HashSet<IMethod> getCallersOf(IMethod m) {

		CallHierarchy callHierarchy = CallHierarchy.getDefault();

		IMember[] members = { m };

		MethodWrapper[] methodWrappers = callHierarchy.getCallerRoots(members);
		HashSet<IMethod> callers = new HashSet<IMethod>();
		for (MethodWrapper mw : methodWrappers) {
			MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
			HashSet<IMethod> temp = getIMethods(mw2);
			callers.addAll(temp);
		}

		return callers;
	}

	HashSet<IMethod> getIMethods(MethodWrapper[] methodWrappers) {
		HashSet<IMethod> c = new HashSet<IMethod>();
		for (MethodWrapper m : methodWrappers) {
			IMethod im = getIMethodFromMethodWrapper(m);
			if (im != null) {
				c.add(im);
			}
		}
		return c;
	}

	IMethod getIMethodFromMethodWrapper(MethodWrapper m) {
		try {
			IMember im = m.getMember();
			if (im.getElementType() == IJavaElement.METHOD) {
				return (IMethod) m.getMember();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object execute2(ExecutionEvent event) throws ExecutionException {
		// Get the root of the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		// Loop over all projects
		for (IProject project : projects) {
			try {
				System.out.println("Projeto");
				printProjectInfo(project);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void printProjectInfo(IProject project) throws CoreException, JavaModelException {
		System.out.println("Working in project " + project.getName());
		// check if we have a Java project
		if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			IJavaProject javaProject = JavaCore.create(project);
			printPackageInfos(javaProject);
		}
	}

	private void printPackageInfos(IJavaProject javaProject) throws JavaModelException {
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (IPackageFragment mypackage : packages) {
			// Package fragments include all packages in the
			// classpath
			// We will only look at the package from the source
			// folder
			// K_BINARY would include also included JARS, e.g.
			// rt.jar
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				System.out.println("Package " + mypackage.getElementName());
				printICompilationUnitInfo(mypackage);

			}

		}
	}

	private void printICompilationUnitInfo(IPackageFragment mypackage) throws JavaModelException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			// printCompilationUnitDetails(unit);
			printIMethods(unit);
		}
	}

	private void printIMethods(ICompilationUnit unit) throws JavaModelException {
		IType[] allTypes = unit.getAllTypes();
		for (IType type : allTypes) {
			printIMethodDetails(type);
		}
	}

	// private void printCompilationUnitDetails(ICompilationUnit unit) throws
	// JavaModelException {
	// System.out.println("Source file " + unit.getElementName());
	// Document doc = new Document(unit.getSource());
	// System.out.println("Has number of lines: " + doc.getNumberOfLines());
	// printIMethods(unit);
	// }

	private void printIMethodDetails(IType type) throws JavaModelException {
		IMethod[] methods = type.getMethods();
		for (IMethod method : methods) {

			System.out.println("Method name " + method.getElementName());
			System.out.println("Signature " + method.getSignature());
			System.out.println("Return Type " + method.getReturnType());
			System.out.println("Callers" + getCallersOf(method));
		}
	}
}
