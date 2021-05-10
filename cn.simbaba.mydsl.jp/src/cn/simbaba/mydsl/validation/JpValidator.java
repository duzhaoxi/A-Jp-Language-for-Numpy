/*
 * generated by Xtext 2.25.0
 */
package cn.simbaba.mydsl.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.common.types.JvmGenericArrayTypeReference;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.ComposedChecks;
import org.eclipse.xtext.validation.ValidationMessageAcceptor;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XListLiteral;
import org.eclipse.xtext.xbase.XVariableDeclaration;
import org.eclipse.xtext.xbase.imports.StaticallyImportedMemberProvider;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.eclipse.xtext.xbase.util.XbaseUsageCrossReferencer;
import org.eclipse.xtext.xbase.validation.EarlyExitValidator;
import org.eclipse.xtext.xtype.XImportDeclaration;
import org.eclipse.xtext.xtype.XImportSection;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import cn.simbaba.mydsl.jp.JpFeature;
import cn.simbaba.mydsl.jp.JpAdditionalVariableDeclaration;
import cn.simbaba.mydsl.jp.JpArrayConstructorCall;
import cn.simbaba.mydsl.jp.JpAssignment;
import cn.simbaba.mydsl.jp.JpClass;
import cn.simbaba.mydsl.jp.JpPackage;
import cn.simbaba.mydsl.jp.JpOperation;
import cn.simbaba.mydsl.jp.JpPackageDeclaration;
import cn.simbaba.mydsl.jp.JpProperty;
import cn.simbaba.mydsl.jvmmodel.JpJvmModelHelper;
import cn.simbaba.mydsl.util.JpModelUtil;


/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
@ComposedChecks(validators = { EarlyExitValidator.class })
public class JpValidator extends AbstractJpValidator {
	String PREFIX = "cn.simbaba.compiler.Jsp";
	String INVALID_TYPE_NAME = PREFIX + "InvalidTypeName";
	String INVALID_FEATURE_NAME = PREFIX + "InvalidFeatureName";
	String MISSING_TYPE = PREFIX + "MissingType";
	String DUPLICATE_PROPERTY = PREFIX + "DuplicateProperty";
	String DUPLICATE_OPERATION = PREFIX + "DuplicateOperation";

	@Inject 
	private JpModelUtil jpModeUtil;
	
	@Inject
	private IJvmModelAssociations jvmModelAssociations;

	@Inject
	private StaticallyImportedMemberProvider staticallyImportedMemberProvider;

	@Inject
	private JpJvmModelHelper domainmodelJvmModelHelper;

	@Check
	public void checkTypeNameStartsWithCapital(JpClass entity) {
		if (!Character.isUpperCase(entity.getName().charAt(0))) {
			warning("Name should start with a capital", JpPackage.Literals.JP_ELEMENT__NAME,
					ValidationMessageAcceptor.INSIGNIFICANT_INDEX, INVALID_TYPE_NAME, entity.getName());
		}
	}

	@Check
	public void checkFeatureNameStartsWithLowercase(JpFeature feature) {
		if (!Character.isLowerCase(feature.getName().charAt(0))) {
			warning("Name should start with a lowercase", JpPackage.Literals.JP_FEATURE__NAME,
					ValidationMessageAcceptor.INSIGNIFICANT_INDEX, INVALID_FEATURE_NAME, feature.getName());
		}
	}

	@Check
	public void checkPackage(JpPackageDeclaration pack) {
		if (Strings.isEmpty(pack.getName())) {
			error("Name cannot be empty", JpPackage.Literals.JP_ELEMENT__NAME);
		}
		if ("java".equals(pack.getName())) {
			error("Invalid package name", JpPackage.Literals.JP_ELEMENT__NAME);
		}
	}

	protected boolean isLocallyUsed(EObject target, EObject containerToFindUsage) {
		if (target instanceof JpAdditionalVariableDeclaration) {
			containerToFindUsage = containerToFindUsage.eContainer();
		}
	
		if (target instanceof XVariableDeclaration) {
			XVariableDeclaration var = (XVariableDeclaration)target;
			
			if (var.getName().equals("_")) {
				return true;
			}

			if (((XVariableDeclaration) target).getType() instanceof JvmGenericArrayTypeReference && !super.isLocallyUsed(target, containerToFindUsage)) {
				// assigning to one of its elements should mark it as used
				Collection<Setting> refers = XbaseUsageCrossReferencer.find(target, containerToFindUsage);
				return refers.stream().map(it->it.getEObject()).anyMatch(it->it instanceof JpAssignment);
			}
		}

		return super.isLocallyUsed(target, containerToFindUsage);
	}

	@Check
	public void checkPropertyNamesAreUnique(JpClass entity) {
		Map<String, List<JpFeature>> name2properties = entity.getFeatures().stream()
			.filter(JpProperty.class::isInstance)
			.filter(it -> !StringExtensions.isNullOrEmpty(it.getName()))
			.collect(Collectors.groupingBy(JpFeature::getName));
		name2properties.values().forEach(properties -> {
			if (properties.size() > 1) {
				properties.forEach(it ->
					error("Duplicate property " + it.getName(), it, JpPackage.Literals.JP_FEATURE__NAME,
							DUPLICATE_PROPERTY)
				);
			}
		});
	}

