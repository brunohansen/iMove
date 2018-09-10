package br.com.bhansen.handler.select;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import br.com.bhansen.handler.IMoveHandler;

public abstract class SelectionHandler extends IMoveHandler {
	
	protected IType getType() throws Exception {
		ITextEditor editor = (ITextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		if(editor == null)
			throw new Exception("There's no editor openned!");
			
		IEditorInput editorInput = editor.getEditorInput();
		IJavaElement elem = JavaUI.getEditorInputJavaElement(editorInput);
		
		if (elem instanceof ICompilationUnit) {
			ICompilationUnit unit = (ICompilationUnit) elem;
			IType[] allTypes = unit.getAllTypes();
			
			if((allTypes != null) & (allTypes.length > 0)) {
				return allTypes[0];
			} 
		}
		
		throw new Exception("Select a class on editor");
	}
	
	protected IMethod getMethod() throws Exception {
		ITextEditor editor = (ITextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		if(editor == null)
			throw new Exception("There's no editor openned!");

		ITextSelection selection = (ITextSelection) editor.getSelectionProvider().getSelection();

		IEditorInput editorInput = editor.getEditorInput();
		IJavaElement elem = JavaUI.getEditorInputJavaElement(editorInput);
		
		if (elem instanceof ICompilationUnit) {
		    ICompilationUnit unit = (ICompilationUnit) elem;
		    IJavaElement selected = unit.getElementAt(selection.getOffset());
			
			if (selected.getElementType() == IJavaElement.METHOD) {
				return (IMethod) selected;
			} else {
				throw new Exception("Select a method on editor");
			}
		}
		
		throw new Exception("Select a method on editor");
	}
	

}
