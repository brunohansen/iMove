package br.com.bhansen.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import br.com.bhansen.metric.CompositeMetric;
import br.com.bhansen.metric.Metric;
import br.com.bhansen.metric.MetricFactory;
import br.com.bhansen.metric.camc.CAMCClass;
import br.com.bhansen.metric.camc.CAMCJMethod;
import br.com.bhansen.metric.iuc.IUCClass;
import br.com.bhansen.metric.iuc.IUCJMethod;
import br.com.bhansen.metric.nhdm.NHDMClass;
import br.com.bhansen.refactory.EvaluateSumClass;
import br.com.bhansen.refactory.EvaluateSumMethod;
import br.com.bhansen.refactory.MoveMethodEvaluator;

public abstract class IMoveHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {

		try {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

			try {

				return execute(window, event, event.getParameter("iMove.type"), event.getParameter("iMove.metric"));

			} catch (Exception e) {
				e.printStackTrace();

				MessageDialog.openInformation(window.getShell(), "iMove", e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();

			MessageDialog.openInformation(null, "iMove", e.getMessage());
		}

		return null;
	}

	public static MoveMethodEvaluator createEvaluator(IType classFrom, String method, IType classTo, String type, String metric) throws Exception {		
		switch (type) {
		case "class":
			return new EvaluateSumClass(classFrom, method, classTo, createFactory(type, metric), 0);
		case "method":
			return new EvaluateSumMethod(classFrom, method, classTo, createFactory(type, metric), 0);
		default:
			throw new Exception("Invalid type: " + type + "!");
		}		
	}

	public static MetricFactory createFactory(String eType, String metric) throws Exception {
		return new MetricFactory() {

			@Override
			public Metric create(IType type, String method, String parameter, boolean skipIUC) throws Exception {

				switch (eType) {
				case "class":
					switch (metric) {
					case "IUC":
						return new IUCClass(type);
					case "CAMC":
						return new CAMCClass(type, method, parameter);
					case "NHDM":
						return new NHDMClass(type, method, parameter);
					case "IUC + CAMC":
						return new CompositeMetric(new IUCClass(type), new CAMCClass(type, method, parameter));
					case "IUC + NHDM":
						return new CompositeMetric(new IUCClass(type), new NHDMClass(type, method, parameter));
					default:
						throw new Exception("Invalid metric: " + metric + "!");
					}
				case "method":
					switch (metric) {
					case "IUC":
						return new IUCJMethod(type, method);
					case "CAMC":
						return new CAMCJMethod(type, method, parameter);
					case "IUC + CAMC":
						if(skipIUC)
							return new CAMCJMethod(type, method, parameter);
						else
							return new CompositeMetric(new IUCJMethod(type, method), new CAMCJMethod(type, method, parameter));
					default:
						throw new Exception("Invalid metric: " + metric + "!");
					}
				default:
					throw new Exception("Invalid type: " + eType + "!");
				}

			}

		};
	}

	protected abstract Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric)
			throws Exception;

}
