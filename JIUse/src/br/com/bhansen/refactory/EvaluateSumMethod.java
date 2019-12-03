package br.com.bhansen.refactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.ltk.core.refactoring.Change;

import br.com.bhansen.config.MoveMethodConfig;
import br.com.bhansen.config.UsageMetricConfig;
import br.com.bhansen.jdt.MethodWithCallers;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.CompositeMetric;
import br.com.bhansen.metric.DeclarationMetricMethod;
import br.com.bhansen.metric.Metric;
import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.metric.UsageMetricMethod;

public class EvaluateSumMethod extends MoveMethodEvaluator  {
	
	private Metric oldMetric;
	private Metric newMetric;

	private double oldValue;
	private double newValue;

	private double oldUsageMetric;
	private double oldDeclarationMetric;
	
	private double newUsageMetric;
	private double newDeclarationMetric;
	
	private double usageDifference;
	private double declarationDifference;
	
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
		
		// Doesn't Share Callers
		/* if((! skipUsage) && (MoveMethodConfig.skipIfEnviedByDestination()) && (oldMetric instanceof CompositeMetric)) {
			CompositeMetric cMetric = (CompositeMetric) oldMetric;
			UsageMetric uMetric = cMetric.getUMetric();
			
			if(m.isCalledBy(classTo) && uMetric.doesntShareCallers(m, classTo)) {
				factory.setSkipUsage(true);
				this.oldMetric = cMetric.getDMetric();
			}
		} */
		
		this.oldValue = this.oldMetric.getMetric();
		
