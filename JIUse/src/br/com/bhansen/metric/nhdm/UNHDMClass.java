package br.com.bhansen.metric.nhdm;

import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.nhd.UNHDClass;
import br.com.bhansen.utils.Type;

public class UNHDMClass extends UNHDClass {
	
	public UNHDMClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}
	
	@Override
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHDMClass.NHDM;
	}
}
