/**
 * generated by Xtext 2.25.0
 */
package cn.simbaba.mydsl.jp.impl;

import cn.simbaba.mydsl.jp.JpPackage;
import cn.simbaba.mydsl.jp.JpWithOpenAs;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XFeatureCall;

import org.eclipse.xtext.xbase.impl.XExpressionImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>With Open As</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link cn.simbaba.mydsl.jp.impl.JpWithOpenAsImpl#getOpen <em>Open</em>}</li>
 *   <li>{@link cn.simbaba.mydsl.jp.impl.JpWithOpenAsImpl#getName <em>Name</em>}</li>
 *   <li>{@link cn.simbaba.mydsl.jp.impl.JpWithOpenAsImpl#getWithExpression <em>With Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class JpWithOpenAsImpl extends XExpressionImpl implements JpWithOpenAs
{
  /**
   * The cached value of the '{@link #getOpen() <em>Open</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOpen()
   * @generated
   * @ordered
   */
  protected XFeatureCall open;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getWithExpression() <em>With Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWithExpression()
   * @generated
   * @ordered
   */
  protected XExpression withExpression;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected JpWithOpenAsImpl()
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
    return JpPackage.Literals.JP_WITH_OPEN_AS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public XFeatureCall getOpen()
  {
    return open;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOpen(XFeatureCall newOpen, NotificationChain msgs)
  {
    XFeatureCall oldOpen = open;
    open = newOpen;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JpPackage.JP_WITH_OPEN_AS__OPEN, oldOpen, newOpen);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setOpen(XFeatureCall newOpen)
  {
    if (newOpen != open)
    {
      NotificationChain msgs = null;
      if (open != null)
        msgs = ((InternalEObject)open).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JpPackage.JP_WITH_OPEN_AS__OPEN, null, msgs);
      if (newOpen != null)
        msgs = ((InternalEObject)newOpen).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JpPackage.JP_WITH_OPEN_AS__OPEN, null, msgs);
      msgs = basicSetOpen(newOpen, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JpPackage.JP_WITH_OPEN_AS__OPEN, newOpen, newOpen));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JpPackage.JP_WITH_OPEN_AS__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public XExpression getWithExpression()
  {
    return withExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetWithExpression(XExpression newWithExpression, NotificationChain msgs)
  {
    XExpression oldWithExpression = withExpression;
    withExpression = newWithExpression;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JpPackage.JP_WITH_OPEN_AS__WITH_EXPRESSION, oldWithExpression, newWithExpression);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setWithExpression(XExpression newWithExpression)
  {
    if (newWithExpression != withExpression)
    {
      NotificationChain msgs = null;
      if (withExpression != null)
        msgs = ((InternalEObject)withExpression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JpPackage.JP_WITH_OPEN_AS__WITH_EXPRESSION, null, msgs);
      if (newWithExpression != null)
        msgs = ((InternalEObject)newWithExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JpPackage.JP_WITH_OPEN_AS__WITH_EXPRESSION, null, msgs);
      msgs = basicSetWithExpression(newWithExpression, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JpPackage.JP_WITH_OPEN_AS__WITH_EXPRESSION, newWithExpression, newWithExpression));
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
      case JpPackage.JP_WITH_OPEN_AS__OPEN:
        return basicSetOpen(null, msgs);
      case JpPackage.JP_WITH_OPEN_AS__WITH_EXPRESSION:
        return basicSetWithExpression(null, msgs);
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
      case JpPackage.JP_WITH_OPEN_AS__OPEN:
        return getOpen();
      case JpPackage.JP_WITH_OPEN_AS__NAME:
        return getName();
      case JpPackage.JP_WITH_OPEN_AS__WITH_EXPRESSION:
        return getWithExpression();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case JpPackage.JP_WITH_OPEN_AS__OPEN:
        setOpen((XFeatureCall)newValue);
        return;
      case JpPackage.JP_WITH_OPEN_AS__NAME:
        setName((String)newValue);
        return;
      case JpPackage.JP_WITH_OPEN_AS__WITH_EXPRESSION:
        setWithExpression((XExpression)newValue);
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
      case JpPackage.JP_WITH_OPEN_AS__OPEN:
        setOpen((XFeatureCall)null);
        return;
      case JpPackage.JP_WITH_OPEN_AS__NAME:
        setName(NAME_EDEFAULT);
        return;
      case JpPackage.JP_WITH_OPEN_AS__WITH_EXPRESSION:
        setWithExpression((XExpression)null);
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
      case JpPackage.JP_WITH_OPEN_AS__OPEN:
        return open != null;
      case JpPackage.JP_WITH_OPEN_AS__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case JpPackage.JP_WITH_OPEN_AS__WITH_EXPRESSION:
        return withExpression != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //JpWithOpenAsImpl
