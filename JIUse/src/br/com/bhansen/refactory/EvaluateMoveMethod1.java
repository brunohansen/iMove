package br.com.bhansen.refactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.MetricFactory;


public class EvaluateMoveMethod1 extends MoveMethodEvaluator  {
	
	private double threshold;

	private double oldFromValue;
	private double oldToValue;

	private double newFromValue;
	private double newToValue;

	private double valueDifference;
	
	private MetricFactory factory;
	
	public EvaluateMoveMethod1(IType classFrom, String method, IType classTo, MetricFactory factory, double threshold) throws Exception {
		super(classFrom, method, classTo);
		
		this.threshold = threshold;
		
		this.factory = factory;
		
		this.oldFromValue = factory.create(classFrom).getMetric();
		this.oldToValue = factory.create(classTo).getMetric();
		
		this.move(method);
	}
	
	private void move(String method) throws Exception {
		Change undo = MoveMethodRefactor.move(this.classFrom, method, this.classTo);
		
		try {
			this.newFromValue = factory.create(this.classFrom).getMetric();
			this.newToValue = factory.create(this.classTo, AbsMetric.getMoveMethodName(iMethod.getElementName()), AbsMetric.getClassName(this.classFrom)).getMetric();
			
			this.valueDifference = (this.newFromValue - this.oldFromValue) + (this.newToValue - this.oldToValue);
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}

	public boolean shouldMove() {
		return this.valueDifference > threshold;
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();

		txt.append(AbsMetric.getClassName(this.classFrom)).append(" ").append(this.oldFromValue).append(" -> ")
				.append(this.newFromValue).append("\n");
		txt.append(AbsMetric.getClassName(this.classTo)).append(" ").append(this.oldToValue).append(" -> ")
				.append(this.newToValue).append("\n");
		txt.append("Value difference: ").append(this.valueDifference).append("\n\n");

		return txt.toString();
	}

	public String toLineString() {
		return new StringBuilder().append(new BigDecimal(this.valueDifference).setScale(6, RoundingMode.HALF_EVEN) + "-").append((shouldMove()) ? "0" : "1").append("\t").append(AbsMetric.getClassName(this.classFrom)).append("::").append(method)
				.append("\t").append(AbsMetric.getClassName(this.classTo)).toString();
	}

}