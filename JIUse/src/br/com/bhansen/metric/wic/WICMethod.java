package br.com.bhansen.metric.wic;

import java.util.Set;
import java.util.function.BiFunction;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.ic.ICMethod;

public class WICMethod extends ICMethod {

	public WICMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	protected BiFunction<Set<String>, Set<String>, Double> createWeight() {
		return WICClass.createWeight(getValues().size());
	}

}
