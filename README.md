

# PropertyHandler

## 목적
> pojo Project시 손쉽게 프로퍼티를 설정할 수 있도록 패키징

## 개발 스펙
- Jdk 14
- Gradle 6.7
- Apache configuration2 2.7

## class diagram
![MemoryPosition](https://user-images.githubusercontent.com/44669620/102716379-e67d8e00-431e-11eb-85f2-dc9b7256713a.png)


## 기본 규칙
기본적으로 `/default.properties` 파일을 import 한다.
내부적으로 `apache common configuration` 라이브러리를 이용 한다.

## build 하기

```shell
# java version 체크 하기
$ java --version
openjdk 14.0.2 2020-07-14
OpenJDK Runtime Environment AdoptOpenJDK (build 14.0.2+12)
Eclipse OpenJ9 VM AdoptOpenJDK (build openj9-0.21.0, JRE 14 Mac OS X amd64-64-Bit Compressed References 20200715_143 (JIT enabled, AOT enabled)
OpenJ9   - 34cf4c075
OMR      - 113e54219
JCL      - 1d231bd6a2 based on jdk-14.0.2+12)

# build 하기
$ ./gradlew build

# build 파일 확인
$ cd build/libs
$ ll
total 8224
drwxr-xr-x@ 4 jordan  staff   128B Jan 18 19:29 .
drwxr-xr-x@ 9 jordan  staff   288B Jan 18 19:29 ..
-rw-r--r--  1 jordan  staff   3.2M Jan 18 19:27 property-lib-1.1-jdk-14-all.jar
-rw-r--r--  1 jordan  staff   9.9K Jan 18 19:27 property-lib-1.1-jdk-14.jar
```

## 프로젝트에 import 하기
 nexus가 있다면 해당 jar 파일을 업로드 하여 사용 하면 되고 nexus 환경이 없다면 프로젝트 폴더에 복사한 뒤 아래처럼 `import` 할 수 있다.

ex) `gradle`
```groovy
// 외부 jar import
dependencies {
    ...
    compile files("libs/property-lib-1.1-jdk-14-all.jar")
    ...
}
```


## instance 생성
default로 해당 경로의 프로퍼티를 읽는다. 
`properties/default.properties`
```java
PropertyHandler instance = PropertyFactory.getInstance();
Configuration configuration = instance.get();
```

default 파일이 없다면 등록 해주어야 한다.
```java
PropertyHandler instance = PropertyFactory.getInstance();
instance.add("properties/second.properties");
Configuration configuration = instance.get();
```

scope를 다르게 관리하고 싶다면 `name`을 지정해주자.
```java
PropertyHandler instance = PropertyFactory.getInstance();
instance.add("thirdProperty", "properties/third.properties");
Configuration configuration = instance.get("thirdProperty");
```

## Configuration 기본적인 사용 법
```java
Configuration configuration = instance.get();
String value = configuration.getString("key");
int number = instance.get().getInt("number");
boolean isValidate = instance.get().getBoolean("isValidate");
```

배열 처리 기본적으로 `,`(콤마)로 구분하도록 되어 있다.

`default.properties`
```
names=john,smith,george,jordan
```
```java
Configuration configuration = instance.get();
String[] names = configuration.getStringArray("names");
List<String> nameList = configuration.getList(String.class, "names");
```

또는 key를 중복하여 선언하는 방법이 있다.

`default.properties`
```
name=john
name=smith
name=george
name=jordan
```

```java
Configuration configuration = instance.get();
String[] names = configuration.getStringArray("name");
List<String> nameList = configuration.getList(String.class, "name");
```

## 리로드 하기
서버내에 프로퍼티 파일을 서버 중단 없이 리로딩 할 수 있다.
reload 메소드 자체는 동기화로 처리 됨.
내부적으로는 프로퍼티 정보를 새로 메모리에 올리고 마직마에 메모리 포지션을 변경하여 멀티쓰레딩 환경에서 발생할 수 있는 에러를 최소화 함.
```java
PropertyFactory.getInstance().reload();
```






