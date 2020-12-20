/*
 * Created By dogfootmaster@gmail.com on 2020
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>bnjjong</a>
 * @since 2020/12/18
 */

package org.bnjjong.property;


import lombok.Data;
import lombok.NonNull;
import org.apache.commons.configuration2.Configuration;

/**
 * create on 2019-08-21.
 * <p> 프로퍼티 정보를 관리 하는 클래스 </p>
 *
 * @author jshan
 * @version 1.0
 * @since 8+
 */
@Data
public class PropertyInfo {

  @NonNull
  private String propertyName;

  @NonNull
  private Configuration configuration;

  @NonNull
  private String filePath;
}
