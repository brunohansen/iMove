package br.com.bhansen.metric.wic;

import java.util.Set;
import java.util.function.BiFunction;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.ic.UICClass;

public class UWICClass extends UICClass {

	public UWICClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}

	@Override
	protected BiFunction<Set<String>, Set<String>, Double> createWeight() {
		return WICClass.createWeight(getValues().size());
	}

}
