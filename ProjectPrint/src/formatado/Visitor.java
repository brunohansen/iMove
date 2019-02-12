JDeodorant - Impedindo Movimento

	net...SAXVisitor::isAttributeNode(Node):boolean -> net...Node

public class HierarchicalConfigurationXMLReader {
	class SAXVisitor extends HierarchicalConfiguration.NodeVisitor {
		...
		public void visitAfterChildren(Node node, ConfigurationKey key)
		public void visitBeforeChildren(Node node, ConfigurationKey key)
		public boolean terminate()
		protected Attributes fetchAttributes(Node node)
		private String nodeName(Node node)
		private boolean isAttributeNode(Node node) {
			return ConfigurationKey.isAttributeKey(node.getName());
		}
		...
	}
}

public class HierarchicalConfiguration extends AbstractConfiguration {
	public static class Node implements Serializable, Cloneable {
		...
		public String getName()
		public Object getValue()
		public Node getParent()
		public void setName(String string)
		public void setValue(Object object)
		public void setParent(Node node)
		public void addChild(Node child)
		public void visit(NodeVisitor visitor, ConfigurationKey key)
		...
	}
}


