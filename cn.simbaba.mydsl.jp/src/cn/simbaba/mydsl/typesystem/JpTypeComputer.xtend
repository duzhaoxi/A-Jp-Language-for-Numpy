package cn.simbaba.mydsl.typesystem

import org.eclipse.xtext.xbase.annotations.typesystem.XbaseWithAnnotationsTypeComputer
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.typesystem.computation.ITypeComputationState
import org.eclipse.xtext.xbase.XbasePackage
import org.eclipse.xtext.xbase.impl.XNumberLiteralImpl
import org.eclipse.xtext.xbase.impl.XBinaryOperationImpl
import org.eclipse.xtext.xbase.XUnaryOperation
import org.eclipse.xtext.xbase.typesystem.references.ArrayTypeReference
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices
import org.eclipse.xtext.xbase.XPostfixOperation
import org.eclipse.xtext.xbase.jvmmodel.JvmTypeReferenceBuilder
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.XStringLiteral

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EStructuralFeature

import java.io.FileInputStream
import java.io.RandomAccessFile
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.BufferedOutputStream

import com.google.inject.Inject

import org.eclipse.xtext.xbase.typesystem.computation.ILinkingCandidate
import org.eclipse.xtext.xbase.typesystem.internal.ExpressionTypeComputationState
import org.eclipse.xtext.common.types.JvmIdentifiableElement
import org.eclipse.xtext.xbase.typesystem.internal.AbstractTypeComputationState
import org.eclipse.xtext.xbase.XBinaryOperation

import cn.simbaba.mydsl.jp.JpArrayConstructorCall
import cn.simbaba.mydsl.jp.JpAssignment
import cn.simbaba.mydsl.jp.JpArrayAccessExpression
import cn.simbaba.mydsl.jp.JpLetVarDeclaration
import cn.simbaba.mydsl.jp.impl.JpIndexImpl
import cn.simbaba.mydsl.jp.impl.JpWithOpenCallImplCustom
import cn.simbaba.mydsl.jp.impl.JpWithOpenAsImplCustom
import cn.simbaba.mydsl.jp.JpIndex

/**
 * @author simbaba - Initial contribution and API
 */
class JpTypeComputer extends XbaseWithAnnotationsTypeComputer {
	@Inject
	CommonTypeComputationServices services;
	
	@Inject 
	JvmTypeReferenceBuilder.Factory factory;

	JvmTypeReferenceBuilder jvmTypeReferenceBuilder;

	// static final String OPEN_MOD_READ="r,r+,rb,rb+";
	static final String OPEN_MOD_WRITE="w,w+,wb,wb+";
	static final String OPEN_MOD_BINARY="rb,rb+,wb,wb+,ab,ab+";

	override computeTypes(XExpression expression, ITypeComputationState state) {
		if (expression instanceof JpAssignment) {
			_computeTypes(expression, state)
		} else if (expression instanceof JpArrayConstructorCall) {
			_computeTypes(expression, state)
		} else if (expression instanceof XUnaryOperation) {
			_computeTypes(expression, state);
		} else if (expression instanceof JpArrayAccessExpression) {
			_computeTypes(expression, state)
		} else if (expression instanceof JpWithOpenAsImplCustom) {
			_computeTypes(expression, state)
		} else if (expression instanceof JpLetVarDeclaration) {
			_computeTypes(expression, state)
		} else if (expression instanceof JpIndexImpl) {
			_computeTypes(expression, state)
		} else if (expression instanceof XPostfixOperation) {
			_computeTypes(expression, state)
		} else if (expression instanceof XBinaryOperation) {
			_computeTypes(expression, state)
		} else {
			super.computeTypes(expression, state)
		}
	}
	
	def JvmTypeReferenceBuilder getJvmTypeReferenceBuilder(EObject context) {
		if (jvmTypeReferenceBuilder === null) {
			jvmTypeReferenceBuilder = factory.create(context.eResource().getResourceSet());
		}
		return jvmTypeReferenceBuilder;
	}

	/**
	 * support let x,_,z = a
	 */
	override protected addLocalToCurrentScope(XVariableDeclaration localVariable, ITypeComputationState state) {
		if (localVariable.name != '_') {
			super.addLocalToCurrentScope(localVariable, state)
		}

		// should not be null, avoid infinite loop
		if (localVariable instanceof JpLetVarDeclaration) {
			if (localVariable.right === null) {
				return;
			}
			for (other : localVariable.other) {
				if(other.name != '_' ) {
					super.addLocalToCurrentScope(other, state)
				}
			}
		}
	}

