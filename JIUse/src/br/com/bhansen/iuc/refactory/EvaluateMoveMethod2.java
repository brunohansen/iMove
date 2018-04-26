package br.com.bhansen.iuc.refactory;

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

import br.com.bhansen.iuc.metric.CAMCClass;
import br.com.bhansen.iuc.metric.CAMCJClass;
import br.com.bhansen.iuc.metric.CheckMoves;
import br.com.bhansen.iuc.metric.CompositeMetric;
import br.com.bhansen.iuc.metric.IUCClass;
import br.com.bhansen.iuc.metric.Metric;
import br.com.bhansen.iuc.metric.MetricClass;
import br.com.bhansen.iuc.metric.NHDMClass;
import br.com.bhansen.iuc.metric.NHDMNClass;

@SuppressWarnings("restriction")
public class EvaluateMoveMethod2 {
	
	private IType classFrom;
	private IType classTo;
	private String method;
	
	public EvaluateMoveMethod2(IType classFrom) throws Exception {
		this.setClassFrom(classFrom);
	}

	public EvaluateMoveMethod2(IType classFrom, String method, IType classTo) throws Exception {
		this(classFrom);
		this.move(method, classTo);
	}
	
	public void setClassFrom(IType classFrom) {
		this.classFrom = classFrom;
	}

	public IType getClassFrom() {
		return classFrom;
	}

	public void setClassTo(IType classTo) {
		this.classTo = classTo;
	}

	public IType getClassTo() {
		return classTo;
	}

	public void move(String method) throws Exception {

	}



	public void move(String method, IType classTo) throws Exception {

	}

	public boolean shouldMove() {
		return true;
	}

	@Override
	public String toString() {
		return "";
	}

	public String toLineString() {
		return "";
	}

}
