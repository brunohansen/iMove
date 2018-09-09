package br.com.bhansen.metric;

public class CompositeMetric implements Metric {
	
	private Metric one;
	private Metric two;
	
	public CompositeMetric(Metric one, Metric two) throws Exception {
		this.one = one;
		this.two = two;
	}

	@Override
	public double getMetric() throws Exception {
		return one.getMetric() + two.getMetric();
	}

	@Override
	public String getName() {
		return one.getName();
	}

}
