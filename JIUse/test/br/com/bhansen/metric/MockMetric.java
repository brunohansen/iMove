package br.com.bhansen.metric;

public class MockMetric extends DeclarationMetric {
	
	public static final MockMetric i = new MockMetric();

	private MockMetric() {
		super(null);
	}

	@Override
	public double getMetric() throws Exception {
		return 0;
	}
	
}
