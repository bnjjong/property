/*
 * Created By dogfootmaster@gmail.com on 2020
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>bnjjong</a>
 * @since 2020/12/18
 */

package org.bnjjong.property;

/**
 * create on 2019-08-20.
 * <p> 팩토리 클래스 프로퍼티 구현체를 만들어 준다. </p>
 *
 * @author bnjjong
 * @version 1.0
 * @since 8+
 */
public class PropertyFactory {

  private PropertyFactory() {
    throw new IllegalStateException("use only static method.");
  }

  private static class Singlton {

    private static final PropertyHandler INSTANCE = new PropertyHandlerDefault();
  }

  /**
   * return propertyHandler 인스턴스.
   *
   * @return singleton PropertyHandler 오브젝트
   */
  public static PropertyHandler getInstance() {
    return Singlton.INSTANCE;
  }
}
