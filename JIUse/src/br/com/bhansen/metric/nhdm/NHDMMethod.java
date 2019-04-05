package br.com.bhansen.metric.nhdm;

import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.nhd.NHDMethod;

public class NHDMMethod extends NHDMethod {
	
	public NHDMMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}
	
	@Override
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHDMClass.NHDM;
	}
}
