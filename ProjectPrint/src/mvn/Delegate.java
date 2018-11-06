JDeodorant - Impedindo Movimento, mas permite

	com.mvnforum.categorytree.CategoryTree::build():String	com.mvnforum.categorytree.CategoryBuilder

Este � um exemplo n�tido de inveja de dados por�m s� mover viola o padr�o de consumo, os consumidores de build() consomem pelo menos metado dos m�todos 
de CategoryTree e nenhum m�todo de CategoryBuilder. O CAMC de build() s�o quase iguais em ambas as classes 25 em CategoryTree e 23 em CategoryBuilder oque 
faz com que ele pudesse ser movdo pelo CAMC.

Neste caso a extra��o permitiria a movimenta��o e faria com que a classe mentesse o design delegate dela. buld2() iuc 0 pois � s� chamada em CategoryTree e camc 40, 
qdo build2 � movido para CategoryBuilder iuc 23 e camc 23 = 46

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
        AssertionUtil.doAssert(categoryBuilder.getCategoryTreeListener() != null, "...");
        
        categoryBuilder.drawHeader();
        categoryBuilder.drawBody();
        categoryBuilder.drawFooter();

        return categoryBuilder.getTree();
    }

    public String getID() {
        return categoryBuilder.getID();
    }

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
        return build2();
    }

	private String build2() {
		AssertionUtil.doAssert(categoryBuilder.getCategoryTreeListener() != null, "...");
        
        categoryBuilder.drawHeader();
        categoryBuilder.drawBody();
        categoryBuilder.drawFooter();

        return categoryBuilder.getTree();
	}

    public String getID() {
        return categoryBuilder.getID();
    }

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
