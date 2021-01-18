/*
 * Created By dogfootmaster@gmail.com on 2020
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>bnjjong</a>
 * @since 2020/12/18
 */

package org.bnjjong.property;

import org.apache.commons.configuration2.Configuration;

/**
 * create on 2019-07-26.
 * <p> Apache common configutation 을 이용한 property handler class.</p>
 *
 * @author bnjjong
 * @version 1.0
 * @see <a href="apache commons document">https://commons.apache.org/proper/commons-configuration/index.html</a>
 * @see Configuration
 * @since 8+
 */
public interface PropertyHandler {


  /**
   * 해당 경로의 프로퍼티를 default 프로퍼티 객체에 append 한다. 이때 중복된 키가 있으면 덮어 쓸 수 있으니 주의가 필요 함. 경로가 올바르지 않을경우
   * <code>IllegalArgumentException</code>이 발생한다.
   *
   * @param path properties 파일 경로
   * @throws IllegalArgumentException 경로가 올바르지 않을 경우
   */
  void add(String path);

  /**
   * <p>해당 경로의 properties 파일을 프로퍼티명으로 등록 한다.</p>
   * {@link #get(String)} 을 통해서 해당 정보를 가져올 수 있다.
   *
   * @param propertyName 식별이 가능한 프로퍼티 명
   * @param path         프로퍼티 경로
   * @throws IllegalArgumentException 경로가 올바르지 않을 경우
   */
  void add(String propertyName, String path);

  /**
   * default로 등록을 했거나 {@link #add(String)} 이용하여등록하였다면 정상적으로 Configuration 객체를 반환 함.
   *
   * @return 등록된 프로퍼티 또는 properties/default.properties에 있는 configuration 객체를 반환.
   * @throws IllegalStateException default로 등록된 프로퍼티가 없거나 등록된 프로퍼티가 없는데 호출할 경우
   */
  Configuration get();

  /**
   * <p>프로퍼티 명으로 등록된 Configuration 객체를 리턴 함</p>
   * {@link #add(String, String)}을 통해서 등록된 객체가 반드시 존재해야 함.
   *
   * @param propertyName 유니크한 프로퍼티 명
   * @return 등록된 Configuration 객체
   * @see #add(String, String)
   */
  Configuration get(String propertyName);

  /**
   * 해당 프로퍼티가 존재 하는지 여부를 체크.
   * @param propertyName 등록된 프로퍼티 명
   * @return 존재하면 {@code true}, 존재 하지 않으면 {@code false}
   */
  boolean isExists(String propertyName);


  /**
   * <pre>
   * 기존에 등록된 프로퍼티를 리로딩 한다. 이때 해당 메소드는 동기화 처리 되어 있으므로 연속된 호출에 주의 해야 한다.
   * 내부적으로 알수 없는 이유로 에러가 발생할 경우 reload를 하지 않는다. 로그를 출력하게 해놓았으므로 확인이 가능하다.
   * </pre>
   */
  void reload();

}
