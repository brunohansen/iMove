package br.com.bhansen.metric.nhdm;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.utils.OccMtrx;
import br.com.bhansen.utils.Type;

public class NHDMClass extends DeclarationMetricClass {
	
	public NHDMClass(Type type, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, parameter, monitor);
	}
	
	@Override
	public double getMetric() throws Exception {
		
		String params [] = getParams().toArray(new String[0]);
		
		if((getMethods().size() == 1) || (params.length == 0))
			return 0f;
		
		boolean[][] poMtrx = OccMtrx.createOccMtrx(getMethods(), params);
						
		double metric = 0;
		
		for (int p = 0; p < params.length; p++) {
			double ones = 0;
			double zeros = 0;
			
			for (int m = 0; m < getMethods().size(); m++) {
				if(poMtrx[m][p]) {
					ones++;
				} else {
					zeros++;
				}
			}
			
			metric += ones * (getMethods().size() - ones) + 0.5 * zeros * (zeros - 1);
		}
		
		double y1 = (2f / (params.length * getMethods().size() * (getMethods().size() - 1)));
		return 1 - y1 * metric;
	}

}
