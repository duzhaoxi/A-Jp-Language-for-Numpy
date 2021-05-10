package cn.simbaba.mydsl.typesystem;

import java.util.Iterator;

import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.typesystem.arguments.AssignmentFeatureCallArguments;
import org.eclipse.xtext.xbase.typesystem.arguments.IFeatureCallArguments;
import org.eclipse.xtext.xbase.typesystem.internal.AbstractLinkingCandidate;
import org.eclipse.xtext.xbase.typesystem.internal.ExpressionArgumentFactory;
import org.eclipse.xtext.xbase.typesystem.references.ArrayTypeReference;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;

import cn.simbaba.mydsl.jp.JpAssignment;


/**
 * refer from jbase
 * @author simbaba
 */
public class JpExpressionArgumentFactory extends ExpressionArgumentFactory {
	
	@Override
	public IFeatureCallArguments createExpressionArguments(
			XExpression expression, AbstractLinkingCandidate<?> candidate) {

		IFeatureCallArguments createExpressionArguments = super.createExpressionArguments(expression, candidate);
		if (!(expression instanceof JpAssignment)) {
			return createExpressionArguments;
		}

		if (!(createExpressionArguments instanceof AssignmentFeatureCallArguments)) {
			return createExpressionArguments;
		}

		
		AssignmentFeatureCallArguments assignmentFeatureCallArguments = (AssignmentFeatureCallArguments) createExpressionArguments;
		JpAssignment assignment = (JpAssignment) expression;
		LightweightTypeReference featureType = assignmentFeatureCallArguments.getDeclaredType();
		
		// if it's an array access we must take the array component type
		if (featureType instanceof ArrayTypeReference) {
			return new AssignmentFeatureCallArguments(assignment.getValue(), 
					getComponentType(featureType, assignment));
		}
//		else if ("NDArray".equals(featureType.getSimpleName())) {
//			return new AssignmentFeatureCallArguments(assignment.getValue(), 
//					candidate.computeArgumentTypes();
//					);
//		}
		else {
			return assignmentFeatureCallArguments;
		}
	}

	/**
	 * Computes the component type according to the number of array access expressions,
	 * for example
	 * 
	 * <pre>
	 * int[][] a;
	 * a[0] // type int[]
	 * a[0][0] // type int
	 * </pre>
	 * 
	 * @param arrayType
	 * @param arrayAccess
	 * @return
	 */
	private LightweightTypeReference getComponentType(LightweightTypeReference arrayType, JpAssignment arrayAccess) {
		LightweightTypeReference resultType = arrayType;
		Iterator<XExpression> indexes = arrayAccess.getIndexes().iterator();
		while (indexes.hasNext()) {
			LightweightTypeReference componentType = resultType.getComponentType();
			if (componentType == null) {
				return resultType;
			}
			resultType = componentType;
			indexes.next();
		}
		return resultType;
	}
}