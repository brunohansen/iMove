package br.com.bhansen.utils;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

public class Project {
	
	private IJavaProject javaProject;
	
	public Project(IJavaProject project) {
		javaProject = project;
	}
	
	public Project(Path path) {
		this(getProjectName(path));
	}
	
	public Project(String projectName) {
		IProject project = getRoot().getProject(projectName);
		
		if(! project.exists() || ! project.isOpen()) {
			throw new RuntimeException("Project " + projectName + " not exists or closed!");
		}
			
		javaProject = JavaCore.create(project);
	}
	
	private static IWorkspaceRoot getRoot() {
		// Get the root of the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		return root;
	}
	
	public static String getProjectName(Path path) {
		try {
			String projName = path.getFileName().toString();
			projName = projName.substring(0, projName.indexOf("_"));
			
			return projName;
		} catch (Exception e) {
			throw new RuntimeException("Invalid file project name syntax");
		}
	}
	
	public static String[] getProjectsNames() {
		// Get all projects in the workspace
		IProject[] projects = getRoot().getProjects();
		String[] projNames = new String[projects.length];
		for (int i = 0; i < projects.length; i++) {
			projNames[i] = projects[i].getName();			
		}
		
		return projNames;
	}
	
	public Method findMethod(String movement) throws Exception {
		String [] mvt = Movement.getMovement(movement);
		Type classFrom = findType(mvt[Movement.SOURCE_CLASS]);
		
		return classFrom.getMethod(mvt[Movement.METHOD]);
	}
	
	public Type findClassFrom(String movement) throws Exception {
		return findType(Movement.getSourceClass(movement));
	}
	
	public Type findClassTo(String movement) throws Exception {
		return findType(Movement.getTargetClass(movement));
	}
	
	private Type findType(String fullyQualifiedName) throws Exception {
		IType iType = javaProject.findType(fullyQualifiedName);
		
		if(iType != null) {
			return new Type(iType);
		} else {
			throw new Exception("Class " + fullyQualifiedName + " not found!");
		}
	}
	
}
