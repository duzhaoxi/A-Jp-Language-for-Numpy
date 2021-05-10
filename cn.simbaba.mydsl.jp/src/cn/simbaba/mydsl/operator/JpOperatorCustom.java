package cn.simbaba.mydsl.operator;

import static org.eclipse.xtext.naming.QualifiedName.*;
import javax.inject.Singleton;

import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.scoping.featurecalls.OperatorMapping;

@Singleton
public class JpOperatorCustom extends OperatorMapping {
	public static final QualifiedName OBJ_SET = create("]=");
	public static final QualifiedName RANGE = create(":");
	public static final QualifiedName BITWISE_AND = create("&");
	public static final QualifiedName BITWISE_OR = create("|");
	public static final QualifiedName BITWISE_XOR = create("^");
	public static final QualifiedName BITWISE_NOT = create("~");

	public static final QualifiedName BITWISE_AND_ASSIGN = create("&=");
	public static final QualifiedName BITWISE_OR_ASSIGN = create("|=");
	public static final QualifiedName BITWISE_XOR_ASSIGN = create("^=");

	@Override
	protected void initializeMapping() {
		super.initializeMapping();
		map.put(OBJ_SET, create(OP_PREFIX+"set"));
		map.put(RANGE, create(OP_PREFIX+"range"));
		
		map.remove(TRIPLE_EQUALS);
		map.put(EQUALS, create(OP_PREFIX + "tripleEquals"));

		map.remove(TRIPLE_NOT_EQUALS);
		map.put(NOT_EQUALS, create(OP_PREFIX + "tripleNotEquals"));

		map.put(BITWISE_AND, create("bitwiseAnd"));
		map.put(BITWISE_OR, create("bitwiseOr"));
		map.put(BITWISE_XOR, create("bitwiseXor"));
		map.put(BITWISE_NOT, create("bitwiseNot"));

		map.put(BITWISE_AND_ASSIGN, create("bitwiseAndAssign"));
		map.put(BITWISE_OR_ASSIGN, create("bitwiseOrAssign"));
		map.put(BITWISE_XOR_ASSIGN, create("bitwiseXorAssign"));

		compoundOperatorMapping.put(BITWISE_AND_ASSIGN, BITWISE_AND);
		compoundOperatorMapping.put(BITWISE_OR_ASSIGN, BITWISE_OR);
		compoundOperatorMapping.put(BITWISE_XOR_ASSIGN, BITWISE_XOR);
	}
}
