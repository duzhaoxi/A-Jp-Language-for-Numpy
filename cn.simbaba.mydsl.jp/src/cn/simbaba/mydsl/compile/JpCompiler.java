package cn.simbaba.mydsl.compile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.xbase.XAbstractFeatureCall;
import org.eclipse.xtext.xbase.XAssignment;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XListLiteral;
import org.eclipse.xtext.xbase.XMemberFeatureCall;
import org.eclipse.xtext.xbase.XStringLiteral;
import org.eclipse.xtext.xbase.XbasePackage;
import org.eclipse.xtext.xbase.compiler.XbaseCompiler;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.impl.XBinaryOperationImpl;
import org.eclipse.xtext.xbase.impl.XMemberFeatureCallImpl;
import org.eclipse.xtext.xbase.impl.XNumberLiteralImpl;
import org.eclipse.xtext.xbase.impl.XPostfixOperationImplCustom;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;

import com.google.inject.Inject;

import cn.simbaba.mydsl.jp.JpArrayAccessExpression;
import cn.simbaba.mydsl.jp.JpArrayConstructorCall;
import cn.simbaba.mydsl.jp.JpAssignment;
import cn.simbaba.mydsl.jp.JpLetVarDeclaration;
import cn.simbaba.mydsl.jp.JpWithOpenAs;
import cn.simbaba.mydsl.jp.impl.JpArrayAccessExpressionImpl;
import cn.simbaba.mydsl.jp.impl.JpAssignmentImpl;
import cn.simbaba.mydsl.jp.impl.JpIndexImpl;
import cn.simbaba.mydsl.jp.impl.JpPrefixOperationImpl;
import cn.simbaba.mydsl.jp.impl.JpWithOpenCallImpl;
import cn.simbaba.mydsl.util.JpModelUtil;


public class JpCompiler extends XbaseCompiler {

	@Inject
	private JpModelUtil modelUtil;

	@Override
	protected void doInternalToJavaStatement(XExpression expr, ITreeAppendable b, boolean isReferenced) {
		if (expr instanceof JpArrayConstructorCall) {
			_toJavaStatement((JpArrayConstructorCall) expr, b, isReferenced);
		} 
		else if (expr instanceof JpArrayAccessExpression) {
			_toJavaStatement((JpArrayAccessExpression)expr, b, isReferenced);
		}
		else if (expr instanceof XBinaryOperationImpl) {
			_toJavaStatement((XBinaryOperationImpl)expr, b, isReferenced);
		}
		else if (expr instanceof JpWithOpenAs) {
			_toJavaStatement((JpWithOpenAs)expr, b, isReferenced);
		}
		else if (expr instanceof JpLetVarDeclaration) {
			_toJavaStatement((JpLetVarDeclaration)expr, b, isReferenced);
		}
		else if (expr instanceof JpAssignmentImpl) {
			_toJavaStatement((JpAssignmentImpl)expr, b, isReferenced);
		}
		else {
			super.doInternalToJavaStatement(expr, b, isReferenced);
		}
	}
	
	@Override
	protected void internalToConvertedExpression(XExpression obj, ITreeAppendable b) {
		if (obj instanceof JpArrayConstructorCall) {
			_toJavaExpression((JpArrayConstructorCall) obj, b);
		}
		else if (obj instanceof JpArrayAccessExpression) {
			_toJavaExpression((JpArrayAccessExpression) obj, b);
		}
		else if (obj instanceof XBinaryOperationImpl) {
			_toJavaExpression((XBinaryOperationImpl)obj, b);
		}
		else if (obj instanceof JpPrefixOperationImpl) { // should befor postfix
			_toJavaExpression((JpPrefixOperationImpl)obj, b);
		}
		else if (obj instanceof XPostfixOperationImplCustom) {
			_toJavaExpression((XPostfixOperationImplCustom)obj, b);
		}
		else {
			super.internalToConvertedExpression(obj, b);
		}
	}
	

	/**
	 * super will throw exception, so empty here
	 * 
	 * @param call
	 * @param b
	 * @param isReferenced
	 */
	public void _toJavaStatement(JpArrayConstructorCall call, ITreeAppendable b,
			boolean isReferenced) {
		// compile it only as expression
	}

