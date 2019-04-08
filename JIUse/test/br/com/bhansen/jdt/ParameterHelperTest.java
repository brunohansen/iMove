package br.com.bhansen.jdt;

import java.util.Arrays;
import java.util.HashSet;

public class ParameterHelperTest { 
	
	public static final void main(String[] args) throws Exception {
		String generics = "Class<?, ? extends Annotation>, AbstractChain0_<Input, Output>, AbstractDelegate<Chain<Input, Output>>, EntityFactory<Ent extends Entity<?>>, Id<T extends Id<T>>, CRUDer<Id, Ent extends Entity<Id>>, ServiceProvider   <    Service    super Retriever<?, ?>>";
		generics = ParameterHelper.explodGenerics(generics);
		
		String [] str = generics.split(", ");
		
		HashSet<String> hs = new HashSet<>(Arrays.asList(str));
				
		System.out.println(hs);
	}

}
