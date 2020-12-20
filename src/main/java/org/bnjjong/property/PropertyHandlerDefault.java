/*
 * Created By dogfootmaster@gmail.com on 2020
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>bnjjong</a>
 * @since 2020/12/18
 */

package org.bnjjong.property;

import java.net.URISyntaxException;
import java.rmi.UnexpectedException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.*;


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
@Slf4j
public class PropertyHandlerDefault implements PropertyHandler {

  private final PropertyMemoryManager propertyMemoryManager = new PropertyMemoryManager();
  private static final String DEFAULT_PATH = "properties/default.properties";
  private static final String DEFAULT_NAME = "default";

  // 외부에서 객체 생성할 수 없다.
  PropertyHandlerDefault() {
    try {
      // default 경로에 프로퍼티가 있는지 먼저 체크
      if (FileUtils.isExistFile(DEFAULT_PATH)) {
        Configuration configuration = this.getConfiguration(DEFAULT_PATH);
        if (configuration != null) {
          PropertyInfo propertyInfo = new PropertyInfo(DEFAULT_NAME, configuration, DEFAULT_PATH);
          propertyMemoryManager.put(propertyInfo);
        }
      }
    } catch (URISyntaxException e) {
    }
  }

  @Override
  public void add(String path) {
    add(DEFAULT_NAME, path);
  }

  @Override
  public void add(String propertyName, String path) {
    Configuration configuration = getConfiguration(path);
    if (configuration == null) {
      throw new IllegalArgumentException("make sure that input path is valid >>>" + path);
    }
    propertyMemoryManager.put(new PropertyInfo(propertyName, configuration, path));
  }

  @Override
  public Configuration get() {
    Configuration configuration = propertyMemoryManager.getConfigration(DEFAULT_NAME);
    if (configuration == null) {
      throw new IllegalStateException(
          "there is no default properties. should set default properties. path is >>>>"
              + DEFAULT_PATH);
    }
    return configuration;
  }

  @Override
  public Configuration get(String propertyName) {
    return propertyMemoryManager.getConfigration(propertyName);
  }

  @Override
  public boolean isExists(String propertyName) {
    return get(propertyName) != null;
  }


  @Override
  public synchronized void reload() {
    MemoryPosition oppositePosition = propertyMemoryManager.getOppositeMemoryPosition();
    List<PropertyInfo> properties = propertyMemoryManager.getProperties();

    //memory clear
    propertyMemoryManager.removeAllByStatus(oppositePosition);

    String propertyName;
    String propertyPath;
    try {
      for (PropertyInfo propertyInfo : properties) {
        propertyName = propertyInfo.getPropertyName();
        propertyPath = propertyInfo.getFilePath();
        Configuration configuration = this.getConfiguration(propertyInfo.getFilePath());
        if (configuration == null) {
          throw new UnexpectedException(
              "unexpected error!!! check this path>>>>" + propertyInfo.getFilePath());
        } else {
          propertyMemoryManager
              .put(oppositePosition, new PropertyInfo(propertyName, configuration, propertyPath));
        }
      }
      propertyMemoryManager.setMemoryPosition(oppositePosition);
    } catch (UnexpectedException e) {

    }
  }


  /**
   * 해당 경로의 프로퍼티 파일을 Configuration 객체로 만들어 준다.
   *
   * @param path 프로퍼티 경로
   * @return 생성된 Configuration 객체
   */
  private Configuration getConfiguration(String path) {
    Parameters params = new Parameters();

    List<FileLocationStrategy> subs = Arrays.asList(new ClasspathLocationStrategy(),
        new FileSystemLocationStrategy()); //new ProvidedURLLocationStrategy(),
    FileLocationStrategy strategy = new CombinedLocationStrategy(subs);

    FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
        PropertiesConfiguration.class).configure(
        params.properties().setFileName(path).setLocationStrategy(strategy)
            .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
    );

    try {
      return builder.getConfiguration();
    } catch (ConfigurationException e) {
//      log.error(LogUtils.getStackErr(e));
    }

    return null;

  }
}
