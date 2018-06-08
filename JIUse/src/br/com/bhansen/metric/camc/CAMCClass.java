package br.com.bhansen.metric.camc;

import java.util.Set;

import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.DeclarationMetricClass;

public class CAMCClass extends DeclarationMetricClass {
	
	public CAMCClass() throws Exception {
		super();
	}
	
	public CAMCClass(IType type, boolean zeroParams, String method, String fakeParameter) throws Exception {
		super(type, zeroParams, method, fakeParameter);
	}

	@Override
	public double getMetric() throws Exception {
		double camc = 0;
		double numParams = getParams().size();
		double numMethods = getMethods().size();
		
		if((numMethods == 0) || (numParams == 0)) {
			return 0.0f;
		}
		
		for (Set<String> ps : getMethods().values()) {
			camc += ps.size() / numParams;
		}
		
		camc = camc / numMethods;
		
		return camc;
	}
	
	public static void main(String[] args) throws Exception {
		CAMCClass cj = new CAMCClass();
		
		System.out.println("CAMC Resultado: " + cj.getMetric());
	}

}
