JDeodorant - Impedindo Movimento

	com...PostWebHandler::deletePost(GenericRequest, PostBean):void -> com...PostBean
	com...ThreadWebHandler::deleteThread(GenericRequest, ThreadBean):void -> com...ThreadBean

JMove - Permitindo Movimento

	com...PostBean::deletePost(GenericRequest, PostWebHandler):void -> com...PostWebHandler

public class PostWebHandler {
	private void deletePost(GenericRequest, PostBean) {
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
	public void deleteThread(GenericRequest, ThreadBean) {
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

public class PostBean {
	...
	public int getPostID()
	public void setPostID(int postID)
	public int getForumID()
	public void setForumID(int forumID)
	public int getPostStatus()
	public void setPostStatus(int postStatus)	
	public void deletePost(GenericRequest, PostWebHandler) {
		...
	    OnlineUser onlineUser = postWebHandler.getOnlineUser(request);
	    MVNForumPermission permission = onlineUser.getPermission();
	    permission.ensureIsAuthenticated();
		
	    DAOFactory.getPostDAO().delete(postID);
	    
	    PostIndexer.scheduleDeletePostTask(postID, ...);
	    ...
	}
	...
}

public class PostWebHandler {
	...
    public void processAdd(GenericRequest, GenericResponse)
    public void preparePrintPost(GenericRequest, String)
    public void prepareEdit(GenericRequest, GenericResponse)
    public void processUpdate(GenericRequest)
    public void prepareDelete(GenericRequest, GenericResponse)
    public void processDelete(GenericRequest)
    public void processModeratePendingPosts(GenericRequest)
    public void processSearch(GenericRequest, GenericResponse)
    ...
}




