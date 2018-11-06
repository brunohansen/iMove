package mvn;

JMove

DMove Wrong com.mvnforum.admin.importexport.mvnforum.MvnForumMemberXML::addGlobalWatch(String,String,String,String,String,String):void com.mvnforum.admin.MemberXML DMove Wrong com.mvnforum.admin.importexport.mvnforum.MvnForumMemberXML::addMessageFolder(String,String,String,String):void com.mvnforum.admin.MemberXML

O IUC permite, pois nigu�m chama. J� o CAMC n�o permite pois esta em uma classe com par�metros s� String para uma classe que tem XMLWriter, al�m da movimenta��o obrigar adicionar um par�metro XMLWriter
 devido a chamada de addMember() e memberCreated. J� possui o m�todo na classe destino.

public class MvnForumMemberXML {
	...
	public void setMemberFirstIP(String value)
	public void setMemberLastIP(String value)
	public void setMemberViewCount(String value)
	public void setMemberPostCount(String value)
	public void setMemberCreationDate(String value)
	public void setMemberModifiedDate(String value)
	public void setMemberExpireDate(String value)
	public void setMemberLastLogon(String value)
	public void setMemberTempPassword(String value)
	...
	public void addMessageFolder(String folderName, String folderOrder, String folderCreationDate,
			String folderModifiedDate) {
		if ((!memberCreated) || (memberXML.getMemberID() < 0)) {
			addMember();
		}
		ImportMvnForum.addMessage("Adding message folder \"" + folderName + "\".");
		memberXML.addMessageFolder(folderName, folderOrder, folderCreationDate, folderModifiedDate);
	}

	public void addGlobalWatch(String watchType, String watchOption, String watchStatus, String watchCreationDate,
			String watchLastSentDate, String watchEndDate) {
		if ((!memberCreated) || (memberXML.getMemberID() < 0)) {
			addMember();
		}
		ImportMvnForum.addMessage("Adding global watch.");
		memberXML.addGlobalWatch(watchType, watchOption, watchStatus, watchCreationDate, watchLastSentDate,
				watchEndDate);
	}

}

public class MemberXML {
	...
	public void addMessageFolder(String folderName, String folderOrder,
                String folderCreationDate, String folderModifiedDate)
    public void addGlobalWatch(String watchType, String watchOption,
                String watchStatus, String watchCreationDate,
                String watchLastSentDate, String watchEndDate)
    public static void exportMessageFoldersForMember(XMLWriter xmlWriter, String memberName)
    public static void exportMessageFoldersForMember(XMLWriter xmlWriter, int memberID)
    public static void exportGlobalPermissionsForMember(XMLWriter xmlWriter, String memberName)
    public static void exportGlobalPermissionsForMember(XMLWriter xmlWriter, int memberID)
    public static void exportGlobalWatchesForMember(XMLWriter xmlWriter, String memberName)
    public static void exportGlobalWatchesForMember(XMLWriter xmlWriter, int memberID)
    public static void exportMember(XMLWriter xmlWriter, String memberName)
    public static void exportMember(XMLWriter xmlWriter, int memberID)
    public static void exportMemberList(XMLWriter xmlWriter)
}
