package br.com.bhansen.config;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jdt.core.JavaModelException;
import org.osgi.service.prefs.BackingStoreException;

import br.com.bhansen.jdt.Method;

public class MetricConfig extends Config {
	
    private static final String CONSTRUCTOR = "Constructor"; 
    private static final String ACCESSOR = "accessorMethods";
    private static final String PUBLIC = "publicMethods";
    private static final String PROTECTED = "protectedMethods";
    private static final String DEFAULT = "defaultMethods";
    private static final String PRIVATE = "privateMethods";
    
    private static final boolean CONSTRUCTOR_DEF = false; 
    private static final boolean ACCESSOR_DEF = true;
    private static final boolean PUBLIC_DEF = true;
    private static final boolean PROTECTED_DEF = true;
    private static final boolean DEFAULT_DEF = true;
    private static final boolean PRIVATE_DEF = false;
    
    public static boolean use(Method method) throws JavaModelException {

    	if(! usePublicMethods() && method.isPublic())
			return false;

		if (! usePrivateMethods() && method.isPrivate())
			return false;
		
		if (! useProtectedMethods() && method.isProtected())
			return false;
		
		if (! useDefaultMethods() && method.isDefault())
			return false;

		if (! useConstructor() && method.isConstructor())
			return false;
		
		if (! useAccessorMethods() && method.isAccessorMethod())
			return false;
		
		return true;
    }
    
    public static boolean useConstructor() {
    	return getPrefs().getBoolean(CONSTRUCTOR, CONSTRUCTOR_DEF);
    }
    
    public static boolean useAccessorMethods() {
    	return getPrefs().getBoolean(ACCESSOR, ACCESSOR_DEF);
    }
    
    public static boolean usePublicMethods() {
    	return getPrefs().getBoolean(PUBLIC, PUBLIC_DEF);
    }
    
    public static boolean useProtectedMethods() {
    	return getPrefs().getBoolean(PROTECTED, PROTECTED_DEF);
    }
    
    public static boolean useDefaultMethods() {
    	return getPrefs().getBoolean(DEFAULT, DEFAULT_DEF);
    }
    
    public static boolean usePrivateMethods() {
    	return getPrefs().getBoolean(PRIVATE, PRIVATE_DEF);
    }
    
    public static void setMetricConfig(boolean constructor, boolean accessor, boolean publ, boolean prot, boolean def, boolean priv) throws BackingStoreException {
		IEclipsePreferences prefs = getPrefs();

		prefs.putBoolean(CONSTRUCTOR, constructor);
		prefs.putBoolean(ACCESSOR, accessor);
		prefs.putBoolean(PUBLIC, publ);
		prefs.putBoolean(PROTECTED, prot);
		prefs.putBoolean(DEFAULT, def);
		prefs.putBoolean(PRIVATE, priv);
		
		prefs.flush();
    }
    
}
