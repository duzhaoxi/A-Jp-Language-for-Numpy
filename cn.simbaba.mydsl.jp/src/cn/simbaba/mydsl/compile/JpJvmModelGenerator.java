package cn.simbaba.mydsl.compile;

import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.xbase.compiler.DisableCodeGenerationAdapter;
import org.eclipse.xtext.xbase.compiler.GeneratorConfig;
import org.eclipse.xtext.xbase.compiler.IGeneratorConfigProvider;
import org.eclipse.xtext.xbase.compiler.JvmModelGenerator;

import com.google.inject.Inject;

/**
 * if no package name, use default "cn.simbaba.jpy"
 * @author simbaba
 *
 */
public class JpJvmModelGenerator extends JvmModelGenerator {
	@Inject
	private IGeneratorConfigProvider generatorConfigProvider;

	/**
	 * check package name, change java file in correct class path
	 * @param type
	 * @param fsa
	 */
	@Override
	protected void _internalDoGenerate(JvmDeclaredType type, IFileSystemAccess fsa) {
		if (DisableCodeGenerationAdapter.isDisabled(type)) {
			return;
		}
 
		String qualifiedName = type.getQualifiedName();

		if (type.getPackageName()==null) {
			if (Character.isLowerCase(qualifiedName.charAt(0))) {
				qualifiedName = toCaptialName(qualifiedName);
			}
			qualifiedName = "cn/simbaba/jp/" + qualifiedName;
		}

		if(type.getQualifiedName() != null) {
			String fileName = qualifiedName.replace('.', '/') + ".java";
			fsa.generateFile(fileName, generateType(type, generatorConfigProvider.get(type)));
		}
	}

	/**
	 * generate package statement with default cn.simbab.jpy
	 */
	public CharSequence generateType(JvmDeclaredType type, GeneratorConfig config) {
		if (type.getPackageName()==null) {
			type.setPackageName("cn.simbaba.jp");
		}

		String typeName = type.getSimpleName();
		if (Character.isLowerCase(typeName.charAt(0))) {
			type.setSimpleName(toCaptialName(typeName));
		}
		return super.generateType(type, config);
	}

	private String toCaptialName(String str) {
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }
}