	/**
	 * let x,y,z = a
	 */
	def protected _computeTypes(JpLetVarDeclaration expr, ITypeComputationState state) {
		val actualType = state.computeTypes(expr.right).actualExpressionType
		val typeBuilder = getJvmTypeReferenceBuilder(expr);

		if (actualType === null) {
			return; // should not happen!!!!
		}

		if (actualType.isArray) {
			expr.type = typeBuilder.typeRef(actualType.componentType.type);
		} else if (actualType.type !== null){
			expr.type = typeBuilder.typeRef(actualType.type);
		}

		super.computeTypes(expr, state);

		for (additional : expr.other) {
			state.withoutExpectation.computeTypes(additional)
		}
	}

	/**
	 * with open as f {
	 *   expression
	 * }
	 */
	def protected _computeTypes(JpWithOpenAsImplCustom expr, ITypeComputationState state) {
		val expressionState = state.withoutExpectation;
		val actualType = expressionState.computeTypes(expr.open);

		val openCall = expr.open as JpWithOpenCallImplCustom

		if (openCall.concreteSyntaxFeatureName != "open") {
			state.assignType(expr, actualType.actualExpressionType);
		} else {
			val type = _computeOpenStreamType(openCall.featureCallArguments, state);
			openCall.returnType = type.toTypeReference
			state.assignType(expr, type);
		}

		state.addLocalToCurrentScope(expr);

		state.withinScope(expr);
		state.computeTypes(expr.withExpression);
	}

	def protected _computeOpenStreamType(EList<XExpression> arguments, ITypeComputationState state) {
		var isBinary=false

		// check only first argument
		val option=arguments.get(1) as XStringLiteral
		val optionValue = option.value
		
		if (optionValue.contains('+')){
			return getTypeForName(typeof(RandomAccessFile), state);
		}

		val mods = OPEN_MOD_BINARY.split(",")
		val ret = mods.findFirst[m|m.equals(optionValue)]
		if (ret !== null) {
			isBinary = true
		}

		 if (OPEN_MOD_WRITE.contains(optionValue)) {
			return isBinary?
				getTypeForName(typeof(FileOutputStream), state):
				getTypeForName(typeof(BufferedOutputStream),state)
		} else {
			return isBinary?
				getTypeForName(typeof(FileInputStream), state):
				getTypeForName(typeof(BufferedInputStream),state)
		}
	}
	
	/**
	 * JpIndex composite of JpRange
	 * [1,2:, 3:4]
	 */
	def protected _computeTypes(JpIndex expression, ITypeComputationState state) {
		for (index: expression.indexes) {
			state.withoutExpectation().computeTypes(index);
		}
	}

	/**
	 * if JpRange only has a ':' -> 0:-999_999_999
	 */
	def protected _computeTypes(XPostfixOperation expr, ITypeComputationState state) {
		val feaureName = expr.getConcreteSyntaxFeatureName();
		if (expr.operand === null && feaureName == ":") {
			val constructor = XNumberLiteralImpl.getDeclaredConstructor();
			constructor.accessible = true;
			var operand = constructor.newInstance();
			operand.value = "-999_999_999";
			expr.operand = operand;
			super.computeTypes(expr, state)
			expr.operand = null;
		}
		super.computeTypes(expr, state)
	}

	/**
	 * try compute 1+a, but a+1 is simple
	 */
	def protected _computeTypes(XBinaryOperationImpl expr, ITypeComputationState state) {
		val rightOperand = expr.rightOperand;
		val leftOperand = expr.leftOperand;

		if (leftOperand instanceof XNumberLiteralImpl) {
			expr.leftOperand = rightOperand;
			expr.rightOperand = leftOperand;
			super.computeTypes(expr, state);
			expr.leftOperand = leftOperand;
			expr.rightOperand = rightOperand;
		} else {
			super.computeTypes(expr, state);
		}
	}

	/**
	 * support a[1,2][2]
	 */
	def protected _computeTypes(JpArrayAccessExpression arrayAccess, ITypeComputationState state) {
		val actualType = state.withNonVoidExpectation.computeTypes(arrayAccess.array).actualExpressionType

		// arrayAccess.array can be null in expressions of the shape
		// ()[0]
		if (actualType === null) {
			state.acceptActualType(getPrimitiveVoid(state))
		} else {
			val type = componentTypeOfArrayAccess(arrayAccess, actualType, state, XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE)
			state.acceptActualType(type)
		}

		checkArrayIndexHasTypeInt(arrayAccess.indexes, state);
	}

