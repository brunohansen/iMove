package br.com.bhansen.metric;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import br.com.bhansen.utils.MethodHelper;
import br.com.bhansen.utils.Signature;

public abstract class DeclarationMetric extends AbsMetric {

	public DeclarationMetric(IType type) {
		super(type);
	}

	@Override
	public boolean isPublicMethod() {
		return true;
	}

	@Override
	public boolean hasNoCaller() {
		return false;
	}

	@Override
	public boolean isCalledOnlyBy(IType type) {
		return false;
	}

	protected final Set<String> createParametersSet(IMethod iMethod, String without) throws IllegalArgumentException, JavaModelException {
		return MethodHelper.createParametersSet(iMethod, getName(), without);
	}
	
	protected final void removeFakeParameter(Set<String> params, String parameter) throws JavaModelException {

		if (parameter == null)
			return;

		String fType = Signature.normalizeInnerSignature(parameter);

		for (IField iField : getType().getFields()) {
			if (iField.toString().split(" ", 2)[0].equals(fType)) {
				params.remove(fType);
				return;
			}
		}

		fType = Signature.normalizeSignature(parameter);

		for (IField iField : getType().getFields()) {
			if (iField.toString().split(" ", 2)[0].equals(fType)) {
				params.remove(fType);
				return;
			}
		}

	}
	
	protected final Set<String> getParams() {
		Set<String> params = new HashSet<>();

		for (Set<String> ps : getMethods().values()) {
			params.addAll(ps);
		}

		return params;
	}
	
}
