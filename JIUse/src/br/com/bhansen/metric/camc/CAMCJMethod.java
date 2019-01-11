package br.com.bhansen.metric.camc;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.DeclarationMetric;
import br.com.bhansen.utils.MethodHelper;

public class CAMCJMethod extends DeclarationMetric {

	Set<String> method;
	private boolean publicMethod;

	public CAMCJMethod(IType type, String method, String parameter) throws Exception {
		super(type);

		IMethod[] iMethods = type.getMethods();

		for (IMethod iMethod : iMethods) {

			if (MethodHelper.isMethod(iMethod, method) || MethodHelper.isMovedMethod(iMethod, method)) {
				this.method = createParametersSet(iMethod);
				this.publicMethod = Flags.isPublic(iMethod.getFlags());
			} else {
				
				// Dont add constructor
//				if (iMethod.isConstructor())
//					continue;
				
				// Dont add private
//				if (Flags.isPrivate(iMethod.getFlags()))
//					continue;
				
				Set<String> params = createParametersSet(iMethod);
				
				// Dont add zero parameters
//				if(params.size() == 0)
//					continue;

				getMethods().put(MethodHelper.getSignature(iMethod), params);
			}

		}
	}

	@Override
	public boolean isPublicMethod() {
		return this.publicMethod;
	}

	@Override
	public double getMetric() throws Exception {
		double metric = 0;

		if (getMethods().size() == 0)
			return 0;

		if (method.size() == 0)
			return 1;

		for (Entry<String, Set<String>> entry : getMethods().entrySet()) {

			metric += AbsMetric.howMuchIntersect(method, entry.getValue());

		}

		metric = metric / getMethods().size();

		return metric;
	}

}
