/*******************************************************************************
 * Copyright (c) 2020 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package cn.simbaba.mydsl.util

import com.google.inject.Inject
import com.google.inject.Singleton
import org.eclipse.xtext.xbase.XExpression
import cn.simbaba.mydsl.jp.JpArrayDimension
import cn.simbaba.mydsl.jp.JpArrayConstructorCall

/**
 * Utility methods for accessing the Jbase model.
 * 
 * @author Lorenzo Bettini
 * 
 */
@Singleton
class JpModelUtil {

	@Inject extension JpNodeModelUtil

	/**
	 * The returned list contains XExpressions corresponding to
	 * dimension specification in a XJArrayConstructorCall;
	 * if the n-th element in the list is null then it means that no
	 * dimension expression has been specified for that dimension.
	 * For example
	 * 
	 * <pre>new int[][0][][0]</pre>
	 * 
	 * will correspond to the list [null, XNumberLiteral, null, XNumberLiteral]
	 */
	def arrayDimensionIndexAssociations(JpArrayConstructorCall c) {
		val sortedByOffset = (c.dimensions + c.indexes).sortBy[elementOffsetInProgram]

		// there's at least one dimension [ if we parsed a XJArrayConstructorCall
		val associations = <XExpression>newArrayList()

		val last = sortedByOffset.reduce [ p1, p2 |
			if (p1 instanceof JpArrayDimension) {
				if (p2 instanceof XExpression) {
					// case "[ exp"
					associations.add(p2)
				} else {
					// case "[ ["
					associations.add(null)
				}
			}
			// else "exp [ " skip and go to the next state
			p2
		]
		if (last instanceof JpArrayDimension) {
			associations.add(null)
		}

		return associations
	}

}
