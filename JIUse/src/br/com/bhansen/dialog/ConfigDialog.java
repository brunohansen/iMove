package br.com.bhansen.dialog;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import br.com.bhansen.config.DataMetricConfig;
import br.com.bhansen.config.MetricConfig;
import br.com.bhansen.config.MoveMethodConfig;
import br.com.bhansen.config.UsageMetricConfig;

public class ConfigDialog extends TitleAreaDialog {
	
	private Button constructor; 
	private Button accessor; 
	private Button publ; 
	private Button prot; 
	private Button def; 
	private Button priv;
	
	private Button primitives;
	private Button arraysAndCollections;
	private Button _return;
	private Button extractGenerics;
	private Button _this;
	private Button parameterlessMethods;
	
	private Button hideMethods;
	private Button internalCalls;
	private Combo usageScope;
	
	private Combo metric;
    private Text threshold;
    private Text mucWeight;
    private Text mdcWeight;
    
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
        
        createMetricConfig(container);
        createDataMetricConfig(container);
        createUsageMetricConfig(container);
        createMoveMethodConfig(container);
        
        return area;
    }
    
    private void createMetricConfig(Composite container) {
        Group common = new Group(container, SWT.SHADOW_ETCHED_IN);
        common.setText("Metric Configuration");
        common.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        common.setLayout(new GridLayout(2, false));
        
    	constructor = createCheckBox(common, "Constructor", false); 
    	accessor = createCheckBox(common, "Accessor methods (get, set and is)", true);
    	publ = createCheckBox(common, "Public methods", true);
    	prot = createCheckBox(common, "Protected methods", true);
    	def = createCheckBox(common, "Default methods", true);
    	priv = createCheckBox(common, "Private methods", false);
    }
    
    private void createDataMetricConfig(Composite container) {
        Group data = new Group(container, SWT.SHADOW_ETCHED_IN);
        data.setText("Data Configuration");
        data.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        data.setLayout(new GridLayout(2, false));
        
    	primitives = createCheckBox(data, "Primitives", false);
    	arraysAndCollections = createCheckBox(data, "Arrays and Collections", true);
    	_return = createCheckBox(data, "Return", true);
    	extractGenerics = createCheckBox(data, "Extract generics", true);
    	_this = createCheckBox(data, "This", false);
    	parameterlessMethods = createCheckBox(data, "Parameterless methods", true);
    }
    
    private void createUsageMetricConfig(Composite container) {
        Group usage = new Group(container, SWT.SHADOW_ETCHED_IN);
        usage.setText("Usage Configuration");
        usage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        usage.setLayout(new GridLayout(2, false));

    	hideMethods = createCheckBox(usage, "Hide methods", true);
    	internalCalls = createCheckBox(usage, "Internal calls", true);
        usageScope = createComboField(usage, "Usage Scope", UsageMetricConfig.UsageScope.values(), UsageMetricConfig.getUsageScope().ordinal());
    }
    
    private void createMoveMethodConfig(Composite container) {
        Group move = new Group(container, SWT.SHADOW_ETCHED_IN);
        move.setText("Move Method Configuration");
        move.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        move.setLayout(new GridLayout(2, false));
        
        metric = createComboField(move, "Metric", MoveMethodConfig.Metric.values(), MoveMethodConfig.getMetric().ordinal());
        threshold = createTextField(move, "Threshold", MoveMethodConfig.getThreshold().toString());
        mucWeight = createTextField(move, "MUC Weight", MoveMethodConfig.getMucWeight().toString());
        mdcWeight = createTextField(move, "MDC Weight", MoveMethodConfig.getMdcWeight().toString());
    }
    
    // save content of the Text fields because they get disposed
    // as soon as the Dialog closes
    private void saveInput() {
    	try {
    		MetricConfig.setMetricConfig(constructor.getSelection(), accessor.getSelection(), publ.getSelection(), prot.getSelection(), def.getSelection(), priv.getSelection());
    		DataMetricConfig.setDataMetricConfig(primitives.getSelection(), arraysAndCollections.getSelection(), _return.getSelection(), extractGenerics.getSelection(), _this.getSelection(), parameterlessMethods.getSelection());
    		UsageMetricConfig.setUsageMetricConfig(hideMethods.getSelection(), internalCalls.getSelection(), UsageMetricConfig.UsageScope.values()[usageScope.getSelectionIndex()]);
    		MoveMethodConfig.set(Double.parseDouble(threshold.getText()), Double.parseDouble(mucWeight.getText()), Double.parseDouble(mdcWeight.getText()), MoveMethodConfig.Metric.values()[metric.getSelectionIndex()]);
		} catch (Exception e) {
			ErrorDialog.open(e);
		}
    }
    
    private Button createCheckBox(Composite container, String label, boolean checked) {
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        
        Button checkBox = new Button(container, SWT.CHECK);
        checkBox.setText(label);
        checkBox.setSelection(checked);
        checkBox.setLayoutData(gridData);
        
        return checkBox;
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

    @Override
    protected void okPressed() {
        saveInput();
        super.okPressed();
    }
}
