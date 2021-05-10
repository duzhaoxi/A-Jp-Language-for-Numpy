/**
 * generated by Xtext 2.25.0
 */
package cn.simbaba.mydsl.jp.impl;

import cn.simbaba.mydsl.jp.JpIndex;
import cn.simbaba.mydsl.jp.JpPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.xtext.xbase.XExpression;

import org.eclipse.xtext.xbase.impl.XExpressionImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Index</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link cn.simbaba.mydsl.jp.impl.JpIndexImpl#getIndexes <em>Indexes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class JpIndexImpl extends XExpressionImpl implements JpIndex
{
  /**
   * The cached value of the '{@link #getIndexes() <em>Indexes</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIndexes()
   * @generated
   * @ordered
   */
  protected EList<XExpression> indexes;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected JpIndexImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return JpPackage.Literals.JP_INDEX;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<XExpression> getIndexes()
  {
    if (indexes == null)
    {
      indexes = new EObjectContainmentEList<XExpression>(XExpression.class, this, JpPackage.JP_INDEX__INDEXES);
    }
    return indexes;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case JpPackage.JP_INDEX__INDEXES:
        return ((InternalEList<?>)getIndexes()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case JpPackage.JP_INDEX__INDEXES:
        return getIndexes();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case JpPackage.JP_INDEX__INDEXES:
        getIndexes().clear();
        getIndexes().addAll((Collection<? extends XExpression>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case JpPackage.JP_INDEX__INDEXES:
        getIndexes().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case JpPackage.JP_INDEX__INDEXES:
        return indexes != null && !indexes.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //JpIndexImpl
