package com.qx.lang.joos.tests.example;

import com.qx.back.lang.joos.engine.JOOS_Engine;

public class UnitTest14 {

	/*
	 * 
	 * fluid=Toluene, phase=unknown, T=68,6�C, p=0,14 bar, density = 0,45 kg.m-3,  s = 0,97 kJ.kg-1.K-1,  h = 306,92 kJ.kg-1,  q = 0 %,  u = 0 kJ.kg-1,  w = 180,82 m.s-1,  Cp = 1,31 J.kg-1.K-1,  Cv = 1,21 J.kg-1.K-1,  lambda = 0,01 W.m-1.K-1,  mu = 7,78E-6 Pa.s,  ,
 fluid=Toluene, phase=unknown, T=243,68�C, p=0,14 bar, density = 599,59 kg.m-3,  s = 0,68 kJ.kg-1.K-1,  h = 306,24 kJ.kg-1,  q = 0 %,  u = 0 kJ.kg-1,  w = 407,51 m.s-1,  Cp = 2,73 J.kg-1.K-1,  Cv = 1,94 J.kg-1.K-1,  lambda = 0,08 W.m-1.K-1,  mu = 9,38E-5 Pa.s,  ,
	 * 
	 */
	
	public static void main(String[] args) throws Exception{
		JOOS_Engine engine = new JOOS_Engine();
		System.out.println(engine);
	}
	
}
