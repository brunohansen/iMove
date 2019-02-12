
JDeodorant - Impedindo Movimento, mas permite se extraido

	com...CategoryTree::build():String -> com...CategoryBuilder



public class CategoryTree {
	...
	public String build() {
	    AssertionUtil.doAssert(categoryBuilder...);
	    
	    categoryBuilder.drawHeader();
	    categoryBuilder.drawBody();
	    categoryBuilder.drawFooter();
	
	    return categoryBuilder.getTree();
	}
	...
}

public class CategoryTree {
	...
	public String build() {
		return build2();
	}
	
	private String build2() {
		AssertionUtil.doAssert(categoryBuilder...);
	    
	    categoryBuilder.drawHeader();
	    categoryBuilder.drawBody();
	    categoryBuilder.drawFooter();
	
	    return categoryBuilder.getTree();
	}
	...
}

public class CategoryTree {
	
	private CategoryBuilder categoryBuilder;
	
	public CategoryTree(CategoryBuilder builder) {
		categoryBuilder = builder;
	}
	
	public void addCategeoryTreeListener(CategoryTreeListener listener) {
		categoryBuilder.setListener(listener);
	}
	
	public void setRoot(CategoryBean categoryRoot) {
		categoryBuilder.setRoot(categoryRoot);
	}
	
	public String build() {
		return categoryBuilder.build2();
	}
	
	public String getID() {
		return categoryBuilder.getID();
	}
}
