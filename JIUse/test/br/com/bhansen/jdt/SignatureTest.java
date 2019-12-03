package br.com.bhansen.jdt;

public class SignatureTest {
	
	public static void main(String[] args) {
		System.out.println(Signature.isSetterOrGetter("getDataAuditoria():Date"));
		System.out.println(Signature.isSetterOrGetter("getAcompanhamentoIndicador(WebRequestContext):AcompanhamentoIndicador"));
		System.out.println(Signature.isSetterOrGetter("getUsuarioEmail():String"));
	}

}
