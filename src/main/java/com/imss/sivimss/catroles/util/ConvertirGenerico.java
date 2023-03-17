package com.imss.sivimss.catroles.util;

public class ConvertirGenerico {
	

	private ConvertirGenerico() {
		super();
	}

	public static <T> T convertInstanceOfObject(Object o) {
	    try {
	       return (T) o;
	    } catch (ClassCastException e) {
	        return null;
	    }
	}
}
