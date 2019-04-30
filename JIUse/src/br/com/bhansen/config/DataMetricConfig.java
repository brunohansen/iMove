package br.com.bhansen.config;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

import br.com.bhansen.jdt.MethodWithParameters;

public class DataMetricConfig extends Config {
	
    private static final String PRIMITIVES = "primitives";
    private static final String ARRAYS_AND_COLLECTIONS= "arraysAndCollections";
    private static final String RETURN = "return";
    private static final String GENERICS = "extractGenerics";
    private static final String THIS = "this";
    private static final String PARAMETERLESS = "parameterlessMethods";
    
    private static final boolean PRIMITIVES_DEF = false;
    private static final boolean ARRAYS_AND_COLLECTIONS_DEF = true;
    private static final boolean RETURN_DEF = true;
    private static final boolean GENERICS_DEF = true;
    private static final boolean THIS_DEF = false;
    private static final boolean PARAMETERLESS_DEF = true;
    
    public static boolean use(MethodWithParameters method) {
    	
		if(! DataMetricConfig.useParameterlessMethods() && ! method.hasParameter())
			return false;
		
		return true;
    }
    
    public static boolean usePrimitives() {
    	return getPrefs().getBoolean(PRIMITIVES, PRIMITIVES_DEF);
    }
    
    public static boolean useArraysAndCollections() {
    	return getPrefs().getBoolean(ARRAYS_AND_COLLECTIONS, ARRAYS_AND_COLLECTIONS_DEF);
    }
    
    public static boolean useReturn() {
    	return getPrefs().getBoolean(RETURN, RETURN_DEF);
    }
    
    public static boolean extractGenerics() {
    	return getPrefs().getBoolean(GENERICS, GENERICS_DEF);
    }
    
    public static boolean useThis() {
    	return getPrefs().getBoolean(THIS, THIS_DEF);
    }
    
    public static boolean useParameterlessMethods() {
    	return getPrefs().getBoolean(PARAMETERLESS, PARAMETERLESS_DEF);
    }
    
    public static void setDataMetricConfig(boolean primitives, boolean arraysAndCollections, boolean _return, boolean extractGenerics, boolean _this, boolean parameterlessMethods) throws BackingStoreException {
		IEclipsePreferences prefs = getPrefs();

		prefs.putBoolean(PRIMITIVES, primitives);
		prefs.putBoolean(ARRAYS_AND_COLLECTIONS, arraysAndCollections);
		prefs.putBoolean(RETURN, _return);
		prefs.putBoolean(GENERICS, extractGenerics);
		prefs.putBoolean(THIS, _this);
		prefs.putBoolean(PARAMETERLESS, parameterlessMethods);
		
		prefs.flush();
    }
    
}