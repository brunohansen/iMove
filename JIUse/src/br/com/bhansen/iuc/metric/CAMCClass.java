package br.com.bhansen.iuc.metric;

import java.util.Set;

import org.eclipse.jdt.core.IType;

public class CAMCClass extends DeclarationMetricClass {
	
	public CAMCClass() throws Exception {
		super();
	}
	
	public CAMCClass(IType type) throws Exception {
		super(type);
	}

	@Override
	public double getMetric(String fakeDelegate, String fakeParameter) throws Exception {
		super.getMetric(fakeDelegate, fakeParameter);
		
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
