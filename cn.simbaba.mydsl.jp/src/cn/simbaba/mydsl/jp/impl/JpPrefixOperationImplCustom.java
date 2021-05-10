package cn.simbaba.mydsl.jp.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XbasePackage;


public class JpPrefixOperationImplCustom extends JpPrefixOperationImpl {

	@Override
	public String getConcreteSyntaxFeatureName() {
		List<INode> list = NodeModelUtils.findNodesForFeature(this, XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE);
		if (list.size()!=1) {
			if (feature == null || feature.eIsProxy())
				return "<unkown>";
			return String.format("<implicit: %s>", feature.getIdentifier());
		}
		INode node = list.get(0);
		if (node instanceof ILeafNode) {
			return node.getText();
		}
		StringBuilder result = new StringBuilder();
		for(ILeafNode leafNode: node.getLeafNodes()) {
			if (!leafNode.isHidden())
				result.append(leafNode.getText());
		}
		return result.toString();
	}

	@Override
	public EList<XExpression> getExplicitArguments() {
		return ECollections.<XExpression>emptyEList();
	}

	@Override
	public boolean isExplicitOperationCallOrBuilderSyntax() {
		return true;
	}

	@Override
	public XExpression getActualReceiver() {
		return getActualReceiver(getOperand());
	}

	@Override
	public EList<XExpression> getActualArguments() {
		return getActualArguments(getOperand(), ECollections.<XExpression>emptyEList());
	}


	@Override
	public boolean isStatic() {
		JvmIdentifiableElement element = getFeature();
		if (element != null && !element.eIsProxy()) {
			if (element instanceof JvmFeature && !(element instanceof JvmConstructor))
				return ((JvmFeature) element).isStatic();
		}
		return false;
	}


	@Override
	public boolean isExtension() {
		return isExtension(getOperand());
	}


	@Override
	public boolean isPackageFragment() {
		return false;
	}


	@Override
	public boolean isTypeLiteral() {
		return false;
	}


	@Override
	public boolean isOperation() {
		return true;
	}

	protected boolean isExtension(XExpression syntacticReceiver) {
		return (isStatic() || getImplicitReceiver() != null)
				&& (syntacticReceiver != null || getImplicitFirstArgument() != null);
	}

	protected XExpression getActualReceiver(XExpression syntacticReceiver) {
		XExpression implicitReceiver = getImplicitReceiver();
		if (implicitReceiver != null)
			return implicitReceiver;
		if (isStatic())
			return null;
		return syntacticReceiver;
	}

	protected EList<XExpression> getActualArguments(XExpression syntacticReceiver, XExpression syntacticArgument) {
		if (syntacticArgument != null) {
			return getActualArguments(syntacticReceiver,
					new BasicEList<XExpression>(Collections.singletonList(syntacticArgument)));
		}
		return getActualArguments(syntacticReceiver, ECollections.<XExpression>emptyEList());
	}

	protected EList<XExpression> getActualArguments(XExpression syntacticReceiver, EList<XExpression> syntacticArguments) {
		if (isStatic()) {
			if (syntacticReceiver != null) {
				return createArgumentList(syntacticReceiver, syntacticArguments);
			}
			XExpression implicitFirstArgument = getImplicitFirstArgument();
			if (implicitFirstArgument != null) {
				return createArgumentList(implicitFirstArgument, syntacticArguments);
			}
		} else {
			XExpression implicitReceiver = getImplicitReceiver();
			if (implicitReceiver != null && syntacticReceiver != null) {
				return createArgumentList(syntacticReceiver, syntacticArguments);
			}
			XExpression implicitFirstArgument = getImplicitFirstArgument();
			if (implicitFirstArgument != null) {
				return createArgumentList(implicitFirstArgument, syntacticArguments);
			}
		}
		return syntacticArguments;
	}

}
