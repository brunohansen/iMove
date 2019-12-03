package br.com.bhansen.handler.input;

import java.nio.file.Path;
import java.util.List;

import br.com.bhansen.dialog.DirectoryDialog;
import br.com.bhansen.utils.FileFinder;

public class FindThreshold {
	//000 right lower correct DI e DF
	
	
	//0x1 menor DI e DF lowest wrong DI allowed
	
	
	//100 maior DI e DF
	
	
	//1x1 maior DI e DF
	
	
	
	public static void main(String[] args) throws Exception {
		List<Path> paths = FileFinder.find(DirectoryDialog.open("Inform the batch folder", "Folder address"), "*.txt");
		
		paths.forEach(path -> {
			
		});
	}

}