		this.move(subMonitor.split(70));
	}
	
	private void move(IProgressMonitor monitor) throws Exception {
		MoveMethodRefactor refactor = new MoveMethodRefactor();
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		
		Change undo = refactor.move(this.classFrom, this.method, this.classTo, subMonitor.split(50));
		
		try {
			//this.newMetric = factory.create(this.classTo, this.method, monitor);
			this.newMetric = factory.create(this.classTo, this.method.getMoveName(), refactor.getTypeNotUsed(), subMonitor.split(50));
			
			calc();		
		} finally {
			undo.perform(new NullProgressMonitor());
		}
		
	}

	private void calc() throws Exception {
		this.newValue = this.newMetric.getMetric();
		
		this.valueDifference = (this.newValue - this.oldValue);
		
		if(oldMetric instanceof CompositeMetric && newMetric instanceof CompositeMetric) {
			oldUsageMetric = ((CompositeMetric) oldMetric).getUsageMetric();
			oldDeclarationMetric = ((CompositeMetric) oldMetric).getDeclarationMetric();
			
			newUsageMetric = ((CompositeMetric) newMetric).getUsageMetric();
			newDeclarationMetric = ((CompositeMetric) newMetric).getDeclarationMetric();
			
			usageDifference = (newUsageMetric - oldUsageMetric);
			declarationDifference = (newDeclarationMetric - oldDeclarationMetric);
		} else {
			oldDeclarationMetric = this.oldValue;
			newDeclarationMetric = this.newValue;
			declarationDifference = this.valueDifference;
		}
	}
		
	@Override
	public String getMessage() {
		
		if(oldMetric instanceof CompositeMetric && newMetric instanceof CompositeMetric) {
			
			if (shouldMove()) {
				if (usageDifference >= MoveMethodConfig.getThreshold() && declarationDifference >= MoveMethodConfig.getThreshold()) {
					return "Tanto a correla��o de dados quanto a de correla��o de uso suportam a movimenta��o!";
				} else if (usageDifference >= MoveMethodConfig.getThreshold() && declarationDifference < MoveMethodConfig.getThreshold()) {
					return "A correla��o de uso � alta o suficiente para suportar a movimenta��o!";
				} else if (usageDifference < MoveMethodConfig.getThreshold() && declarationDifference >= MoveMethodConfig.getThreshold()) {
					return "A correla��o de dados � alta o suficiente para suportar a movimenta��o!";
				} else if (usageDifference < MoveMethodConfig.getThreshold() && declarationDifference < MoveMethodConfig.getThreshold()) {
					return "A correla��o de uso e de dados combinadas suportam a movimenta��o!";
				}
			} else {
				if (usageDifference < MoveMethodConfig.getThreshold() && declarationDifference < MoveMethodConfig.getThreshold()) {
					return "Tanto a correla��o de dados quanto a de correla��o de uso n�o suportam a movimenta��o!";
				} else if (usageDifference < MoveMethodConfig.getThreshold() && declarationDifference >= MoveMethodConfig.getThreshold()) {
					return "A correla��o de uso � baixa o suficiente para n�o suportar a movimenta��o!";
				} else if (usageDifference >= MoveMethodConfig.getThreshold() && declarationDifference < MoveMethodConfig.getThreshold()) {
					return "A correla��o de dados � baixa o suficiente para n�o suportar a movimenta��o!";
				} else if (usageDifference >= MoveMethodConfig.getThreshold() && declarationDifference >= MoveMethodConfig.getThreshold()) {
					return "A correla��o de uso e de dados combinadas n�o suportam a movimenta��o!";
				}
			}
			
			return "Reason not identified! " + super.getMessage();
		} 
		
		return ((this.factory.skipUsage())? "Usage skipped! " : "Usage not skipped!") + super.getMessage();
	}
	
	@Override
	public String getValueText() {
		//Verificar se a assinatura diminuiu
		
		String additionals = "";
		
//		if(this.oldDeclarationMetric == 0 && this.newDeclarationMetric == 0 &&
//				this.oldUsageMetric == 0 && this.newUsageMetric == 0) {
			int oldNumParams = 0;
			int newNumParams = 0;
			
			int oldNumTypes = 0;
			int newNumTypes = 0;
			
			int oldNumCallers = 0;
			int newNumCallers = 0;
			
			int oldNumMts = 0;
			int newNumMts = 0;
			
			if(oldMetric instanceof CompositeMetric && newMetric instanceof CompositeMetric) {
				oldNumParams = ((DeclarationMetricMethod) ((CompositeMetric) oldMetric).getDMetric()).getMethod().size();
				newNumParams = ((DeclarationMetricMethod) ((CompositeMetric) newMetric).getDMetric()).getMethod().size();
				
				oldNumTypes = ((DeclarationMetricMethod) ((CompositeMetric) oldMetric).getDMetric()).getAllParams().size() + 
						((DeclarationMetricMethod) ((CompositeMetric) newMetric).getDMetric()).getMethodsParams().size();
				newNumTypes = ((DeclarationMetricMethod) ((CompositeMetric) oldMetric).getDMetric()).getMethodsParams().size() + 
						((DeclarationMetricMethod) ((CompositeMetric) newMetric).getDMetric()).getAllParams().size();
				
				oldNumCallers = ((UsageMetricMethod) ((CompositeMetric) oldMetric).getUMetric()).getAllCallers().size() + 
						((UsageMetricMethod) ((CompositeMetric) newMetric).getUMetric()).getMethodsCallers().size();
				newNumCallers = ((UsageMetricMethod) ((CompositeMetric) oldMetric).getUMetric()).getMethodsCallers().size() + 
						((UsageMetricMethod) ((CompositeMetric) newMetric).getUMetric()).getAllCallers().size();
				
				oldNumMts = ((DeclarationMetricMethod) ((CompositeMetric) oldMetric).getDMetric()).getMethods().size() + 1;
				newNumMts = ((DeclarationMetricMethod) ((CompositeMetric) newMetric).getDMetric()).getMethods().size();
			} else {
				oldNumParams = ((DeclarationMetricMethod) oldMetric).getMethod().size();
				newNumParams = ((DeclarationMetricMethod) newMetric).getMethod().size();
				
				oldNumTypes = ((DeclarationMetricMethod) oldMetric).getAllParams().size();
					((DeclarationMetricMethod) newMetric).getMethodsParams().size();
				newNumTypes = ((DeclarationMetricMethod) oldMetric).getMethodsParams().size();
					((DeclarationMetricMethod) newMetric).getAllParams().size();
				
				oldNumCallers = 0;
				newNumCallers = 0;
				
				oldNumMts = ((DeclarationMetricMethod) oldMetric).getMethods().size() + 1;
				newNumMts = ((DeclarationMetricMethod) newMetric).getMethods().size();
			}
			
			if (oldNumParams > newNumParams) {
				additionals = "-";
			} else if (oldNumParams < newNumParams) {
				additionals = "+";
			} else {
				additionals = "=";
			}
			
			if (oldNumTypes > newNumTypes) {
				additionals += "-";
			} else if (oldNumTypes < newNumTypes) {
				additionals += "+";
			} else {
				additionals += "=";
			}
			
			if (oldNumCallers > newNumCallers) {
				additionals += "-";
			} else if (oldNumCallers < newNumCallers) {
				additionals += "+";
			} else {
				additionals += "=";
			}
			
			if (oldNumMts > newNumMts) {
				additionals += "-";
			} else if (oldNumMts < newNumMts) {
				additionals += "+";
			} else {
				additionals += "=";
			}
//		}
		
		return new StringBuilder().append(additionals + "BD" + super.getValueText() + 
				":BI" + new BigDecimal(this.oldValue).setScale(6, RoundingMode.HALF_EVEN) +
				":BF" + new BigDecimal(this.newValue).setScale(6, RoundingMode.HALF_EVEN) +
				
				"\tDD" + new BigDecimal(this.declarationDifference).setScale(6, RoundingMode.HALF_EVEN) +
				":DI" + new BigDecimal(this.oldDeclarationMetric).setScale(6, RoundingMode.HALF_EVEN) +
				":DF" + new BigDecimal(this.newDeclarationMetric).setScale(6, RoundingMode.HALF_EVEN) +
				
				"\tUD" + new BigDecimal(this.usageDifference).setScale(6, RoundingMode.HALF_EVEN) + 
				":UI" + new BigDecimal(this.oldUsageMetric).setScale(6, RoundingMode.HALF_EVEN) + 
				":UF" + new BigDecimal(this.newUsageMetric).setScale(6, RoundingMode.HALF_EVEN)
				).toString();
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
		txt.append("Message: ").append(this.getMessage()).append("\n\n");

		return txt.toString();
	}

}
