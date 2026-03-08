Kotlin Gatling Graphite-exporter Prometheus

Gatling loads Kafka by sending messages containing an ID (36 characters), a valid name (full name),
and a valid tax ID. Kafka reads the Spring stub and inserts these messages into the Postgres database.
Metrics are collected using Graphite Exporter and read by Prometheus.

Dashboard files for Spring and Gatling are in Docker files. All dependencies are included in the files.

Important! The Kafka plugin already has a built-in Kafka client, but the client may not download (due to blocking)
a workaround helped).


Gatling подает нагрузку на kafka отправляя сообщения в виде id(36 символов), валидное имя(фио), 
валидный инн.  стаб на спринге читает кафка и вносит эти сообщения в базу постгрес.  Метрики мобираются 
с помощью graphite exporter и читаются прометеусом. 
Файлы дашборд для спринга и гатлинга в докер файлах.  Все зависимости вложены в файлах. 
Важно! В кафка плагин уже встроен кафка клиент, но клиент может не скачаться(из-за блокировок, помог обход)

gatling version 3.11.5
gatling-kafka-plugin_2.13:0.15.1
kotlin("jvm") version 1.9.24



