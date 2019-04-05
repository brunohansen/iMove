package br.com.bhansen.metric.wic;

import java.util.Set;
import java.util.function.BiFunction;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.ic.UICMethod;

public class UWICMethod extends UICMethod {

	public UWICMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}
	
	@Override
	protected BiFunction<Set<String>, Set<String>, Double> createWeight() {
		return WICClass.createWeight(getValues().size());
	}

}
