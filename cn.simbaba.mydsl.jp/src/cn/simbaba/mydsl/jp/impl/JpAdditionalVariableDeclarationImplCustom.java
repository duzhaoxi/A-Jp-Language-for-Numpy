package cn.simbaba.mydsl.jp.impl;

import org.eclipse.xtext.common.types.JvmTypeReference;

import cn.simbaba.mydsl.jp.JpLetVarDeclaration;


public class JpAdditionalVariableDeclarationImplCustom extends JpAdditionalVariableDeclarationImpl {

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
		writeable = getContainingVariableDeclaration().isWriteable();
		return writeable;
	}

	/**
	 * Custom implementation that returns the type of the containing variable declaration.
	 * 
	 * @see org.eclipse.xtext.xbase.impl.XVariableDeclarationImpl#getType()
	 */
	@Override
	public JvmTypeReference getType() {
		type = getContainingVariableDeclaration().getType() ;
		return type;
	}

	protected JpLetVarDeclaration getContainingVariableDeclaration() {
		return (JpLetVarDeclaration)eContainer();
	}
}
