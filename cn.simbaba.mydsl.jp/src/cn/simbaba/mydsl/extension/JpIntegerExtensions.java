package cn.simbaba.mydsl.extension;

import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;


public class JpIntegerExtensions {
	
	@Pure
	@Inline(value="($1 : $2)", constantExpression=true)
	public static int[] operator_range(int from, int to) {
		return null;
	}

	@Pure
	@Inline(value="($1 : $2)", constantExpression=true)
	public static int operator_range(int to) {
		return 0;
	}

	public static int get(int to) {
		return 0;
	}

	@Pure
	@Inline(value="($1)", constantExpression=true)
	public static int operator_range() {
		return 0;
	}
	
	public static ArrayList<Integer> range(int to) {
		ArrayList<Integer> a = new ArrayList<>();
		for (int i = 0; i < to; i++) {
			a.add(i);
		}
		return a;
	}

	/**
	 * just a stub for type computing
	 * @param file
	 * @param mod
	 * @return
	 */
	public static InputStream open(String file, String mod) {
		return null;
	}

//	public static ArrayList<?> range(Object to) {
//		to.getClass().fi
//		return a;
//	}
}