	@Check
	public void checkOperationNamesAreUnique(JpClass entity) {
		JvmGenericType inferredJavaClass = IterableExtensions
				.head(Iterables.filter(jvmModelAssociations.getJvmElements(entity), JvmGenericType.class));
		domainmodelJvmModelHelper.handleDuplicateJvmOperations(inferredJavaClass, jvmOperations ->
			jvmOperations.stream()
				.map(it -> jvmModelAssociations.getPrimarySourceElement(it))
				.filter(JpOperation.class::isInstance)
				.map(JpOperation.class::cast)
				.forEach(it -> 
					error("Duplicate operation " + it.getName(), it, JpPackage.Literals.JP_FEATURE__NAME, DUPLICATE_OPERATION)
				)
		);
	}
	
	@Override
	protected void populateMaps(XImportSection importSection, final Map<String, List<XImportDeclaration>> imports,
			final Map<String, List<XImportDeclaration>> staticImports, final Map<String, List<XImportDeclaration>> extensionImports,
			final Map<String, JvmType> importedNames) {
		for (XImportDeclaration imp : importSection.getImportDeclarations()) {
//			if (imp.getImportedNamespace() != null) { 
//				addIssue("The use of wildcard imports is deprecated.", imp, org.eclipse.xtext.xbase.validation.IssueCodes.IMPORT_WILDCARD_DEPRECATED);
//				continue;
//			}
			
			JvmType importedType = imp.getImportedType();
			if (importedType == null || importedType.eIsProxy()) {
				continue;
			}
			
			Map<String, List<XImportDeclaration>> map = imp.isStatic() ? (imp.isExtension() ? extensionImports : staticImports) : imports;
			List<XImportDeclaration> list = map.get(importedType.getIdentifier());
			if (list != null) {
				list.add(imp);
				continue;
			}
			list = Lists.newArrayListWithCapacity(2);
			list.add(imp);
			map.put(importedType.getIdentifier(), list);

			if (!imp.isStatic()) {
				JvmType currentType = importedType;
				String currentSuffix = currentType.getSimpleName();
				JvmType collidingImport = importedNames.put(currentSuffix, importedType);
				if(collidingImport != null)
					error("The import '" + importedType.getIdentifier() + "' collides with the import '" 
							+ collidingImport.getIdentifier() + "'.", imp, null, org.eclipse.xtext.xbase.validation.IssueCodes.IMPORT_COLLISION);
				while (currentType.eContainer() instanceof JvmType) {
					currentType = (JvmType) currentType.eContainer();
					currentSuffix = currentType.getSimpleName()+"$"+currentSuffix;
					JvmType collidingImport2 = importedNames.put(currentSuffix, importedType);
					if(collidingImport2 != null)
						error("The import '" + importedType.getIdentifier() + "' collides with the import '" 
								+ collidingImport2.getIdentifier() + "'.", imp, null, org.eclipse.xtext.xbase.validation.IssueCodes.IMPORT_COLLISION);
				}
			} else if (!imp.isWildcard()) {
				Iterable<JvmFeature> allFeatures = staticallyImportedMemberProvider.getAllFeatures(imp);
				if (!allFeatures.iterator().hasNext()) {
					addIssue("The import " + imp.getImportedName() + " cannot be resolved.", imp, org.eclipse.xtext.xbase.validation.IssueCodes.IMPORT_UNRESOLVED);
					continue;
				}
			}
		}
	}

	@Check
	public void checkArrayConstructor(JpArrayConstructorCall cons) {
		XListLiteral arrayLiteral = cons.getArrayLiteral();
		EList<XExpression> dimensionExpressions = cons.getIndexes();

		if (dimensionExpressions.isEmpty() && arrayLiteral == null) {
			error("Constructor must provide either dimension expressions or an array initializer",
				cons,
				null,
				JpIssueCodes.ARRAY_CONSTRUCTOR_EITHER_DIMENSION_EXPRESSION_OR_INITIALIZER
			);
		} else if (!dimensionExpressions.isEmpty() && arrayLiteral != null) {
			error("Cannot define dimension expressions when an array initializer is provided",
				cons,
				null,
				JpIssueCodes.ARRAY_CONSTRUCTOR_BOTH_DIMENSION_EXPRESSION_AND_INITIALIZER
			);
		} else {
			ArrayList<XExpression> dimensionsAndIndexes = jpModeUtil.arrayDimensionIndexAssociations(cons);
			boolean foundEmptyDimension = false;
			for (XExpression d : dimensionsAndIndexes) {
				if (d == null) {
					foundEmptyDimension = true;
				} else if (foundEmptyDimension) {
					error("Cannot specify an array dimension after an empty dimension",
						d,
						null,
						JpIssueCodes.ARRAY_CONSTRUCTOR_DIMENSION_EXPRESSION_AFTER_EMPTY_DIMENSION
					);
					return;
				}
			}
		}
	}
}