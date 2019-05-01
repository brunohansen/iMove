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
    private Button removeIfNotUsed;
    private Button removeIfAttribute;
    private Button skipIfPrivate;
    private Button skipIfEnviedByDestination;
    
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
        gridData.heightHint = 550;
        
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
        
    	constructor = createCheckBox(common, "Constructor", MetricConfig.useConstructor()); 
    	accessor = createCheckBox(common, "Accessor methods (get, set and is)", MetricConfig.useAccessorMethods());
    	publ = createCheckBox(common, "Public methods", MetricConfig.usePublicMethods());
    	prot = createCheckBox(common, "Protected methods", MetricConfig.useProtectedMethods());
    	def = createCheckBox(common, "Default methods", MetricConfig.useDefaultMethods());
    	priv = createCheckBox(common, "Private methods", MetricConfig.usePrivateMethods());
    	
    }
    
    private void createDataMetricConfig(Composite container) {
        Group data = new Group(container, SWT.SHADOW_ETCHED_IN);
        data.setText("Data Metric Configuration");
        data.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        data.setLayout(new GridLayout(2, false));
    	        
    	primitives = createCheckBox(data, "Primitives", DataMetricConfig.usePrimitives());
    	arraysAndCollections = createCheckBox(data, "Arrays and Collections", DataMetricConfig.useArraysAndCollections());
    	_return = createCheckBox(data, "Return", DataMetricConfig.useReturn());
    	extractGenerics = createCheckBox(data, "Extract generics", DataMetricConfig.extractGenerics());
    	_this = createCheckBox(data, "This", DataMetricConfig.useThis());
    	parameterlessMethods = createCheckBox(data, "Parameterless methods", DataMetricConfig.useParameterlessMethods());
    }
    
    private void createUsageMetricConfig(Composite container) {
        Group usage = new Group(container, SWT.SHADOW_ETCHED_IN);
        usage.setText("Usage Metric Configuration");
        usage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        usage.setLayout(new GridLayout(2, false));
        
    	hideMethods = createCheckBox(usage, "Hide methods", UsageMetricConfig.hideMethods());
        createLabel(usage, "Usage Scope");
    	internalCalls = createCheckBox(usage, "Internal calls", UsageMetricConfig.useInternalCalls());
        usageScope = createComboField(usage, UsageMetricConfig.UsageScope.values(), UsageMetricConfig.getUsageScope().ordinal());
    }

    private void createMoveMethodConfig(Composite container) {
        Group move = new Group(container, SWT.SHADOW_ETCHED_IN);
        move.setText("Move Method Configuration");
        move.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        move.setLayout(new GridLayout(2, false));
        
        createLabel(move, "Metric");
        createLabel(move, "Threshold");
        metric = createComboField(move, MoveMethodConfig.Metric.values(), MoveMethodConfig.getMetric().ordinal());
        threshold = createTextField(move, MoveMethodConfig.getThreshold().toString());
        createLabel(move, "Data Weight");
        createLabel(move, "Usage Weight");
        mdcWeight = createTextField(move, MoveMethodConfig.getMdcWeight().toString());
        mucWeight = createTextField(move, MoveMethodConfig.getMucWeight().toString());
        
        removeIfNotUsed = createCheckBox(move, "Remove parameter if not used", MoveMethodConfig.removeIfNotUsed());
        skipIfPrivate = createCheckBox(move, "Skip usage if private", MoveMethodConfig.skipIfPrivate());
        
        removeIfAttribute = createCheckBox(move, "Remove parameter if attribute", MoveMethodConfig.removeIfAttribute());
        skipIfEnviedByDestination = createCheckBox(move, "Skip usage if envied by destination", MoveMethodConfig.skipIfEnviedByDestination());
    }
    
    // save content of the Text fields because they get disposed
    // as soon as the Dialog closes
    private void saveInput() {
    	try {
    		MetricConfig.setMetricConfig(constructor.getSelection(), accessor.getSelection(), publ.getSelection(), prot.getSelection(), def.getSelection(), priv.getSelection());
    		DataMetricConfig.setDataMetricConfig(primitives.getSelection(), arraysAndCollections.getSelection(), _return.getSelection(), extractGenerics.getSelection(), _this.getSelection(), parameterlessMethods.getSelection());
    		UsageMetricConfig.setUsageMetricConfig(hideMethods.getSelection(), internalCalls.getSelection(), UsageMetricConfig.UsageScope.values()[usageScope.getSelectionIndex()]);
    		MoveMethodConfig.setMoveMethodConfig(Double.parseDouble(threshold.getText()), Double.parseDouble(mucWeight.getText()), Double.parseDouble(mdcWeight.getText()), MoveMethodConfig.Metric.values()[metric.getSelectionIndex()], removeIfNotUsed.getSelection(), removeIfAttribute.getSelection(), skipIfPrivate.getSelection(), skipIfEnviedByDestination.getSelection());
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

	private Text createTextField(Composite container, String text) {
		GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;

        Text txt = new Text(container, SWT.BORDER);
        txt.setLayoutData(gridData);
        txt.setText(text);
        
        return txt;
	}

	private Combo createComboField(Composite container, Enum<?>[] values, int index) {
		String [] items = new String[values.length]; 
        
        for (Enum<?> value : values) {
        	items[value.ordinal()] = value.toString();
		}
    	
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;

        Combo combo = new Combo(container, SWT.BORDER);
        combo.setLayoutData(gridData);
        combo.setItems(items);
        combo.select(index);
        
        return combo;
	}
    
	private void createLabel(Composite container, String text) {
		Label lbl = new Label(container, SWT.NONE);
        lbl.setText(text);
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