	/**
	 * super will throw exception, so empty here
	 * 
	 * @param call
	 * @param b
	 * @param isReferenced
	 */
	public void _toJavaStatement(JpArrayAccessExpression access, ITreeAppendable b,
			boolean isReferenced) {
		// compile it only as expression
	}

	/**
	 * this.a[1]=2
	 * 
	 * @param expr
	 * @param b
	 * @param isReferenced
	 */
	public void _toJavaStatement(JpAssignmentImpl expr, ITreeAppendable b, boolean isReferenced) {
		b.newLine();
		internalToConvertedExpression(expr, b);
		b.append(";");
	}

	public void _toJavaStatement(JpLetVarDeclaration expr, ITreeAppendable b, boolean isReferenced) {
		XExpression right = expr.getRight();
		boolean isFeatureCall = false;
		if (right instanceof XMemberFeatureCall) {
			isFeatureCall = true;
		}
		
		/** don't use let like this, even support here! */
		if (right instanceof JpArrayAccessExpressionImpl) {
			b.newLine();
			b.append(expr.getType().getSimpleName());
			b.append(" ");
			b.append(expr.getName());
			b.append(" = ");
			internalToJavaExpression(right, b);
			b.append(";");
			return;
		}

		String rightName = expr.getRight().toString();

		b.newLine();
		
		if (!expr.getName().equals("_")) {
			b.append(expr.getType().getSimpleName());
			b.append(" ");
			b.append(expr.getName());
			b.append("=");
			if (isFeatureCall) {
				internalToJavaExpression(right, b);
			} else {
				b.append(rightName);
			}
			b.append("[0];");
		}

		for (int i = 0; i < expr.getOther().size(); i++) {
			String varName = expr.getOther().get(i).getName();
			if (varName.equals("_")) {
				continue;
			}
			b.newLine();
			b.append(expr.getType().getSimpleName());
			b.append(" ");
			b.append(varName);
			b.append("=");
			if (isFeatureCall) {
				internalToJavaExpression(right, b);
			} else {
				b.append(rightName);
			}
			b.append("[").append(Integer.toString(i+1)).append("];");
		}
	}

	/**
	 * 
	 * open with file, => try resource catch 
	 * 
	 * @param call
	 * @param b
	 * @param isReferenced
	 */
	public void _toJavaStatement(JpWithOpenAs call, ITreeAppendable b, boolean isReferenced) {
		JpWithOpenCallImpl featureCall = (JpWithOpenCallImpl) call.getOpen();
		EList<XExpression> arguments = featureCall.getActualArguments();
		JvmOperation withOpenOp = (JvmOperation)featureCall.getFeature();
		JvmType returnType = withOpenOp.getReturnType().getType();

		String file = ((XStringLiteral)arguments.get(0)).getValue();
		//internalToConvertedExpression(arguments.get(0), b);

		b.newLine().newLine();
		boolean isInput = returnType.getIdentifier().contains("InputStream");
		boolean isBuffering = returnType.getIdentifier().contains("Buffered");
		boolean isRadom = returnType.getIdentifier().contains("Random");
		
		b.newLine();
		b.append("try(").append(returnType).append(" ").append(call.getName());
		b.append(" = new ").append(returnType).append("(");

		if (isInput && isBuffering) {
			b.append(" new ").append(FileInputStream.class);
		} else if (isBuffering) {
			b.append(" new ").append(FileOutputStream.class);
		} else if (isRadom) {
			// empty
		} else {
			b.append(" new ").append(File.class);
		}

		b.append("(\"").append(file).append("\")");
		if (isRadom) {
			String mod = ((XStringLiteral)arguments.get(1)).getValue();
			b.append(", \"").append(mod).append("\"");
		}
		b.append(")");

		//internalToConvertedExpression(arguments.get(1), b);
		b.append("){").increaseIndentation();

		doInternalToJavaStatement(call.getWithExpression(), b, isReferenced);
		b.decreaseIndentation();
		b.newLine().append("} catch(").append(IOException.class).append(" e){");
		b.newLine().append("}");
	}


