
public class MvnForumMemberXML {
	...
	public void addMember()
	public void addMemberPermission(String permission)	
	public void addMessageFolder(String folderName, ...) {
        if ( (!memberCreated) || (memberXML.getMemberID()<0) ) {
            addMember();
        }
        ImportMvnForum.addMessage("Adding message folder \"" + ...);
        memberXML.addMessageFolder(folderName, ...);
    }
	public void addGlobalWatch(String ...) 
	...
}

public class MemberXML {
	...
	public int getMemberID()
	public void setMemberID(String id)
	public void addMemberPermission(String permission) 
	public static void addGuestMemberPermission(String permission)
	public static void addAdminMemberPermission(String permission)
	public static void addMemberPermission(String memberName, ...)
	public void addMessageFolder(String folderName, ...)
	public void addGlobalWatch(String ...)
	...
}
