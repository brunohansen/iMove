package br.com.bhansen.metric;

import java.util.HashSet;
import java.util.Set;

import br.com.bhansen.utils.Type;

public abstract class DeclarationMetric extends AbsMetric {

	public DeclarationMetric(Type type) {
		super(type);
	}
	
//	protected final void removeFakeParameter(Set<String> params, String parameter) throws JavaModelException {
//
//		if (parameter == null)
//			return;
//
//		String fType = Signature.normalizeInnerSignature(parameter);
//
//		for (IField iField : getType().getFields()) {
//			if (iField.toString().split(" ", 2)[0].equals(fType)) {
//				params.remove(fType);
//				return;
//			}
//		}
//
//		fType = Signature.normalizeSignature(parameter);
//
//		for (IField iField : getType().getFields()) {
//			if (iField.toString().split(" ", 2)[0].equals(fType)) {
//				params.remove(fType);
//				return;
//			}
//		}
//
//	}
	
	protected final Set<String> getParams() {
		Set<String> params = new HashSet<>();

		for (Set<String> ps : getMethods().values()) {
			params.addAll(ps);
		}

		return params;
	}
	
}