	/**
	 * 
	 * Support NDArray 1/a, a little magic
	 * We will generator code at _toJavaExpression
	 * 
	 * @param expr
	 * @param b
	 * @param isReferenced
	 */
	public void _toJavaStatement(XBinaryOperationImpl expr, ITreeAppendable b, boolean isReferenced) {
		XExpression left = expr.getLeftOperand();
		XExpression right = expr.getRightOperand();
		
		if (right instanceof XNumberLiteralImpl) {
			// OK, let's go
		} else if (left instanceof XNumberLiteralImpl) {
			JvmIdentifiableElement feature = expr.getFeature();
			if (feature instanceof JvmOperation) {
				expr.setLeftOperand(right);
				JvmOperation operation = (JvmOperation)feature;
				if ("operator_divide".equals(operation.getSimpleName())) {
					XNumberLiteralImpl number = (XNumberLiteralImpl) left;
					operation.setSimpleName("operator_reciprocal");
					number.setValue(number.getValue());
				} else if ("operator_minus".equals(operation.getSimpleName())) {
					XNumberLiteralImpl number = (XNumberLiteralImpl) left;
					operation.setSimpleName("operator_minus_after");
					number.setValue(number.getValue());
				}
				expr.setRightOperand(left);
			}
		}
		super._toJavaStatement(expr, b, isReferenced);
	}
	

	/**
	 * -999_999_999 is NDArray => a[:2]
	 * 
	 * @param expr
	 * @param b
	 */
	public void _toJavaExpression(JpPrefixOperationImpl expr, ITreeAppendable b) {
		b.append("0,");
		if (expr.getOperand() != null) {
			internalToConvertedExpression(expr.getOperand(), b);
		} else {
			b.append("-999_999_999");
		}
	}

	/**
	 *  -999_999_999 is NDArray => a[2:]
	 * 
	 * @param expr
	 * @param b
	 */
	public void _toJavaExpression(XPostfixOperationImplCustom expr, ITreeAppendable b) {
		if (expr.getFeature().getSimpleName().contains("range")) {
			internalToConvertedExpression(expr.getOperand(), b);
			b.append(",-999_999_999");
		} else {
			super.internalToConvertedExpression(expr, b);
		}
	}

	/**
	 * 
	 * NDArray a[1:-1]
	 * 
	 * @param expr
	 * @param b
	 */
	public void _toJavaExpression(XBinaryOperationImpl expr, ITreeAppendable b) {
		if (expr.getFeature().getSimpleName().contains("range")) {
			internalToConvertedExpression(expr.getLeftOperand(),b);
			b.append(",");
			internalToConvertedExpression(expr.getRightOperand(),b);
		} else {
			super.internalToConvertedExpression(expr, b);
		}
	}

	public void _toJavaExpression(JpArrayConstructorCall call, ITreeAppendable b) {
		if (call.getArrayLiteral() == null) {
			// otherwise we simply compile the array literal
			// assuming that no dimension expression has been specified
			// (checked by the validator)
			b.append("new ");
			b.append(call.getType());
		}
		compileArrayAccess(call, b);
	}

	/**
	 * if XMemberFeatureCallImpl only support 1-dimension
	 * @param arrayAccess
	 * @param b
	 */
	public void _toJavaExpression(JpArrayAccessExpression arrayAccess, ITreeAppendable b) {
		XExpression array = arrayAccess.getArray();
		internalToConvertedExpression(arrayAccess.getArray(), b);
		
		LightweightTypeReference actualType = getLightweightType(array);
		boolean isArray = actualType.isArray();

		if (!isArray && array instanceof XMemberFeatureCallImpl) {
			b.append(".get(");
			internalToConvertedExpression(arrayAccess.getIndexes().get(0), b);
			b.append(")");
		} else {
			compileArrayAccess(arrayAccess, b);
		}
	}

