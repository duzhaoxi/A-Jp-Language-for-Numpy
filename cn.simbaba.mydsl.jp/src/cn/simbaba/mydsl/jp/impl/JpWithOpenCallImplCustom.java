package cn.simbaba.mydsl.jp.impl;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.XExpression;


public class JpWithOpenCallImplCustom extends JpWithOpenCallImpl
{
	private JvmTypeReference returnType;
	
	public JvmTypeReference getReturnType() {
		return returnType;
	}
	
	public void setReturnType(JvmTypeReference returnType) {
		this.returnType = returnType;
	}
	
	@Override
	public void setFeature(JvmIdentifiableElement newFeature) {
		super.setFeature(newFeature);

		if (newFeature instanceof JvmOperation) {
			JvmOperation op = (JvmOperation)newFeature;
			op.setReturnType(returnType);
		}
	}
	
	@Override
	public String toString() {
		return getConcreteSyntaxFeatureName()+getExpressionsAsString(getFeatureCallArguments(),isExplicitOperationCall());
	}
	
	@Override
	public EList<XExpression> getExplicitArguments() {
		BasicEList<XExpression> result = new BasicEList<XExpression>();
		result.addAll(getFeatureCallArguments());
		return result;
	}
	
	@Override
	public boolean isExplicitOperationCallOrBuilderSyntax() {
		return super.isExplicitOperationCall() || !getFeatureCallArguments().isEmpty();
	}
	
	@Override
	public EList<XExpression> getActualArguments() {
		return getActualArguments(null, getFeatureCallArguments());
	}
	
	@Override
	public XExpression getActualReceiver() {
		return getActualReceiver(null);
	}

	@Override
	public boolean isExtension() {
		return isExtension(null);
	}
	
	@Override
	public boolean isPackageFragment() {
		ensureFeatureLinked();
		return super.isPackageFragment();
	}
	
	@Override
	public boolean isTypeLiteral() {
		ensureFeatureLinked();
		return super.isTypeLiteral();
	}

} //JpyWithOpenCallImpl