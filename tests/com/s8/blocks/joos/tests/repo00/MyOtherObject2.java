package com.s8.blocks.joos.tests.repo00;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.s8.io.joos.JOOS_Append;
import com.s8.io.joos.JOOS_Get;
import com.s8.io.joos.JOOS_Iterate;
import com.s8.io.joos.JOOS_Set;
import com.s8.io.joos.JOOS_Type;

@JOOS_Type(name="myOtherObject2")
public class MyOtherObject2 {

	public List<Double> coefficients;

	
	public String str1 = "kdjooijso";

	public String str2 = "kdjooijso";
	
	public String str3 = "kdjooijso";

	public String str4 = "kdjooijso";


	public MyOtherObject2() {
		super();
		coefficients = new ArrayList<>();
		coefficients.add(879.0);
		coefficients.add(-0.923);
	}


	@JOOS_Iterate(name = "coefficients")
	public void getCoefficients(Consumer<Double> consumer) {
		if(coefficients != null) {
			coefficients.forEach(c -> consumer.accept(c));
		}
	}

	
	@JOOS_Append(name = "coefficients")
	public void addCoefficient(double coefficient) {
		this.coefficients.add(coefficient);
	}


	@JOOS_Get(name = "str1")
	public String getStr1() { return str1; }

	@JOOS_Set(name = "str1")
	public void setStr1(String str) { this.str1 = str; }
	
	@JOOS_Get(name = "str2")
	public String getStr2() { return str2; }

	@JOOS_Set(name = "str2")
	public void setStr2(String str) { this.str2 = str; }

	@JOOS_Get(name = "str3")
	public String getStr3() { return str3; }

	@JOOS_Set(name = "str3")
	public void setStr3(String str) { this.str3 = str; }
	
	@JOOS_Get(name = "str4")
	public String getStr4() { return str4; }

	@JOOS_Set(name = "str4")
	public void setStr4(String str) { this.str4 = str; }

}
