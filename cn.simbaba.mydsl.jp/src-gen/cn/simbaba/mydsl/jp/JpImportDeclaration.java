/**
 * generated by Xtext 2.25.0
 */
package cn.simbaba.mydsl.jp;

import org.eclipse.xtext.xtype.XImportDeclaration;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Import Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link cn.simbaba.mydsl.jp.JpImportDeclaration#getAlias <em>Alias</em>}</li>
 * </ul>
 *
 * @see cn.simbaba.mydsl.jp.JpPackage#getJpImportDeclaration()
 * @model
 * @generated
 */
public interface JpImportDeclaration extends XImportDeclaration
{
  /**
   * Returns the value of the '<em><b>Alias</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Alias</em>' attribute.
   * @see #setAlias(String)
   * @see cn.simbaba.mydsl.jp.JpPackage#getJpImportDeclaration_Alias()
   * @model
   * @generated
   */
  String getAlias();

  /**
   * Sets the value of the '{@link cn.simbaba.mydsl.jp.JpImportDeclaration#getAlias <em>Alias</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Alias</em>' attribute.
   * @see #getAlias()
   * @generated
   */
  void setAlias(String value);

} // JpImportDeclaration