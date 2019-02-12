JDeodorant - Impedindo Movimento

	com...PostWebHandler::deletePost(GenericRequest, PostBean):void -> com...PostBean
	com...ThreadWebHandler::deleteThread(GenericRequest, ThreadBean):void -> com...ThreadBean

JMove - Permitindo Movimento

	com...PostBean::deletePost(GenericRequest, PostWebHandler):void -> com...PostWebHandler

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



