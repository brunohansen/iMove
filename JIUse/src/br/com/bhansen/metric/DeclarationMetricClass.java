package br.com.bhansen.metric;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.config.DataMetricConfig;
import br.com.bhansen.config.MetricConfig;
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

			if(! MetricConfig.use(m))
				continue;

			MethodWithParameters mp = m.getMethodWithParameters();

			if(! DataMetricConfig.use(mp))
				continue;

			getMethods().put(mp.getSignature(), mp.getParameters());
		}

	}

}
