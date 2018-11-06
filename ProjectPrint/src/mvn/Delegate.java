JDeodorant - Impedindo Movimento, mas permite

	com.mvnforum.categorytree.CategoryTree::build():String	com.mvnforum.categorytree.CategoryBuilder

Este é um exemplo nítido de inveja de dados porém só mover viola o padrão de consumo, os consumidores de build() consomem pelo menos metado dos métodos 
de CategoryTree e nenhum método de CategoryBuilder. O CAMC de build() são quase iguais em ambas as classes 25 em CategoryTree e 23 em CategoryBuilder oque 
faz com que ele pudesse ser movdo pelo CAMC.

Neste caso a extração permitiria a movimentação e faria com que a classe mentesse o design delegate dela. buld2() iuc 0 pois é só chamada em CategoryTree e camc 40, 
qdo build2 é movido para CategoryBuilder iuc 23 e camc 23 = 46

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
