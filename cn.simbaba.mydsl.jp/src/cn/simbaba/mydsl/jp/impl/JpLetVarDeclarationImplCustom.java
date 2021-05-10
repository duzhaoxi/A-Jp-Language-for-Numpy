package cn.simbaba.mydsl.jp.impl;


public class JpLetVarDeclarationImplCustom extends JpLetVarDeclarationImpl {
	
	@Override
	public String getIdentifier() {
		return name;
	}
	
	@Override
	public String getSimpleName() {
		return name;
	}
	
	@Override
	public String getQualifiedName() {
		return name;
	}
	
	@Override
	public String getQualifiedName(char innerClassDelimiter) {
		return name;
	}

	@Override
	public boolean isWriteable() {
		return true;
	}

	@Override
	public String toString() {
		if (isWriteable()) {
			return "var " + getSimpleName(); 
		}
		return "val " + getSimpleName();
	}
}
