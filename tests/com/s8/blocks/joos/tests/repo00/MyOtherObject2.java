package com.s8.blocks.joos.tests.repo00;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.s8.io.joos.JOOS_Field;
import com.s8.io.joos.JOOS_Get;
import com.s8.io.joos.JOOS_Set;
import com.s8.io.joos.JOOS_Type;

@JOOS_Type(name="myOtherObject2")
public class MyOtherObject2 {

	@JOOS_Field(name="coeficients")
	public List<Double> coefficients;

	@JOOS_Field(name="str1")
	public String str1 = "kdjooijso";

	@JOOS_Field(name="str2")
	public String str2 = "kdjooijso";

	@JOOS_Field(name="str3")
	public String str3 = "kdjooijso";

	@JOOS_Field(name="str4")
	public String str4 = "kdjooijso";


	public MyOtherObject2() {
		super();
		coefficients = new ArrayList<>();
		coefficients.add(879.0);
		coefficients.add(-0.923);
	}


	public void getCoefficients(Consumer<Double> consumer) {
		if(coefficients != null) {
			coefficients.forEach(c -> consumer.accept(c));
		}
	}

	public void setCoefficients(double[] coefficients) {
		this.coefficients = coefficients;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}

	public double[] getCoefficients2() {
		return coefficients2;
	}

	public void setCoefficients2(double[] coefficients2) {
		this.coefficients2 = coefficients2;
	}

	public String getStr3() {
		return str3;
	}

	public void setStr3(String str3) {
		this.str3 = str3;
	}

	
	@JOOS_Get(name = "str4")
	public String getStr4() {
		return str4;
	}

	@JOOS_Set(name = "str4")
	public void setStr4(String str4) {
		this.str4 = str4;
	}

}
