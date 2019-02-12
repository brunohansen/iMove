JDeodorant - Impedindo movimentação

com...PostWebHandler::deletePost(GenericRequest, PostBean):void	com...PostBean
com...ThreadWebHandler::deleteThread(GenericRequest, ThreadBean):void	com...ThreadBean

Estas movimentações são incorretas pois dentro dos métodos são chamdos DAOs, Serviços e é feita veririficações de usuário logado e nesta arquitetura, nitidamente, não é 
desejável que este tipo de código vá parar em Entities/POJO. Movimentações para Entity/POJO foi um dos problemas que o Ricardo Terra apontou em seu artigo artigo!

Pela ótica do IUC, estes métodos são métodos utilitários e somente são utilizados no Controlador ao qual eles pertencem. E como os Controladores fazem muitos usos 
da classe PostBean ou ThreadBean o IUC não vê como uma violação no padrão de consumo. Porém, em ambos os casos CAMC não permite que os métodos sejam movidos, pois eles 
possuem o parâmetro GenericRequest que é um parâmetro do Controlador e que não esta presente Entity/POJO

JMove - Permitindo movimentação

com...PostBean::deletePost(GenericRequest, PostWebHandler):void	com...PostWebHandler

Mostra que a minha abordagem também faz o caminho oposto, permite que um método que deveria estar em um controlador saia de uma Entity/Pojo e vá para 
o Controlador adequado

JDeodorant - Impedindo movimento
	com...PostWebHandler::deletePost(GenericRequest, PostBean):void	com...PostBean
	com...ThreadWebHandler::deleteThread(GenericRequest, ThreadBean):void	com...ThreadBean

JMove - Permitindo movimento
	com...PostBean::deletePost(GenericRequest, PostWebHandler):void	com...PostWebHandler

public class PostWebHandler {
	private void deletePost(GenericRequest request, PostBean postBean) {
		...
	    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
	    MVNForumPermission permission = onlineUser.getPermission();
	    permission.ensureIsAuthenticated();
	    
	    AttachmentWebHandler.deleteAttachments_inPost(postID);
	    
	    DAOFactory.getPostDAO().delete(postID);
	    
	    PostIndexer.scheduleDeletePostTask(postID, ...);
	    ...
	}
}

public class PostBean {
	...
	public int getPostID()
	public void setPostID(int postID)
	public int getForumID()
	public void setForumID(int forumID)
	public int getPostStatus()
	public void setPostStatus(int postStatus)
	public String getPostIcon()
	public void setPostIcon(String postIcon)
	...
}

public class ThreadWebHandler {
	public void deleteThread(GenericRequest request, ThreadBean threadBean) {
	    ...
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        
        AttachmentWebHandler.deleteAttachments_inThread(threadID);
        
        DAOFactory.getThreadDAO().delete(threadID);
        
        PostIndexer.scheduleDeletePostTask(threadID, ...);
        ...
	}
}

public class ThreadBean {
	...
    public int getThreadID()
    public void setThreadID(int threadID)
    public int getForumID()
    public void setForumID(int forumID)
    public String getThreadTopic()
    public void setThreadTopic(String threadTopic)
    public String getThreadBody()
    public void setThreadBody(String threadBody)
    ...
}




