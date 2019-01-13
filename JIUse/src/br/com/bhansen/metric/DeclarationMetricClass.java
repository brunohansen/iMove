package br.com.bhansen.metric;

import java.math.BigInteger;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.MethodWithParameters;

public abstract class DeclarationMetricClass extends DeclarationMetric {

	protected DeclarationMetricClass(IType type, String method, String parameter) throws Exception {
		super(type);

		IMethod[] iMethods = type.getMethods();

		for (IMethod iMethod : iMethods) {
			
			Method m = new Method(iMethod);

//			if ((m.isPrivate() || (m.isFakeDelegate(method)))
//				continue;

			MethodWithParameters mp = m.getMethodWithParameters(parameter);

//			if (mp.isMovedMethod(method))
//				removeFakeParameter(mp.getParameters(), parameter);
			
			// Dont add zero parameters
//			if(! mp.hasParameter())
//				continue;

			getMethods().put(mp.getSignature(), mp.getParameters());

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
