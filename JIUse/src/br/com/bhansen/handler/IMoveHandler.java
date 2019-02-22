package br.com.bhansen.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
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
import br.com.bhansen.utils.Project;
import br.com.bhansen.utils.Type;
import br.com.bhansen.view.MoveMethod;

public abstract class IMoveHandler extends AbstractHandler {
	
	protected interface Runnable {
		public void run(IProgressMonitor monitor) throws Exception;
	}
	
	protected static void openProgressDialog(IWorkbenchWindow window, Runnable runnable) throws Exception {
		new ProgressMonitorDialog(window.getShell()).run(true, false, new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					runnable.run(monitor);
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		});
	}
	
	protected void showMovements(IWorkbenchWindow window, Project project, Object movements) throws PartInitException {
		MoveMethod moveMethod = (MoveMethod) window.getActivePage().showView("iMove.view.movemethod");
		moveMethod.update(project, movements);
	}
	
	@Override
	public Object execute(ExecutionEvent event) {

		try {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

			try {

				return execute(window, event, event.getParameter("iMove.type"), event.getParameter("iMove.metric"));
				
			} catch (Exception e) {
				e.printStackTrace();
				if(e.getCause() != null)
					MessageDialog.openInformation(window.getShell(), "iMove Error", e.getCause().getMessage());
				else
					MessageDialog.openInformation(window.getShell(), "iMove Error", e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();

			MessageDialog.openInformation(null, "iMove", e.getMessage());
		}

		return null;
	}

	public static MoveMethodEvaluator createEvaluator(Type classFrom, String method, Type classTo, String type, String metric, IProgressMonitor monitor) throws Exception {	
		switch (type) {
		case "class":
			return new EvaluateSumClass(classFrom, method, classTo, createFactory(type, metric), 0, monitor);
		case "method":
			return new EvaluateSumMethod(classFrom, method, classTo, createFactory(type, metric), 0, monitor);
		default:
			throw new Exception("Invalid type: " + type + "!");
		}		
	}

	public static MetricFactory createFactory(String eType, String metric) throws Exception {
		return new MetricFactory() {

			@Override
			public Metric create(Type type, String method, String parameter, boolean skipIUC, IProgressMonitor monitor) throws Exception {

				switch (eType) {
				case "class":
					switch (metric) {
					case "IUC":
						return new IUCClass(type, monitor);
					case "CAMC":
						return new CAMCClass(type, method, parameter, monitor);
					case "NHDM":
						return new NHDMClass(type, method, parameter, monitor);
					case "IUC + CAMC": 
					{
						SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
						return new CompositeMetric(new IUCClass(type, subMonitor.split(50)), new CAMCClass(type, method, parameter, subMonitor.split(50)));
					}
					case "IUC + NHDM":
					{
						SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
						return new CompositeMetric(new IUCClass(type, subMonitor.split(50)), new NHDMClass(type, method, parameter, subMonitor.split(50)));
					}
					default:
						throw new Exception("Invalid metric: " + metric + "!");
					}
				case "method":
					switch (metric) {
					case "IUC":
						return new IUCJMethod(type, method, monitor);
					case "CAMC":
						return new CAMCJMethod(type, method, parameter, monitor);
					case "IUC + CAMC":
						if(skipIUC)
							return new CAMCJMethod(type, method, parameter, monitor);
						else {
							SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
							return new CompositeMetric(new IUCJMethod(type, method, subMonitor.split(50)), new CAMCJMethod(type, method, parameter, subMonitor.split(50)));
						}
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
