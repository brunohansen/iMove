package br.com.bhansen.metric.iuc;

import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.utils.MethodHelper;
import br.com.bhansen.utils.TypeHelper;

public class IUCJMethod extends IUC {
	
	private final static boolean ADD_NOT_CALLED = true;

	private Set<String> method;
	private boolean publicMethod;

	public IUCJMethod(IType type, String method) throws Exception {
		super(type);

		IMethod[] iMethods = type.getMethods();

		if (MethodHelper.isMovedMethod(method)) {
			IMethod movedMethod = null;

			for (IMethod iMethod : iMethods) {

				if (MethodHelper.isMovedMethod(iMethod, method)) {
					movedMethod = iMethod;
					this.publicMethod = Flags.isPublic(iMethod.getFlags());
				} else {
					Set<String> callers = MethodHelper.getCallerTypes(iMethod);

					if ((ADD_NOT_CALLED) || (callers.size() > 0)) {
						if (getMethods().put(MethodHelper.getSignature(iMethod), callers) != null) {
							System.out.println("Method " + MethodHelper.getSignature(iMethod) + " colision!");
						}
					}
				}

			}

			// String original = getOriginalMethod(movedMethod);
			//
			// //Does not make sense compare itself
			// if(original != null) {
			// this.method = getMethods().remove(original);
			// } else {
			this.method = MethodHelper.getCallerTypes(movedMethod);
			// }

		} else {
			for (IMethod iMethod : iMethods) {

				if (MethodHelper.isMethod(iMethod, method)) {
					this.method = MethodHelper.getCallerTypes(iMethod);
					this.publicMethod = Flags.isPublic(iMethod.getFlags());
				} else {
					Set<String> callers = MethodHelper.getCallerTypes(iMethod);

					if ((ADD_NOT_CALLED) || (callers.size() > 0)) {
						if (getMethods().put(MethodHelper.getSignature(iMethod), callers) != null) {
							System.out.println("Method " + MethodHelper.getSignature(iMethod) + " colision!");
						}
					}
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
		return (!this.method.isEmpty()) && (this.method.size() == 1) && (this.method.contains(TypeHelper.getClassName(type)));
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
