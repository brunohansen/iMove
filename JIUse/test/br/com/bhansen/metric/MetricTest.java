package br.com.bhansen.metric;

import java.util.Map;
import java.util.Set;

import br.com.bhansen.metric.camc.CAMCClass;
import br.com.bhansen.metric.camc.CAMCMethod;
import br.com.bhansen.metric.cci.CCiClass;
import br.com.bhansen.metric.cci.CCiMethod;
import br.com.bhansen.metric.d3c2i.D3C2iClass;
import br.com.bhansen.metric.ic.ICClass;
import br.com.bhansen.metric.ic.ICClass.MMWeight;
import br.com.bhansen.metric.ic.ICClass.PPWeight;
import br.com.bhansen.metric.ic.ICMethod;
import br.com.bhansen.metric.iscomi.ISCOMiClass;
import br.com.bhansen.metric.iscomi.ISCOMiMethod;
import br.com.bhansen.metric.nhd.NHDClass;
import br.com.bhansen.metric.nhd.NHDMethod;
import br.com.bhansen.metric.nhdm.NHDMClass;
import br.com.bhansen.metric.nhdm.NHDMMethod;
import br.com.bhansen.metric.wic.WICClass;
import br.com.bhansen.metric.wic.WICMethod;
import br.com.bhansen.test.TestCase;

public abstract class MetricTest extends TestCase {
		
	protected void test(Map<String, Set<String>> methods, Double expected, TestPredicateFactory<Double> factory) {
		checkThat("CAMC", expected, CAMCClass.camcClass(methods), factory);
		checkThat("CCi", expected, CCiClass.cciClass(methods), factory);
		checkThat("D3C2i", expected, D3C2iClass.d3c2iClass(methods), factory);
		checkThat("IC", expected, ICClass.icClass(methods, new MMWeight(){}, new PPWeight(){}), factory);
		checkThat("ISCOMi", expected, ISCOMiClass.iscomClass(methods), factory);
		checkThat("NHD", expected, NHDClass.nhdClass(methods, NHDClass.NHD), factory);
		checkThat("NHDM", expected, NHDMClass.nhdClass(methods, NHDMClass.NHDM), factory);
		checkThat("WIC", expected, WICClass.icClass(methods, WICClass.createMMWeight(methods), WICClass.createPPWeight(methods)), factory);
	}
	
	protected void test(Map<String, Set<String>> methods1, Map<String, Set<String>> methods2, TestPredicateFactory<Double> factory) {
		checkThat("CAMC", CAMCClass.camcClass(methods1), CAMCClass.camcClass(methods2), factory);
		checkThat("CCi", CCiClass.cciClass(methods1), CCiClass.cciClass(methods2), factory);
		checkThat("D3C2i", D3C2iClass.d3c2iClass(methods1), D3C2iClass.d3c2iClass(methods2), factory);
		checkThat("IC", ICClass.icClass(methods1, new MMWeight(){}, new PPWeight(){}), ICClass.icClass(methods2, new MMWeight(){}, new PPWeight(){}), factory);
		checkThat("ISCOMi", ISCOMiClass.iscomClass(methods1), ISCOMiClass.iscomClass(methods2), factory);
		checkThat("NHD", NHDClass.nhdClass(methods1, NHDClass.NHD), NHDClass.nhdClass(methods2, NHDClass.NHD), factory);
		checkThat("NHDM", NHDMClass.nhdClass(methods1, NHDMClass.NHDM), NHDMClass.nhdClass(methods2, NHDMClass.NHDM), factory);
		checkThat("WIC", WICClass.icClass(methods1, WICClass.createMMWeight(methods1), WICClass.createPPWeight(methods1)), WICClass.icClass(methods2, WICClass.createMMWeight(methods2), WICClass.createPPWeight(methods2)), factory);
	}
	
	protected void print(Map<String, Set<String>> methods) {
		System.out.println("CAMC -> " + CAMCClass.camcClass(methods));
		System.out.println("CCi -> " + CCiClass.cciClass(methods));
		System.out.println("D3C2i -> " + D3C2iClass.d3c2iClass(methods));
		System.out.println("IC -> " + ICClass.icClass(methods, new MMWeight(){}, new PPWeight(){}));
		System.out.println("ISCOMi -> " + ISCOMiClass.iscomClass(methods));
		System.out.println("NHD -> " + NHDClass.nhdClass(methods, NHDClass.NHD));
		System.out.println("NHDM -> " + NHDMClass.nhdClass(methods, NHDMClass.NHDM));
		System.out.println("WIC -> " + WICClass.icClass(methods, WICClass.createMMWeight(methods), WICClass.createPPWeight(methods)));
	}
	
	protected void print(Set<String> method, Map<String, Set<String>> methods) {
		System.out.println("CAMC -> " + CAMCMethod.camcMethod(method, methods));
		System.out.println("CCi -> " + CCiMethod.cciMethod(method, methods));
		//System.out.println("D3C2i -> " + D3C2iClass.getMetric(methods));
		System.out.println("IC -> " + ICMethod.icMethod(method, methods, new MMWeight(){}, new PPWeight(){}));
		System.out.println("ISCOMi -> " + ISCOMiMethod.iscomMethod(method, methods));
		System.out.println("NHD -> " + NHDMethod.nhdMethod(method, methods, NHDClass.NHD));
		System.out.println("NHDM -> " + NHDMMethod.nhdMethod(method, methods, NHDMClass.NHDM));
		System.out.println("WIC -> " + WICMethod.icMethod(method, methods, WICMethod.createMMWeight(method, methods), WICMethod.createPPWeight(method, methods)));
	}
	
}
