package br.com.bhansen.handler.input;

import org.eclipse.core.runtime.NullProgressMonitor;

public class GoldCheckerTest {

	public static void main(String[] args) throws Exception {
		GoldChecker.goldCheck("C:\\Users\\bruno\\git\\iMove\\Data\\gold_sets",
				"C:\\Users\\bruno\\git\\iMove\\Results\\M CAMCJ mais IUCJ\\CAMC 50 2", new NullProgressMonitor());
	}

}
