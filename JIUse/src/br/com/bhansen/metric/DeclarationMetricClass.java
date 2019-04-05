package br.com.bhansen.metric;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.config.Config;
import br.com.bhansen.jdt.Method;
import br.com.bhansen.jdt.MethodWithParameters;
import br.com.bhansen.jdt.Type;

public abstract class DeclarationMetricClass extends DeclarationMetric {

	protected DeclarationMetricClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type);

		IMethod[] iMethods = type.getIType().getMethods();

		SubMonitor subMonitor = SubMonitor.convert(monitor, iMethods.length);

		for (IMethod iMethod : iMethods) {
			subMonitor.split(1).done();

			Method m = new Method(iMethod);

			// Add only public
			// if(! m.isPublic())
			// continue;

			// Dont add private
			if (Config.isMetricTight() && m.isPrivate())
				continue;

			// Dont add constructor
			if (Config.isMetricTight() && m.isConstructor())
				continue;

			MethodWithParameters mp = m.getMethodWithParameters();

			// Dont add zero parameters
			// if(! mp.hasParameter())
			// continue;

			getMethods().put(mp.getSignature(), mp.getParameters());
		}

	}

}
