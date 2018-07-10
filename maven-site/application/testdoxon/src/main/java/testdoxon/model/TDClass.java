package testdoxon.model;

import java.util.ArrayList;

import testdoxon.util.TDGlobals;

public class TDClass {
	private ArrayList<TDMethod> methods;
	private String name;
	private String filepath;
	
	public TDClass (String name, String filepath) {
		this.name = name;
		this.filepath = filepath;
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
	
	public String getFilepath () {
		return this.filepath;
	}
	
	public String getPackage () {
		String _package = TDGlobals.getPackage(this.filepath);
		_package = _package.replaceAll("/", ".");
		_package = _package.substring(0, _package.length() - 1);
		return _package;
	}
	
}
