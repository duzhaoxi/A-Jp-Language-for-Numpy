package cn.simbaba.mydsl.validation;

public interface JpIssueCodes {
	public static String PREFIX = "jp.";

	public static String NOT_ARRAY_TYPE = PREFIX + "NotArrayType";
	public static String INStringID_BRANCHING_STATEMENT = PREFIX + "InStringidBranchingStatement";
	public static String MISSING_SEMICOLON = PREFIX + "MissingSemicolon";
	public static String MISSING_PARENTHESES = PREFIX + "MissingParentheses";
	public static String MISSING_DEFAULT = PREFIX + "MissingDefault";
	public static String DUPLICATE_METHOD = PREFIX + "DuplicateMethod";
	public static String ARRAY_CONSTRUCTOR_EITHER_DIMENSION_EXPRESSION_OR_INITIALIZER = PREFIX +
		"ArrayConstructorEitherDimensionExpressionOrInitializer";
	public static String ARRAY_CONSTRUCTOR_BOTH_DIMENSION_EXPRESSION_AND_INITIALIZER = PREFIX +
		"ArrayConstructorBothDimensionExpressionAndInitializer";
	public static String ARRAY_CONSTRUCTOR_DIMENSION_EXPRESSION_AFTER_EMPTY_DIMENSION = PREFIX +
		"ArrayConstructorDimensionExpressionAfterEmptyExpression";
	public static String INStringID_USE_OF_VAR_ARGS = PREFIX + "InStringidUseOfVarArgs";
	public static String INStringID_CLASS_OBJECT_EXPRESSION = PREFIX + "InStringidClassObjectExpression";
	public static String INCOMPLETE_CLASS_OBJECT = PREFIX + "IncompleteClassObject";
	public static String MISSING_RETURN = PREFIX + "MissingReturn";
	public static String INStringID_CHARACTER_CONSTANT = PREFIX + "InStringidCharacterConstant";
	public static String NOT_INITIALIZED_VARIABLE = PREFIX + "NotInitializedVariable";
	public static String MISSING_RESOURCES = PREFIX + "MissingResources";
	public static String NOT_AUTO_CLOSEABLE = PREFIX + "NotAutoCloseable";
}
