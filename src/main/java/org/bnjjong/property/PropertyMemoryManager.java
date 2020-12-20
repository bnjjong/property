/*
 * Created By dogfootmaster@gmail.com on 2020
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>bnjjong</a>
 * @since 2020/12/18
 */

package org.bnjjong.property;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration2.Configuration;

/**
 * create on 2019-08-20.
 * <p> Property 정보를 관리 및 CRUD를 지원하는 클래스 </p>
 *
 * @author jshan
 * @version 1.0
 * @since 8+
 */
public class PropertyMemoryManager {

  // 상태 값
  private Map<MemoryPosition, List<PropertyInfo>> propertyInfoMap = new EnumMap<>(MemoryPosition.class);
  private Map<MemoryPosition, Map<String, Configuration>> configurationMap = new EnumMap<>(
      MemoryPosition.class);
  private MemoryPosition currentMemoryPosition = MemoryPosition.A;

  /**
   * 생성자 각 맵을 초기화 한다.
   */
  public PropertyMemoryManager() {
    // 초기화
    // 초기화
    propertyInfoMap.put(MemoryPosition.A, new ArrayList<>());
    propertyInfoMap.put(MemoryPosition.B, new ArrayList<>());
    configurationMap.put(MemoryPosition.A, new HashMap<>());
    configurationMap.put(MemoryPosition.B, new HashMap<>());
  }

  /**
   * 현재 메모리 위치에 property 정보를 추가 한다.
   *
   * @param propertyInfo 프로퍼티 정보
   */
  public void put(PropertyInfo propertyInfo) {
    put(currentMemoryPosition, propertyInfo);
  }

  /**
   * 입력한 메모리 위치에 데이터를 추가 한다.
   *
   * @param memoryPosition 추가할 메모리 위치
   * @param propertyInfo   프로퍼티 정보
   */
  public void put(MemoryPosition memoryPosition, PropertyInfo propertyInfo) {
    String propertyName = propertyInfo.getPropertyName();
    Configuration configuration = propertyInfo.getConfiguration();

    // put propertyInfo
    this.propertyInfoMap.get(memoryPosition).add(propertyInfo);
    Map<String, Configuration> currentConfigurationMap = this.configurationMap.get(memoryPosition);

    // put configration
    if (currentConfigurationMap.containsKey(propertyName)) {
      Configuration originConfiguration = currentConfigurationMap.get(propertyName);
      Iterator<String> keys = configuration.getKeys();
      String key;
      while (keys.hasNext()) {
        key = keys.next();
        originConfiguration.addProperty(key, configuration.getProperty(key));
      }
    } else {
      currentConfigurationMap.put(propertyName, configuration);
    }
  }

  /**
   * Configration을 조회 한다.
   *
   * @param propertyName property 명
   * @return propertyName과 매칭된 Configration 객체
   */
  public Configuration getConfigration(String propertyName) {
    return getConfigration(currentMemoryPosition, propertyName);
  }

  /**
   * 입력된 메모리위치의 Configration을 조회 한다.
   *
   * @param memoryPosition 메모리 위치
   * @param propertyName   property 명
   * @return 메모리 위치에서 propertyName과 매칭된 Configration 객체
   */
  public Configuration getConfigration(MemoryPosition memoryPosition, String propertyName) {
    return this.configurationMap.get(memoryPosition).get(propertyName);
  }

  /**
   * 현재 메모리 위치에서 추가된 property 정보 전체를 리턴한다.
   *
   * @return 현재 메모리위치의 property 정보전체
   */
  public List<PropertyInfo> getProperties() {
    return getProperties(currentMemoryPosition);
  }

  /**
   * 원하는 메모리 위치의 property 정보를 리턴한다.
   *
   * @param memoryPosition 조회할 메모리 위치 정보
   * @return 프로퍼티 정보 리스트 객체
   */
  public List<PropertyInfo> getProperties(MemoryPosition memoryPosition) {
    return propertyInfoMap.get(memoryPosition);
  }


  /**
   * 해당 메모리 위치의 모든 데이터를 삭제 한다.
   *
   * @param memoryPosition 삭제할 메모리 위치
   */
  public synchronized void removeAllByStatus(MemoryPosition memoryPosition) {
    // 초기화
    propertyInfoMap.put(memoryPosition, new ArrayList<>());
  }

  /**
   * 메모리 위치를 변경 한다.
   *
   * @param memoryPosition 변경할 메모리 위치
   */
  public void setMemoryPosition(MemoryPosition memoryPosition) {
    this.currentMemoryPosition = memoryPosition;
  }

  /**
   * 현재 메모리 위치를 리턴 함.
   *
   * @return 현재 사용중인 메모리 위치를 반환
   */
  public MemoryPosition getMemoryPosition() {
    return this.currentMemoryPosition;
  }

  /**
   * 현재 메모리 포지션의 반대 값을 리턴한다. A->B, B->A
   *
   * @return 현재 사용중인 메모리의 반대 값
   */
  public MemoryPosition getOppositeMemoryPosition() {
    return this.currentMemoryPosition == MemoryPosition.A ? MemoryPosition.B : MemoryPosition.A;
  }
}
