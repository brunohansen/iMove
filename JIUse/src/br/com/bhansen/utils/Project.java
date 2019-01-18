package br.com.bhansen.utils;

import java.io.File;
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
		String projName = path.toString(); 
		projName = projName.substring(projName.lastIndexOf(File.separator) + 1);
		projName = projName.substring(0, projName.indexOf("_"));
		
		return projName;
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
		Type classFrom = findClassFrom(movement);
		
		return classFrom.getMethod(Method.getSignature(movement));
	}
	
	public Type findClassFrom(String movement) throws Exception {
		return findType(movement.split("::")[0]);
	}
	
	public Type findClassTo(String movement) throws Exception {
		return findType(movement.split("\t")[1]);
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
