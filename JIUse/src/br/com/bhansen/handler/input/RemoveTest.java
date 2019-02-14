package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Stream;

import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.Project;
import br.com.bhansen.utils.Type;

public class RemoveTest {
	
	private static Project project = null;

	private static Project projLarge = null;
	private static Project projSmall = null;

	private final static Set<String> srcPaths;

	static {
		srcPaths = new HashSet<>();

		srcPaths.add("/ant-1.8.2-large/src/main");
		srcPaths.add("/ant-1.8.2-small/src/main");

		srcPaths.add("/drjava-stable-20100913-r5387-large/src/src");
		srcPaths.add("/drjava-stable-20100913-r5387-small/src/src");

		srcPaths.add("/geplanes-3.0.5/src");

		srcPaths.add("/jedit-3.0/src");

		srcPaths.add("/jfreechart-0.9.6/src");

		srcPaths.add("/jfreechart-1.0.13-large/experimental");
		srcPaths.add("/jfreechart-1.0.13-large/source");
		srcPaths.add("/jfreechart-1.0.13-large/swt");
		srcPaths.add("/jfreechart-1.0.13-small/experimental");
		srcPaths.add("/jfreechart-1.0.13-small/source");
		srcPaths.add("/jfreechart-1.0.13-small/swt");

		srcPaths.add("/jgroups-2.10.0-large/src");
		srcPaths.add("/jgroups-2.10.0-small/src");

		srcPaths.add("/jtopen-7.8-large/src");
		srcPaths.add("/jtopen-7.8-small/src");

		srcPaths.add("/junit-4.10-large/junit");
		srcPaths.add("/junit-4.10-large/org/junit");
		srcPaths.add("/junit-4.10-small/junit");
		srcPaths.add("/junit-4.10-small/org/junit");

		srcPaths.add("/mvnforum-1.2.2-ga-large");
		srcPaths.add("/mvnforum-1.2.2-ga-small");

		srcPaths.add("/tapestry-5.1.0.5-large/tapestry-annotations/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-large/tapestry-component-report/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-large/tapestry-core/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-large/tapestry-hibernate-core/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-large/tapestry-hibernate/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-large/tapestry-ioc/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-large/tapestry-spring/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-large/tapestry-tutorial1/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-large/tapestry-upload/src/main/java");

		srcPaths.add("/tapestry-5.1.0.5-small/tapestry-annotations/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-small/tapestry-component-report/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-small/tapestry-core/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-small/tapestry-hibernate-core/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-small/tapestry-hibernate/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-small/tapestry-ioc/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-small/tapestry-spring/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-small/tapestry-tutorial1/src/main/java");
		srcPaths.add("/tapestry-5.1.0.5-small/tapestry-upload/src/main/java");
	}

	private static boolean isSrcType(Type type) {
		String path = type.getIType().getPath().toString();

		for (String srcPath : srcPaths) {
			if (path.contains(srcPath)) {
				return true;
			}
		}

		return false;
	}

	public static void remTest(String dir) throws Exception {

		List<Path> files = FileFinder.find(dir, "*-all_*.txt");

		files.forEach(p -> {
			try {
				remTest(p);
			} catch (Exception e) {
				System.out.println("Error: " + e + "File " + p);
			}
		});

	}

	public static void remTest(Path inFile) throws IOException {

		String projName = Project.getProjectName(inFile);

		if (!projName.contains("-all")) {
			project = new Project(projName);
		} else {
			projLarge = new Project(projName.replaceFirst("-all", "-large"));
			projSmall = new Project(projName.replaceFirst("-all", "-small"));
		}
		
		Stream<String> inStream = Files.lines(inFile);

		try {
			System.out.println("\nRemove test: " + inFile + "\n");

			Set<String> outSet = new TreeSet<>();
			
			inStream.forEach(new Consumer<String>() {
				@Override
				public void accept(String inLine) {
					try {
						
						Type classFrom = null;
						Method method = null;
						String movement = inLine.split("\t", 2)[1];
						
						if(project != null) {
							classFrom = project.findClassFrom(movement);
							method = classFrom.getMethodBySignature(Method.getSignature(movement));
						} else {
							try {
								classFrom = projLarge.findClassFrom(movement);
								method = classFrom.getMethodBySignature(Method.getSignature(movement));
							} catch (Exception e) {
								classFrom = projSmall.findClassFrom(movement);
								method = classFrom.getMethodBySignature(Method.getSignature(movement));
							}
						}
						
						String visibility = "";

						if (! isSrcType(classFrom)) {
							visibility = "?";
						} else if (method.isPublic()) {
							visibility = "+";
						} else if (method.isPrivate()) {
							visibility = "-";
						} else if (method.isProtected()) {
							visibility = "#";
						}
						
						outSet.add(inLine.replaceFirst("\t", "\t" + visibility));

					} catch (Exception e) {
						System.out.println("Error: " + e + " Line " + inLine);

						outSet.add(inLine);
					}
				}
			});

			System.out.println("Result -> " + outSet.size());

			Files.write(Paths.get(inFile.toString().replace("-all_", "-all-src_")), outSet);

		} finally {
			inStream.close();
		}

	}

	public static void main(String[] args) throws Exception {
		remTest("C:\\Users\\bruno\\git\\iMove\\Results\\M CAMCJ mais IUCJ\\CAMC 50 -S -P -C\\ant-1.8.2");
	}

}
