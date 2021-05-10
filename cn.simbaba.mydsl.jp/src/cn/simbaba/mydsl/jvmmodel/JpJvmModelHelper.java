package cn.simbaba.mydsl.jvmmodel;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.xbase.typesystem.override.IResolvedOperation;
import org.eclipse.xtext.xbase.typesystem.override.OverrideHelper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;

public class JpJvmModelHelper {

	@Inject
	private OverrideHelper overrideHelper;

	/**
	 * Detects duplicated {@link JvmOperation}s in the passed {@link JvmDeclaredType} taking into consideration overloading and type
	 * erasure; each collection of duplicates is passed to the consumer.
	 */
	public void handleDuplicateJvmOperations(JvmDeclaredType jvmDeclaredType, Consumer<Collection<JvmOperation>> consumer) {
		// takes into consideration overloading and type erasure
		List<IResolvedOperation> methods = overrideHelper.getResolvedFeatures(jvmDeclaredType).getDeclaredOperations();
		Multimap<String, JvmOperation> signature2Declarations = HashMultimap.create();

		methods.forEach(method -> signature2Declarations.put(method.getResolvedErasureSignature(), method.getDeclaration()));

		signature2Declarations.asMap().values().forEach(jvmOperations -> {
			if (jvmOperations.size() > 1) {
				consumer.accept(jvmOperations);
			}
		});
	}
}