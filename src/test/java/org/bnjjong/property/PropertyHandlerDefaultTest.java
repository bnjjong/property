/*
 * Created By dogfootmaster@gmail.com on 2020
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>bnjjong</a>
 * @since 2020/12/20
 */

package org.bnjjong.property;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.LineIterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * create on 2020/12/20. create by IntelliJ IDEA.
 *
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author bnjjong
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
@Slf4j
class PropertyHandlerDefaultTest {
  @Test
  @DisplayName("인스턴스 생성")
  public void test1() {
    PropertyHandler instance = PropertyFactory.getInstance();
    Configuration configuration = instance.get();
    assertThat(configuration).isNotNull();
  }

  @Test
  @DisplayName("데이터 조회")
  public void test2() {
    PropertyHandler instance = PropertyFactory.getInstance();
    String value = instance.get().getString("test");
    assertThat(value).isEqualTo("value");

    log.info("value : {}", value);
  }

  @Test
  @DisplayName("json 데이터 조회")
  public void testJson() {
    PropertyHandler instance = PropertyFactory.getInstance();
    String jsonValue = instance.get().getString("jsonValue");
    log.info("json data >>>>>>>>>>>>{}", jsonValue);
    JSONObject jsonObject = JSONObject.fromObject(jsonValue);
    log.info("convert json data >>>>>>>>>{}", jsonObject.toString());
    assertThat(jsonObject.getString("key")).isEqualTo("value");
    assertThat(jsonObject.getString("key2")).isEqualTo("value");

  }

  @Test
  @DisplayName("배열 조회")
  public void test3() {
    PropertyHandler instance = PropertyFactory.getInstance();
    String value = instance.get().getString("test2");
    assertThat(value).isEqualTo("1");

    String[] values = instance.get().getStringArray("test2");
    assertThat(values.length).isEqualTo(5);

    List<String> valueList = instance.get().getList(String.class, "test2");

    for (String str : values) {
      log.info("value : {}", str);
    }

    for (String str : valueList) {
      log.info("value : {}", str);
    }
  }

  @Test
  @DisplayName("배열 조회2")
  public void test4() {
    PropertyHandler instance = PropertyFactory.getInstance();
    String[] values = instance.get().getStringArray("listValue");
    assertThat(values.length).isEqualTo(4);

    List<String> list = instance.get().getList(String.class, "listValue");
    assertThat(list.size()).isEqualTo(4);
    for (String str : list) {
      log.info("value : {}", str);
    }
  }

  @Test
  @DisplayName("프로퍼티 추가 및 조회")
  public void test5() {
    PropertyHandler instance = PropertyFactory.getInstance();
    instance.add("properties/second.properties");
    String secondKey = instance.get().getString("secondKey");
    assertThat(secondKey).isEqualTo("value");

    String value = instance.get().getString("test");
    assertThat(value).isEqualTo("value");
  }

  @Test
  @DisplayName("프로퍼티 존재 여부 체크")
  public void testExists() {
    PropertyHandler instance = PropertyFactory.getInstance();
    instance.add("second","properties/second.properties");
    assertThat(instance.isExists("second")).isTrue();
//    assertThat(instance.isExists("third")).isFalse();
  }



  @Test
  @DisplayName("프로퍼티 다른 네임 등록")
  public void test6() {
    PropertyHandler instance = PropertyFactory.getInstance();
    instance.add("third", "properties/third.properties");

    String thirdKey = instance.get().getString("thirdKey");
    assertThat(thirdKey).isNull();
    log.info("must empty value : {}",  thirdKey);

    thirdKey = instance.get("third").getString("thirdKey");
    assertThat(thirdKey).isEqualTo("value");

    log.info("value : {}",  thirdKey);
  }

  @Test
  @DisplayName("리로드 테스트")
  public void test7() throws IOException, URISyntaxException {
    PropertyHandler instance = PropertyFactory.getInstance();
    String deleteValue = instance.get().getString("deleteValue");
    assertThat(deleteValue).isEqualTo("1");


//    File file = FileUtils.getFile("/Users/bnjjong/IdeaProjects/utils/mobon.utils/src/test/resources/properties/default.properties");
    File file = FileUtils.getFileFromResource("properties/default.properties");
    // 원본 백업
    LineIterator originFile = org.apache.commons.io.FileUtils.lineIterator(file);
    Collection<String> originLine = new ArrayList<>();
    while (originFile.hasNext()) {
      String line = originFile.next();
      originLine.add(line);
      log.info("fileToWrite1 lines: " + line);
    }

    // 라인 추가
    Collection<String> lines = new ArrayList<>();
    lines.add("\naddLine=1");
    org.apache.commons.io.FileUtils.writeLines(file, lines,true);
    LineIterator iter = org.apache.commons.io.FileUtils.lineIterator(file);
    while (iter.hasNext()) {
      log.info("fileToWrite1 lines: " + iter.next());
    }
    String addLine = instance.get().getString("addLine");
    log.info("this must be null!!! >>>> {}", addLine);
    assertThat(addLine).isNull();

    instance.reload();
    log.info("reload!!!!");
    addLine = instance.get().getString("addLine");
    log.info("this must be not null!!! >>>> {}", addLine);
    assertThat(addLine).isEqualTo("1");

    instance.reload();
    assertThat(addLine).isEqualTo("1");

    // 원본으로 다시 복구
    org.apache.commons.io.FileUtils.writeLines(file, originLine);
  }
}