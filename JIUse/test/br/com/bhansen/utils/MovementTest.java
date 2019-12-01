package br.com.bhansen.utils;

import java.util.regex.Matcher;

import org.junit.Test;

import br.com.bhansen.test.TestCase;

public class MovementTest extends TestCase {
		
	public static void main(String[] args) {
		//String text = "com.mvnforum.admin.GeneralAdminTasksWebHandler::processMailTemplate(com.mvnforum.db.MemberBean, java.lang.String):java.lang.String	com.mvnforum.db.MemberBean";
		//String text = "org.junit.runners.model.TestClass::getAllArguments(Assignments, boolean):Object[]	org.junit.experimental.theories.internal.Assignments";
		//String text = "org.apache.tapestry5.integration.app1.data.Track::getTracks(GridSetDemo):Set<Track>	org.apache.tapestry5.integration.app1.pages.GridSetDemo";
		
		String text = "br.com.linkcom.sgm.service.UsuarioPapelService::getPapeis(Usuario):java.util.Set<Papel> br.com.linkcom.sgm.dao.UsuarioPapelDAO";
		
		Matcher mSC = Movement.linePattern.matcher(text);
		mSC.find();
		
		System.out.println(mSC.group(1) + "|\t|" + mSC.group(5));
		System.out.println(Movement.getMovementString(text));
	}
	
	//@Test
	public void testEqual1() {
		String mov1 = Movement.getMovementString("1	BD0.031905:BI0.015714:BF0.047619	DD0.036905:DI0.010714:DF0.047619	UD-0.005000:UI0.005000:UF0.000000	br.com.linkcom.sgm.service.AcompanhamentoIndicadorService::getListaDataAcompanhamentoIndicador(List<Indicador>, Calendar, Calendar):List<Calendar>	br.com.linkcom.sgm.dao.AcompanhamentoIndicadorDAO	Message: A correlação de dados é alta o suficiente para suportar a movimentação!");
		String mov2 = Movement.getMovementString("br.com.linkcom.sgm.service.AcompanhamentoIndicadorService::getListaDataAcompanhamentoIndicador(List<Indicador>,Calendar,Calendar):List<Calendar>    br.com.linkcom.sgm.dao.AcompanhamentoIndicadorDAO");
		
		checkThat("Equal1", mov1, mov2, equalTo());
	}
	
	@Test
	public void testEqual2() {
		String mov1 = Movement.getMovementString("br.com.linkcom.sgm.service.UsuarioPapelService::getPapeis(Usuario):Set<Papel>	br.com.linkcom.sgm.dao.UsuarioPapelDAO	Message: Tanto a correlação de dados quanto a de correlação de uso suportam a movimentação!");
		String mov2 = Movement.getMovementString("br.com.linkcom.sgm.service.UsuarioPapelService::getPapeis(Usuario):java.util.Set<Papel> br.com.linkcom.sgm.dao.UsuarioPapelDAO");
		
		checkThat("Equal1", mov1, mov2, equalTo());
	}

}
