package br.com.bhansen.handler.select;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import br.com.bhansen.handler.JIUseHandler;

public abstract class SelectionHandler extends JIUseHandler {
	
	protected IType getSelection() throws Exception {
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

}
