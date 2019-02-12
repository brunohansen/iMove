JDeodorant - Impedindo movimenta��o

com...PostWebHandler::deletePost(GenericRequest, PostBean):void	com...PostBean
com...ThreadWebHandler::deleteThread(GenericRequest, ThreadBean):void	com...ThreadBean

Estas movimenta��es s�o incorretas pois dentro dos m�todos s�o chamdos DAOs, Servi�os e � feita veririfica��es de usu�rio logado e nesta arquitetura, nitidamente, n�o � 
desej�vel que este tipo de c�digo v� parar em Entities/POJO. Movimenta��es para Entity/POJO foi um dos problemas que o Ricardo Terra apontou em seu artigo artigo!

Pela �tica do IUC, estes m�todos s�o m�todos utilit�rios e somente s�o utilizados no Controlador ao qual eles pertencem. E como os Controladores fazem muitos usos 
da classe PostBean ou ThreadBean o IUC n�o v� como uma viola��o no padr�o de consumo. Por�m, em ambos os casos CAMC n�o permite que os m�todos sejam movidos, pois eles 
possuem o par�metro GenericRequest que � um par�metro do Controlador e que n�o esta presente Entity/POJO

JMove - Permitindo movimenta��o

com...PostBean::deletePost(GenericRequest, PostWebHandler):void	com...PostWebHandler

Mostra que a minha abordagem tamb�m faz o caminho oposto, permite que um m�todo que deveria estar em um controlador saia de uma Entity/Pojo e v� para 
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




