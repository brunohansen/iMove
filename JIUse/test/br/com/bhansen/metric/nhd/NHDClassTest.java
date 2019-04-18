package br.com.bhansen.metric.nhd;

import br.com.bhansen.metric.nhdm.NHDMClass;

public class NHDClassTest {
	
	public static void main(String[] args) {
		//boolean[][] poMtrx = {{true, true, true, false},{false, true, true, true},{true, true, true, false}};
		boolean[][] poMtrx = {{true, false, true}, {true, true, false}, {false, true, true}, {true, false, false}, {true, false, false}};
		
		System.out.println(NHDClass.nhdClass(poMtrx, NHDMClass.NHDM));
		
	}

}
