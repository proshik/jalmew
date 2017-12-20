Решения о загрузке файлов *.yml

Если для приложении включен @EnableDiscoveryClient, тогда по умолчанию будут грузиться свойства из файла bootstrap.yml

Способы это перекрыть:
----------------------

1. Надо запустить сервис с флагом: -Dspring.cloud.config.enabled=false (по умолчанию там true)
и создать файл application.yml в /src/main/resource, в котором описать свойства. 

Долнительно можно создать файл application-dev.yml в /src/main/resource и запускать еще и в dev профиле через
-Dspring.profiles.active=dev

2. Расширив содержимое файла bootstrap.yml, описав его как указано ниже. 
(Важным тут является строка с "---", разделяющая профили):

spring:
  application:
    name: ytranslate-service
  cloud:
    config:
      uri: http://config:8888
      fail-fast: true
      password: password
      username: user

---
spring:
  profiles: dev
  cloud:
    config:
      enabled: false
      
тогда при запуске сервиса с флагом -Dspring.profiles.active=dev настройки будут браться из файла application.yml,
но если в директории /src/main/resource есть файл со свойствами, соответствующий имени профиля, 
тогда будет загрузаться он, например: application-dev.yml.

При этом можно еще для некоторых свойств грузить значения через тот же classpath'
-Dyandex.client.dict.key=testKey

3. -Dspring.cloud.config.enabled=false -Dspring.cloud.config.discovery.enabled=false