package br.com.bhansen.metric;

import java.math.BigInteger;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

public abstract class DeclarationMetricClass extends DeclarationMetric {

	protected DeclarationMetricClass(IType type, boolean zeroParams, String method, String parameter) throws Exception {
		super(type);

		IMethod[] iMethods = type.getMethods();

		for (IMethod iMethod : iMethods) {

//			if ((Flags.isPrivate(iMethod.getFlags())) || (isFakeDelegate(iMethod, method)))
//				continue;

			Set<String> params = createParametersSet(iMethod);

//			if (isMovedMethod(iMethod, method))
//				removeFakeParameter(params, parameter);

			if (zeroParams) {
				getMethods().put(getSignature(iMethod), params);
			} else if (params.size() > 0) {
				getMethods().put(getSignature(iMethod), params);
			}

		}
	}

	protected static int comb(int n) {
		if (n == 0)
			return 0;

		if (n <= 2)
			return 1;

		return fat(n).divide(fat(2).multiply(fat(n - 2))).intValue();
	}

	protected static BigInteger fat(int n) {
		BigInteger f = BigInteger.valueOf(n);

		while (n > 1) {
			f = f.multiply(BigInteger.valueOf(n - 1));
			n--;
		}

		return f;
	}

}
