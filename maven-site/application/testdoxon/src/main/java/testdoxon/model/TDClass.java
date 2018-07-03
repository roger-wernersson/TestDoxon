package testdoxon.model;

import java.util.ArrayList;

public class TDClass {
	private ArrayList<TDMethod> methods;
	private String name;
	
	public TDClass (String name) {
		this.name = name;
		this.methods = new ArrayList<TDMethod>();
	}
	
	public void addMethodname (TDMethod methodname) {
		this.methods.add(methodname);
	}
	
	public TDMethod[] getMethods () {
		return this.methods.toArray(new TDMethod[this.methods.size()]);
	}
	
	public String getName () {
		return this.name;
	}
	
}
