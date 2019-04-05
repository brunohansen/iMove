package br.com.bhansen.metric.nhdm;

import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.nhd.UNHDMethod;
import br.com.bhansen.utils.Type;

public class UNHDMMethod extends UNHDMethod {
	
	public UNHDMMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}

	@Override
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHDMClass.NHDM;
	}
}
