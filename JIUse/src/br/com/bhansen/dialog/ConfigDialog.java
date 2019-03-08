package br.com.bhansen.dialog;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import br.com.bhansen.config.Config;
import br.com.bhansen.config.Config.MUCScope;

public class ConfigDialog extends TitleAreaDialog {

    private Text threshold;
    
    private Text mucWeight;
    private Text mdcWeight;
    
    private Combo mucScope;
    
    public static void openDlg() {
    	new ConfigDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()).open();
    }

    public ConfigDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    public void create() {
        super.create();
        setTitle("iMove preferences");
        setMessage("You must use decimal values!", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        threshold = createTextField(container, "Threshold", Config.getThreshold().toString());
        mucWeight = createTextField(container, "MUC Weight", Config.getMucWeight().toString());
        mdcWeight = createTextField(container, "MDC Weight", Config.getMdcWeight().toString());
        
        String [] scopes = new String[MUCScope.values().length]; 
        
        for (MUCScope scope : MUCScope.values()) {
			scopes[scope.ordinal()] = scope.toString();
		}
        
        mucScope = createComboField(container, "MUC Scope", scopes, Config.getMucScope().ordinal());

        return area;
    }

    private Text createTextField(Composite container, String label, String text) {
        Label lbl = new Label(container, SWT.NONE);
        lbl.setText(label);

        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;

        Text txt = new Text(container, SWT.BORDER);
        txt.setLayoutData(gridData);
        txt.setText(text);
        
        return txt;
    }
    
    private Combo createComboField(Composite container, String label, String [] items, int index) {
        Label lbl = new Label(container, SWT.NONE);
        lbl.setText(label);

        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;

        Combo combo = new Combo(container, SWT.BORDER);
        combo.setLayoutData(gridData);       
        combo.setItems(items);
        combo.select(index);
        
        return combo;
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    // save content of the Text fields because they get disposed
    // as soon as the Dialog closes
    private void saveInput() {
    	try {
    		Config.set(Double.parseDouble(threshold.getText()), Double.parseDouble(mucWeight.getText()), Double.parseDouble(mdcWeight.getText()), MUCScope.values()[mucScope.getSelectionIndex()]);
		} catch (Exception e) {
			ErrorDialog.open(e);
		}
        
    }

    @Override
    protected void okPressed() {
        saveInput();
        super.okPressed();
    }
}