	/**
	 * Support assignment including:
	 * > a=b, 
	 * > this.a=b, 
	 * > a[1]=b,
	 * > a[1,2]=b
	 */
	@Override
	protected void assignmentToJavaExpression(XAssignment expr, ITreeAppendable b, boolean isExpressionContext) {
		final JvmIdentifiableElement feature = expr.getFeature();

		if (feature instanceof JvmOperation) {
			super.assignmentToJavaExpression(expr, b, isExpressionContext);
			return;
		}

		boolean isArgument = expr.eContainer() instanceof XAbstractFeatureCall;
		if (isArgument) {
			EStructuralFeature containingFeature = expr.eContainingFeature();
			if (containingFeature == XbasePackage.Literals.XFEATURE_CALL__FEATURE_CALL_ARGUMENTS 
					|| containingFeature == XbasePackage.Literals.XMEMBER_FEATURE_CALL__MEMBER_CALL_ARGUMENTS) {
				isArgument = false;
			} else {
				b.append("(");
			}
		}

		if (feature instanceof JvmField) {
			if (expr.getAssignable() != null) {
				appendReceiver(expr, b, isExpressionContext);
				b.append(".");
			}
			appendFeatureCall(expr, b);
		} else {
			String name = b.getName(expr.getFeature());
			b.append(name);
		}

		LightweightTypeReference actualType = getLightweightType(expr);
		boolean isArray = actualType.isArray();

		// NDArray set, x[1,2]=3
		if (!isArray && expr instanceof JpAssignment){
			JpAssignment access = (JpAssignment) expr;
			b.append(".operator_set(");
			internalToJavaExpression(expr.getValue(), b);
			indexRangeToJava(access.getIndexes(), b);
			b.append(")");
			return;
		}

		// x[1][2] = y
		compileArrayAccess(expr, b);

		b.append(" = ");
		internalToJavaExpression(expr.getValue(), b);

		if (isArgument) {
			b.append(")");
		}
	}

	private void compileArrayAccess(XExpression expr, ITreeAppendable b) {
		if (expr instanceof JpAssignment) {
			JpAssignmentImpl access = (JpAssignmentImpl) expr;
			for (XExpression index : access.getIndexes()) {
				b.append("[");
				internalToJavaExpression(index, b);
				b.append("]");
			}
		}
	}

	/**
	 * 
	 * NDArray get or Array get
	 * 
	 * a.get, a[1]
	 * 
	 * @param expr
	 * @param b
	 */
	private void compileArrayAccess(JpArrayAccessExpression expr, ITreeAppendable b) {
		LightweightTypeReference actualType = getLightweightType(expr.getArray());
		boolean isArray = actualType.isArray();

		JpArrayAccessExpression access = (JpArrayAccessExpression) expr;

		String left = isArray?"":".get(new int[][]{";
		String right = isArray?"":"})";

		for (XExpression index : access.getIndexes()) {
			b.append(left);

			if (index instanceof JpIndexImpl) {
				for (XExpression range: ((JpIndexImpl)index).getIndexes()) {
					b.append(isArray?"[":"{");
					internalToConvertedExpression(range, b);
					b.append(isArray?"]":"},");
				}
			} else {
				b.append("[");
				internalToConvertedExpression(index, b);
				b.append("]");
			}

			b.append(right);
		}
	}

	/**
	 * Specialization for {@link XJArrayConstructorCall} since it can
	 * have dimensions without dimension expression (index).
	 * int[] a  = new int[]{1,2,3}
	 * @param cons
	 * @param b
	 */
	private void compileArrayAccess(JpArrayConstructorCall cons, ITreeAppendable b) {
		XListLiteral arrayLiteral = cons.getArrayLiteral();

		if (arrayLiteral != null) {
			internalToJavaExpression(arrayLiteral, b);
		} else {
			Iterable<XExpression> dimensionsAndIndexes = modelUtil.arrayDimensionIndexAssociations(cons);
			
			for (XExpression e : dimensionsAndIndexes) {
				b.append("[");
				if (e != null) {
					internalToJavaExpression(e, b);
				}
				b.append("]");
			}
		}
	}

	/**
	 * NDArray operator_set( v, new int[][]{{},{},})
	 * 
	 * @param arguments
	 * @param b
	 */
	private void indexRangeToJava(List<XExpression> arguments, ITreeAppendable b) {
		int args = arguments.size();

		if (args == 0) {
			return;
		}

		b.append(", new int[][]{");

		for (int i = 0; i < args; i++) {
			b.append("{");
			appendArgument(arguments.get(i), b);
			b.append("}");
			if (i + 1 < arguments.size()) {
				b.append(", ");
			}
		}
		b.append("}");
	}
}
