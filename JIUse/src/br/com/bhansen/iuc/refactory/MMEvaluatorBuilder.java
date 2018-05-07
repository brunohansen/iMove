package br.com.bhansen.iuc.refactory;

import org.eclipse.jdt.core.IType;

import br.com.bhansen.iuc.metric.CompositeMetric;
import br.com.bhansen.iuc.metric.IUCClass;
import br.com.bhansen.iuc.metric.Metric;
import br.com.bhansen.iuc.metric.MetricFactory;

public class MMEvaluatorBuilder {
	
	private IType classFrom;
	private IType classTo;
	private String method;
	
	private MoveMethodEvaluator evaluator;
	
	public MMEvaluatorBuilder(IType classFrom) throws Exception {
		this.setClassFrom(classFrom);
	}
	
	public MMEvaluatorBuilder(IType classFrom, String method, IType classTo) throws Exception {
		this(classFrom);
		this.move(method, classTo);
	}
	
	private MoveMethodEvaluator createEvaluate() throws Exception {
		return new EvaluateMoveMethod1(this.classFrom, this.method, this.classTo, createFactory(), 0);
		//return new EvaluateMoveMethod2(this.classFrom, this.method, this.classTo, createFactory(), createFactory2());
	}
	
	private MetricFactory createFactory() throws Exception {
		return new MetricFactory() {
			
			@Override
			public Metric create(IType type, String fakeDelegate, String fakeParameter) throws Exception {
				//return new CheckMoves();
				//return new NHDMNClass(type);
				//return new IUCClass(type, fakeDelegate); 
				//return new CAMCClass(type, true, fakeDelegate, fakeParameter);
				//return new NHDMClass(type, true, fakeDelegate, fakeParameter);
				//return new CAMCJClass(type, true, fakeDelegate, fakeParameter);
				return new CompositeMetric(type, fakeDelegate, fakeParameter);
			}
		}; 
	}
	
	private MetricFactory createFactory2() throws Exception {
		return new MetricFactory() {
			
			@Override
			public Metric create(IType type, String fakeDelegate, String fakeParameter) throws Exception {
				//return new CheckMoves();
				//return new NHDMNClass(type);
				return new IUCClass(type, fakeDelegate); 
				//return new CAMCClass(type);
				//return new NHDMClass(type);
				//return new CAMCJClass(type);
				//return new CompositeMetric(type);
			}
		}; 
	}
	
	public void setClassFrom(IType classFrom) {
		this.classFrom = classFrom;
	}

	public IType getClassFrom() {
		return classFrom;
	}

	public void setClassTo(IType classTo) {
		this.classTo = classTo;
	}

	public IType getClassTo() {
		return classTo;
	}

	public void move(String method) throws Exception {
		this.method = method;
		this.evaluator = createEvaluate();
	}
	
	public void move(String method, IType classTo) throws Exception {
		this.setClassTo(classTo);
		this.move(method);
	}

	public boolean shouldMove() {
		return this.evaluator.shouldMove();
	}

	@Override
	public String toString() {
		return this.evaluator.toString();
	}

	public String toLineString() {
		return this.evaluator.toLineString();
	}

}
