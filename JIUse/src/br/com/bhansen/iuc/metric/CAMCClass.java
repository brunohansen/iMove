package br.com.bhansen.iuc.metric;

import java.util.Set;

import org.eclipse.jdt.core.IType;

public class CAMCClass extends DeclarationMetricClass {
	
	public CAMCClass(IType type) throws Exception {
		super(type);
	}

	@Override
	public float getMetric(String fakeDelegate) throws Exception {
		super.getMetric(fakeDelegate);
		
		float camc = 0f;
		float numParams = getParams().size();
		float numMethods = getMethods().size();
		
		if((numMethods == 0) || (numParams == 0)) {
			return 0.0f;
		}
		
		for (Set<String> ps : getMethods().values()) {
			camc += ps.size() / numParams;
		}
		
		camc = camc / numMethods;
		
		return camc;
	}

}
