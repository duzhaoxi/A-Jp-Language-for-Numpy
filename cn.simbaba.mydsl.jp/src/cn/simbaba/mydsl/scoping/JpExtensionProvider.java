package cn.simbaba.mydsl.scoping;

import java.util.List;
import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedFeatures;

import cn.simbaba.mydsl.extension.JpIntegerExtensions;


public class JpExtensionProvider extends ImplicitlyImportedFeatures {

	/**
	 * -> detect jp language: with open()
	 */
	@Override
	protected List<Class<?>> getStaticImportClasses() {
		// TODO Auto-generated method stub
		List<Class<?>> imports = super.getStaticImportClasses();
		imports.add(JpIntegerExtensions.class);
		return imports;
	}

	/**
	 * -> detect jp language: N[2:], N[:2], N[1:2]
	 */
	@Override
	protected List<Class<?>> getExtensionClasses() {
		// TODO Auto-generated method stub
		List<Class<?>> imports = super.getExtensionClasses();
		imports.add(JpIntegerExtensions.class);
		return imports;
	}
}
