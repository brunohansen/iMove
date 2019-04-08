package br.com.bhansen.utils;

import java.util.regex.Matcher;

public class MovementTest {
		
	public static void main(String[] args) {
		String text = "com.mvnforum.admin.GeneralAdminTasksWebHandler::processMailTemplate(com.mvnforum.db.MemberBean, java.lang.String):java.lang.String	com.mvnforum.db.MemberBean";
		//String text = "org.junit.runners.model.TestClass::getAllArguments(Assignments, boolean):Object[]	org.junit.experimental.theories.internal.Assignments";
		//String text = "org.apache.tapestry5.integration.app1.data.Track::getTracks(GridSetDemo):Set<Track>	org.apache.tapestry5.integration.app1.pages.GridSetDemo";
		
		Matcher mSC = Movement.linePattern.matcher(text);
		mSC.find();
		
		System.out.println(mSC.group(1) + "|\t|" + mSC.group(5));
				
	}

}
