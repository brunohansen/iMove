package br.com.bhansen.handler;


import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectDlg extends InputDialog {
	
	private String selc;
	private String [] options;

	public SelectDlg(Shell parentShell, String dialogTitle, String dialogMessage, String [] options) {
		super(parentShell, dialogTitle, dialogMessage, "", null);
		this.options = options;
	}
	
	protected Control createDialogArea(Composite parent) {
		SelectDlg dlg = this;
		 Composite composite = (Composite) super.createDialogArea(parent);
		 Combo c = new Combo(composite, SWT.READ_ONLY);
		    c.setBounds(composite.getBounds());
		    c.setItems(options);
		    c.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					dlg.selc = c.getItem(c.getSelectionIndex());					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		 
		 return composite;
	}
	
	public String getSelection() {
		return selc;
	}

}
