/*******************************************************************************
 * Copyright (c) 2020 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package cn.simbaba.mydsl.scoping

import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.scoping.NestedTypeAwareImportNormalizerWithDotSeparator

/**
 * This custom import normalizer makes it possible to refer to entities defined in
 * our language with partially qualified names as well. Otherwise we wouldn't be
 * able to refer to, say, machine 'foo.M' just by 'M' inside package 'foo'.
 */
class JpImportNormalizer extends NestedTypeAwareImportNormalizerWithDotSeparator {
	boolean useAlias = false;
	String aliasName;

	new(QualifiedName importedNamespace, boolean wildcard, boolean ignoreCase) {
		super(importedNamespace, wildcard, ignoreCase);
	}
	
	new(QualifiedName importedNamespace, String alias, boolean wildcard, boolean ignoreCase) {
		super(importedNamespace, wildcard, ignoreCase);
		aliasName = alias;
		useAlias = true;
	}

	override protected resolveWildcard(QualifiedName relativeName) {
		getImportedNamespacePrefix().append(relativeName)
	}
	
	override protected internalResolve(QualifiedName relativeName) {
		val importedNamespace = getImportedNamespacePrefix()

		if (useAlias && aliasName.equals(relativeName.toString)) {
			return importedNamespace; //.skipLast(1).append(relativeName);
		}
		return super.internalResolve(relativeName);
	}

}
