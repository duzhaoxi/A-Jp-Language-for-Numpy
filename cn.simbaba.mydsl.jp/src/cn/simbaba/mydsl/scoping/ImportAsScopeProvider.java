package cn.simbaba.mydsl.scoping;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.xbase.scoping.XImportSectionNamespaceScopeProvider;
import org.eclipse.xtext.xtype.XImportDeclaration;
import org.eclipse.xtext.xtype.XImportSection;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import cn.simbaba.mydsl.jp.impl.JpImportDeclarationImpl;


/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
public class ImportAsScopeProvider extends XImportSectionNamespaceScopeProvider {
	@Inject private IQualifiedNameConverter qualifiedNameConverter;

	@Override
	public IScope getScope(EObject context, EReference reference) {
		if (context == null || context.eResource() == null || context.eResource().getResourceSet() == null) {
			return IScope.NULLSCOPE;
		}
		return super.getScope(context, reference);
	}

	/**
	 * Create a new {@link ImportNormalizer} for the given namespace.
	 * @param namespace the namespace.
	 * @param ignoreCase <code>true</code> if the resolver should be case insensitive.
	 * @return a new {@link ImportNormalizer} or <code>null</code> if the namespace cannot be converted to a valid
	 * qualified name.
	 */
	@Override
	protected ImportNormalizer createImportedNamespaceResolver(final String namespace, boolean ignoreCase) {
		
		return super.createImportedNamespaceResolver(namespace, ignoreCase);
	}
	
	@Override
	protected List<ImportNormalizer> getImportedNamespaceResolvers(XImportSection importSection, boolean ignoreCase) {
		List<XImportDeclaration> importDeclarations = importSection.getImportDeclarations();
		List<ImportNormalizer> result = Lists.newArrayListWithExpectedSize(importDeclarations.size());
		
		for (XImportDeclaration imp: importDeclarations) {
			if (!imp.isStatic()) {
				String value = imp.getImportedNamespace();
				if(value == null) {
					value = imp.getImportedTypeName();
				}

				ImportNormalizer resolver;
				if (imp instanceof JpImportDeclarationImpl) {
					String alias = ((JpImportDeclarationImpl) imp).getAlias();
					QualifiedName qualifiedName = qualifiedNameConverter.toQualifiedName(value);
					resolver = new JpImportNormalizer(qualifiedName, alias, false, ignoreCase);
				} else {
					resolver = createImportedNamespaceResolver(value, ignoreCase);
				}
				
				if (resolver != null) {
					result.add(resolver);
				}
			}
		}

		return result;
	}

	@Override
	protected JpImportNormalizer doCreateImportNormalizer(QualifiedName importedNamespace, boolean wildcard, boolean ignoreCase) {
		return new JpImportNormalizer(importedNamespace, wildcard, ignoreCase);
	}
}