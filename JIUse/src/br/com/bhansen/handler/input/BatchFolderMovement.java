package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

public class BatchFolderMovement extends BatchFileMovement {
	
	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {
		
		InputDialog inDlg = new InputDialog(window.getShell(), "iMove - Inform the batch directory", "Directory address", "", null);
		inDlg.open();
						
		MessageDialog.openInformation(window.getShell(), "Result", "Result will be saved in file and shown on cosole!");
		
		goldCheckDir(inDlg.getValue(), type, metric);
		
		MessageDialog.openInformation(window.getShell(), "Finish", "Finish!");
		
		return null;
	}
	
	public void goldCheckDir(String resultDir, String type, String metric) throws Exception {
		//Path inDir = Paths.get("C:\\Users\\bruno\\git\\jiuse\\Results");
		Path inDir = Paths.get(resultDir);
		
		if(! Files.isDirectory(inDir)) {
			throw new Exception("Directory not found!" + inDir);
		}
		
		Path goldDir = Paths.get(inDir.toString(), "gold_sets");
		
		if(! Files.isDirectory(goldDir)) {
			throw new Exception("Gold directory not found!" + goldDir);
		}
		
		Files.list(goldDir).forEach(
				new Consumer<Path>() {

					@Override
					public void accept(Path goldFile) {
						try {
							goldCheck(goldFile, type, metric);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						
					}
				}
		);
	}
	
	public void goldCheck(Path goldFile, String type, String metric) throws Exception {
		Path resultsDir = Paths.get(goldFile.getParent().getParent().toString(), "results");
		
		if(! Files.isDirectory(resultsDir)) {
			throw new Exception("Results directory not found!" + resultsDir);
		}
		
		String sysName = goldFile.getFileName().toString().replace(".txt", "");
		
		Path sysDir = Paths.get(resultsDir.toString(), sysName);
		
		if(! Files.isDirectory(sysDir)) {
			throw new RuntimeException("System directory not found!" + sysDir);
		}
		
		Stream<Path> sysFiles = Files.list(sysDir).filter(new Predicate<Path>() {

			@Override
			public boolean test(Path sysFile) {
				String path = sysFile.toString(); 
				return ! path.endsWith("_metric.txt") && ! path.endsWith("_gold.txt");
			}
		});
		
		goldCheck(goldFile, sysFiles, sysDir, type, metric);
		
	}
	
	public void goldCheck(Path goldFile, Stream<Path> sysFiles, Path sysDir, String type, String metric) throws IOException {
		Set<String> outSet = new TreeSet<>();
		
		sysFiles.forEach(new Consumer<Path>() {
			public void accept(Path toolFile) {
				try {
					metricCheck(toolFile, type, metric);
					outSet.addAll(GoldChecker.goldCheck(goldFile, getMetricPath(toolFile)));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		
//		Path outPath = Paths.get(sysDir.toString(), goldFile.getFileName().toString().replace(".txt", "_both_metric_gold.txt"));
//
//		Files.write(outPath, outSet);
	}
}