	private def checkArrayIndexHasTypeInt(EList<XExpression> indexes, ITypeComputationState state) {
		for (index : indexes) {
			val _typeForName = getTypeForName(typeof(JpIndexImpl), state);
			val conditionExpectation = state.withExpectation(_typeForName)
			conditionExpectation.computeTypes(index)
		}
	}

	private def componentTypeOfArrayAccess(JpArrayAccessExpression arrayAccess, LightweightTypeReference type, ITypeComputationState state, EStructuralFeature featureForError) {
		var currentType = type
		for (index : arrayAccess.indexes) {
			if (currentType instanceof ArrayTypeReference) {
				currentType = currentType.componentType
			} else { // simbaba: support maybe object.get
				return currentType
			}
		}
		return currentType
	}
	
	/**
	 * support a[1,:,1:2]=9
	 */
	def protected _computeTypes(JpAssignment assignment, ITypeComputationState state) {
		val candidates = state.getLinkingCandidates(assignment)
		val best = getBestCandidate(candidates);
		val expressionState = state as ExpressionTypeComputationState
		val featureType = getDeclaredType(best.feature, expressionState)
		
		if (assignment.assignable !== null) {
			state.computeTypes(assignment.assignable);
		}

		if (featureType.isArray) {
			best.applyToComputationState()
		} else {
			state.computeTypes(assignment.value)
			assignment.feature = best.feature
		}
		
		computeTypesOfArrayAccess(assignment, best, state, XbasePackage.Literals.XASSIGNMENT__ASSIGNABLE)
	}

	private def computeTypesOfArrayAccess(JpAssignment arrayAccess, 
		ILinkingCandidate best, ITypeComputationState state, EStructuralFeature featureForError
	) {
		checkArrayIndexHasTypeInt(arrayAccess, state);
		val expressionState = state as ExpressionTypeComputationState
		val featureType = getDeclaredType(best.feature, expressionState)
		componentTypeOfArrayAccess(arrayAccess, featureType, state, featureForError)
	}

	def protected _computeTypes(JpArrayConstructorCall call, ITypeComputationState state) {
		checkArrayIndexHasTypeInt(call.indexes, state)
		val typeReference = services.typeReferences.createTypeRef(call.type)
		val lightweight = state.referenceOwner.toLightweightTypeReference(typeReference)
		var arrayTypeRef = lightweight
		
		for (i : 0..<call.dimensions.size) {
			arrayTypeRef = state.referenceOwner.newArrayTypeReference(arrayTypeRef)
		}
		
		if (call.arrayLiteral !== null) {
			state.withExpectation(arrayTypeRef).computeTypes(call.arrayLiteral)
		}
		
		state.acceptActualType(arrayTypeRef)
	}

	def private getDeclaredType(JvmIdentifiableElement feature, AbstractTypeComputationState state) {
		val result = state.getResolvedTypes().getActualType(feature);
		if (result === null) {
			return state.getReferenceOwner().newAnyTypeReference();
		}
		return result;
	}

	private def checkArrayIndexHasTypeInt(JpAssignment arrayAccess, ITypeComputationState state) {
		for (index : arrayAccess.indexes) {
			val conditionExpectation = state.withExpectation(getTypeForName(Integer.TYPE, state))
			conditionExpectation.computeTypes(index)
		}
	}

	private def componentTypeOfArrayAccess(JpAssignment arrayAccess, LightweightTypeReference type, ITypeComputationState state, EStructuralFeature featureForError) {
		var currentType = type
		
		for (index : arrayAccess.indexes) {
			if (currentType instanceof ArrayTypeReference) {
				currentType = currentType.componentType
			} else {
//				val diagnostic = new EObjectDiagnosticImpl(
//					Severity.ERROR,
//					JpIssueCodes.NOT_ARRAY_TYPE, 
//					"The type of the expression must be an array type but it resolved to " + currentType.simpleName,
//					arrayAccess,
//					featureForError,
//					-1,
//					null);
//				state.addDiagnostic(diagnostic);
				return currentType
			}
		}
		return currentType
	}
}
