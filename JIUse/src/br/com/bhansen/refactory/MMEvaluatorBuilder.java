package br.com.bhansen.refactory;

import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.CompositeMetric;
import br.com.bhansen.metric.Metric;
import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.metric.iuc.IUCClass;
import br.com.bhansen.metric.iuc.IUCMethod;

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
		//return new EvaluateMoveMethod1(this.classFrom, this.method, this.classTo, createFactory(), 0);
		//return new EvaluateMoveMethod2(this.classFrom, this.method, this.classTo, createFactory(), createFactory2());
		return new EvaluateMoveMethod3(classFrom, method, classTo, createFactory(), 0);
	}
	
	private MetricFactory createFactory() throws Exception {
		return new MetricFactory() {
			
			@Override
			public Metric create(IType type, String method, String parameter) throws Exception {
				//return new CheckMoves();
				//return new NHDMNClass(type, true, method, parameter);
				//return new IUCClass(type, method); 
				//return new CAMCClass(type, true, method, parameter);
				//return new NHDMClass(type, true, method, parameter);
				//return new CAMCJClass(type, true, method, parameter);
				//return new CompositeMetric(type, method, parameter);
				
				return new IUCMethod(type, method);
			}
		}; 
	}
	
	private MetricFactory createFactory2() throws Exception {
		return new MetricFactory() {
			
			@Override
			public Metric create(IType type, String movedMethod, String parameter) throws Exception {
				//return new CheckMoves();
				//return new NHDMNClass(type);
				return new IUCClass(type, movedMethod); 
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
