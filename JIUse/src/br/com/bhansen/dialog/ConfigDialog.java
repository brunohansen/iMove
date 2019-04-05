package br.com.bhansen.dialog;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import br.com.bhansen.config.Config;
import br.com.bhansen.config.Config.MUCScope;
import br.com.bhansen.config.Config.Metric;
import br.com.bhansen.config.Config.MetricType;

public class ConfigDialog extends TitleAreaDialog {
	
	private Combo metric;
	private Combo metricType;
	
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
        
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.heightHint = 300;
        
        container.setLayoutData(gridData);
        container.setLayout(new GridLayout(1, false));
        
        Group group1 = new Group(container, SWT.SHADOW_ETCHED_IN);
        group1.setText("Metric Configuration");
        group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group1.setLayout(new GridLayout(2, false));

        metricType = createComboField(group1, "Type", MetricType.values(), Config.getMetricType().ordinal());
        
        Group group2 = new Group(container, SWT.SHADOW_ETCHED_IN);
        group2.setText("Move Method Configuration");
        group2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group2.setLayout(new GridLayout(2, false));
        
        metric = createComboField(group2, "Metric", Metric.values(), Config.getMetric().ordinal());
        threshold = createTextField(group2, "Threshold", Config.getThreshold().toString());
        mucWeight = createTextField(group2, "MUC Weight", Config.getMucWeight().toString());
        mdcWeight = createTextField(group2, "MDC Weight", Config.getMdcWeight().toString());
        
        mucScope = createComboField(group2, "MUC Scope", MUCScope.values(), Config.getMucScope().ordinal());
        
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
    
    private Combo createComboField(Composite container, String label, Enum<?> [] values, int index) {
        String [] items = new String[values.length]; 
        
        for (Enum<?> value : values) {
        	items[value.ordinal()] = value.toString();
		}
    	
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
    		Config.set(Double.parseDouble(threshold.getText()), Double.parseDouble(mucWeight.getText()), Double.parseDouble(mdcWeight.getText()), MUCScope.values()[mucScope.getSelectionIndex()], MetricType.values()[metricType.getSelectionIndex()], Metric.values()[metric.getSelectionIndex()]);
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
