package br.com.bhansen.metric;

import org.eclipse.jdt.core.IType;

public class CheckMoves implements Metric{

	@Override
	public double getMetric() throws Exception {
		return 0;
	}

	@Override
	public String getName() {
		return "Just check";
	}

	@Override
	public boolean isPublicMethod() {
		return true;
	}

	@Override
	public boolean hasNoCaller() {
		return false;
	}

	@Override
	public boolean isCalledOnlyBy(IType type) {
		return false;
	}

	@Override
	public double getMetric(boolean skipIUC) throws Exception {
		return 0;
	}

}
