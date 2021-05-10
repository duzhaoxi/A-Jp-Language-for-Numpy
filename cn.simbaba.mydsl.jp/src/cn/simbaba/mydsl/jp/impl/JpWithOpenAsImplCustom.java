package cn.simbaba.mydsl.jp.impl;

import org.eclipse.xtext.common.types.JvmIdentifiableElement;


public class JpWithOpenAsImplCustom extends JpWithOpenAsImpl implements JvmIdentifiableElement{
		
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
	public String toString() {
		return getSimpleName(); 
	}
}

