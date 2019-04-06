package br.com.bhansen.metric.nhdm;

import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.nhd.NHDClass;

public class NHDMClass extends NHDClass {
	
	public static final BiPredicate<Boolean, Boolean> NHDM = new BiPredicate<Boolean, Boolean>() {
		
		@Override
		public boolean test(Boolean v1, Boolean v2) {
			return v1 && v2;
		}
	};
	
	public NHDMClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}
	
	@Override
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHDM;
	}
	
	public static void main(String[] args) {
		//boolean[][] poMtrx = {{true, true, true, false},{false, true, true, true},{true, true, true, false}};
		boolean[][] poMtrx = {{true, false, true}, {true, true, false}, {false, true, true}, {true, false, false}, {true, false, false}};
		
		System.out.println(NHDClass.nhdClass(poMtrx, NHDM));
		
	}

}
