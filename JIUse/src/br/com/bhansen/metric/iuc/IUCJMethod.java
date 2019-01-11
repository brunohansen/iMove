package br.com.bhansen.metric.iuc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.utils.MethodHelper;

public class IUCJMethod extends IUC {

	private Set<String> method;
	private boolean publicMethod;

	public IUCJMethod(IType type, String method) throws Exception {
		super(type);

		IMethod[] iMethods = type.getMethods();

		for (IMethod iMethod : iMethods) {

			if (MethodHelper.isMethod(iMethod, method) || MethodHelper.isMovedMethod(iMethod, method)) {
				Set<String> callers = MethodHelper.getCallerTypes(iMethod);
				
				//Remove fake public
				MethodHelper.removeCaller(callers, type);
				
				this.method = callers;
				this.publicMethod = Flags.isPublic(iMethod.getFlags());

			} else {
				
				// Dont add constructor
				if (iMethod.isConstructor())
					continue;
				
				// Dont add private
				if (Flags.isPrivate(iMethod.getFlags()))
					continue;
				
				Set<String> callers = MethodHelper.getCallerTypes(iMethod);

				// Dont add not called
				if (callers.size() == 0)
					continue;
				
				// Dont add fake public
				if(MethodHelper.isCalledOnlyBy(callers, type))
					continue;
				
				//Remove fake public
				MethodHelper.removeCaller(callers, type);
				
				if (getMethods().put(MethodHelper.getSignature(iMethod), callers) != null) {
					System.out.println("Method " + MethodHelper.getSignature(iMethod) + " colision!");
				}
			}

		}
	}

	@Override
	public boolean isPublicMethod() {
		return this.publicMethod;
	}

	@Override
	public boolean hasNoCaller() {
		return this.method.isEmpty();
	}

	@Override
	public boolean isCalledOnlyBy(IType type) {
		return MethodHelper.isCalledOnlyBy(this.method, type);
	}

	@Override
	public double getMetric() throws Exception {
		return getMetric(this.method, this.getMethods());
	}

	public static double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		double metric = 0;

		if (methods.size() == 0)
			return 0;

		for (Entry<String, Set<String>> entry : methods.entrySet()) {

			metric += AbsMetric.howMuchIntersect(method, entry.getValue());

		}

		metric = metric / methods.size();

		return metric;
	}

}
