package br.com.bhansen.metric.nhdm;

import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.nhd.NHDClass;

public class NHDMClass extends NHDClass {
	
	public NHDMClass(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	public static final BiPredicate<Boolean, Boolean> NHDM = new BiPredicate<Boolean, Boolean>() {
		
		@Override
		public boolean test(Boolean v1, Boolean v2) {
			return v1 && v2;
		}
	};
	
	@Override
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHDM;
	}
}
