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
		
		
		ITextSelection selection = (ITextSelection) editor.getSelectionProvider().getSelection();

		IEditorInput editorInput = editor.getEditorInput();
		IJavaElement elem = JavaUI.getEditorInputJavaElement(editorInput);
		
		if (elem instanceof ICompilationUnit) {
		    ICompilationUnit unit = (ICompilationUnit) elem;
		    IJavaElement selected = unit.getElementAt(selection.getOffset());
			
		    if(selected != null) {
				if (selected.getElementType() == IJavaElement.TYPE) {
					return (IType) selected;
				} else if (selected.getElementType() == IJavaElement.METHOD) {
					return (IType) selected.getParent();
				} 
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
		    if(selected != null) {
				if (selected.getElementType() == IJavaElement.METHOD) {
					return (IMethod) selected;
				}
		    } 
		}
		
		throw new Exception("Select a method on editor");
	}
	

}
