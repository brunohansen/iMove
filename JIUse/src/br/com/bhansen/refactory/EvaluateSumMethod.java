package br.com.bhansen.refactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.ltk.core.refactoring.Change;

import br.com.bhansen.config.MoveMethodConfig;
import br.com.bhansen.config.UsageMetricConfig;
import br.com.bhansen.jdt.MethodWithCallers;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.CompositeMetric;
import br.com.bhansen.metric.Metric;
import br.com.bhansen.metric.MetricFactory;

public class EvaluateSumMethod extends MoveMethodEvaluator  {
	
	private Metric oldMetric;
	private Metric newMetric;

	private double oldValue;
	private double newValue;
	
	public EvaluateSumMethod(Type classFrom, String method, Type classTo, MetricFactory factory, IProgressMonitor monitor) throws Exception {
		super(classFrom, method, classTo, factory);
		
		boolean skipUsage = false;
		
		MethodWithCallers m = this.method.getMethodWithCallers();
		
		if((! m.hasCaller()) 
				|| (MoveMethodConfig.skipIfPrivate() && m.isPrivate()) 
				|| (MoveMethodConfig.skipIfPrivate() && UsageMetricConfig.hideMethods() && m.isCalledOnlyBy(classFrom)) 
				|| (MoveMethodConfig.skipIfEnviedByDestination() && m.isCalledOnlyBy(classTo))) {
			skipUsage = true;
		} else {
			skipUsage = false;
		}
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		this.oldMetric = factory.create(classFrom, method, skipUsage, subMonitor.split(30));
		this.oldValue = this.oldMetric.getMetric();
		
		this.move(subMonitor.split(70));
	}
	
	private void move(IProgressMonitor monitor) throws Exception {
		MoveMethodRefactor refactor = new MoveMethodRefactor();
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		Change undo = refactor.move(this.classFrom, this.method, this.classTo, subMonitor.split(50));
		
		try {
			this.newMetric = factory.create(this.classTo, this.method.getMoveName(), refactor.getTypeNotUsed(), subMonitor.split(50));
			this.newValue = this.newMetric.getMetric();
			
			this.valueDifference = (this.newValue - this.oldValue);
			
//			if(this.factory.skipUsage())
//				this.valueDifference += 0.1;
			
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}
	
	@Override
	public String getMessage() throws Exception {
		
		if(oldMetric instanceof CompositeMetric && newMetric instanceof CompositeMetric) {
			double oldUsageMetric = ((CompositeMetric) oldMetric).getUsageMetric();
			double oldDeclarationMetric = ((CompositeMetric) oldMetric).getDeclarationMetric();
			
			double newUsageMetric = ((CompositeMetric) newMetric).getUsageMetric();
			double newDeclarationMetric = ((CompositeMetric) newMetric).getDeclarationMetric();
			
			double usageDifference = (newUsageMetric - oldUsageMetric);
			double declarationDifference = (newDeclarationMetric - oldDeclarationMetric);
			
			if (shouldMove()) {
				if (usageDifference >= MoveMethodConfig.getThreshold() && declarationDifference >= MoveMethodConfig.getThreshold()) {
					return "Tanto a correlação de dados quanto a de correlação de uso suportam a movimentação!";
				} else if (usageDifference >= MoveMethodConfig.getThreshold() && declarationDifference < MoveMethodConfig.getThreshold()) {
					return "A correlação de uso é alta o suficiente para suportar a movimentação!";
				} else if (usageDifference < MoveMethodConfig.getThreshold() && declarationDifference >= MoveMethodConfig.getThreshold()) {
					return "A correlação de dados é alta o suficiente para suportar a movimentação!";
				} else if (usageDifference < MoveMethodConfig.getThreshold() && declarationDifference < MoveMethodConfig.getThreshold()) {
					return "A correlação de uso e de dados combinadas suportam a movimentação!";
				}
			} else {
				if (usageDifference < MoveMethodConfig.getThreshold() && declarationDifference < MoveMethodConfig.getThreshold()) {
					return "Tanto a correlação de dados quanto a de correlação de uso não suportam a movimentação!";
				} else if (usageDifference < MoveMethodConfig.getThreshold() && declarationDifference >= MoveMethodConfig.getThreshold()) {
					return "A correlação de uso é baixa o suficiente para não suportar a movimentação!";
				} else if (usageDifference >= MoveMethodConfig.getThreshold() && declarationDifference < MoveMethodConfig.getThreshold()) {
					return "A correlação de dados é baixa o suficiente para não suportar a movimentação!";
				} else if (usageDifference >= MoveMethodConfig.getThreshold() && declarationDifference >= MoveMethodConfig.getThreshold()) {
					return "A correlação de uso e de dados combinadas não suportam a movimentação!";
				}
			}
			
			return "Reason not identified! " + super.getMessage();
		} 
		
		return ((this.factory.skipUsage())? "Usage skipped! " : "Usage not skipped!") + super.getMessage();
	}

	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();
		
		txt.append(shouldMove()? " Move!" : "Don't Move!");
		txt.append("\n");
		txt.append(this.classFrom.getName()).append(" ").append(this.oldValue).append("\n");
		txt.append(this.classTo.getName()).append(" ").append(this.newValue).append("\n");
		txt.append("Skip Usage: ").append(this.factory.skipUsage()).append("\n");
		txt.append("Value difference: ").append(this.valueDifference).append("\n\n");
		txt.append("Original Metric: ").append(this.oldMetric.toString()).append("\n");
		txt.append("Destination Metric: ").append(this.newMetric.toString()).append("\n\n");

		return txt.toString();
	}

}
