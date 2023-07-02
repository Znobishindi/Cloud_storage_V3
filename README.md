Cloud storage "Облачное хранилище"
=
Описание проекта
==
Задача — разработать REST-сервис. Сервис должен предоставить REST-интерфейс для загрузки файлов и вывода списка уже загруженных файлов пользователя.

Все запросы к сервису должны быть авторизованы. Заранее подготовленное веб-приложение (FRONT) должно подключаться к разработанному сервису без доработок, а также использовать функционал FRONT для авторизации, загрузки и вывода списка файлов пользователя.

## Требования к приложению
* Сервис должен предоставлять REST-интерфейс для интеграции с FRONT.
* Сервис должен реализовывать все методы, описанные в yaml-файле:
* Вывод списка файлов.
* Добавление файла.
* Удаление файла.
* Авторизация.
* Все настройки должны вычитываться из файла настроек (yml).
* Информация о пользователях сервиса (логины для авторизации) и данные должны храниться в базе данных (на выбор студента).
## Требования в реализации
* Приложение разработано с использованием Spring Boot
* Использован сборщик пакетов gradle/maven
* Для запуска используется docker, docker-compose
* Код размещен на github
* Код покрыт unit тестами с использованием mockito



## Описание выполненного проекта
* Реализовано RESTful интерфейс для интеграции с готовым FRONT (Rest controller, слои, валидация)
* Для обработки исключений используется Controller advance c созданием собственного Response. 
* Для логирования применяется интерфейс SLF4j. 
* Код покрыт Unit - тестами, в том числе с использованием Mockito
* В проекте используется база данных MySQL.
* Для миграций используетя Liquibase.
* Используется Spring security, а так же JSON web token (JWT)
* Добавлены настройки для возможности использования в Swagger UI.
## Создание образа и запуск контейнера Docker
* ### Через docker-compose.yaml:
    -  В терминале cобираем jar архив с нашим spring boot приложением: mvn package
    - в терминале и выполнить команду: docker-compose up
  
![0](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_20-32-17.png)
  ## Как проверить?
    - Postman
1. Открываем Postman, вводим URL http://localhost:8090/login , выбираем метод POST и в body заносим:
````
{ "login": "user@test.com", "password": "zxcvb12345"}
````
![1](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_20-01-04.png "Получили токен")
2. В случае ввода валидных данных в ответ нам придет auth-token. Его мы копируем и помещаем в Authorization:

![2](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_20-03-36.png "вставили токен")
3. Теперь, когда авторизованному пользователю выдан токен доступа, ему доступны эндпоинты, некоторых из которых потестим через Swagger.


   - Swagger

1. Проходим по ссылке http://localhost:8090/swagger-ui/index.html#/Cloud/downloadFile

![3](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_19-52-20.png)

2. Выбираем метод POST с эндпоинтом /login, а дальше по аналогии с Postman вводим тело запроса  и кликаем Execute:

![4](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_19-53-43.png "")

3. В ответ нам приходит auth-token:

![5](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_19-54-47.png)

4. Копируем полученный токен, кликаем Authorize и вставляем его в поле Value.

![6](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_19-55-47.png)

5. С авторизацией разобрались, теперь попробуем загрузить файл в облачное хранилище. Выбираем метод POST с эндпоинтом /file, вводим новое имя файла и выбираем его из жесткого диска.

![7](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_19-57-30.png)

6. Теперь давайте убедимся, что загруженный файл в хранилище появился. Для этого выбираем метод GET c эндпоинтом /file  и вводим имя искомого файла.

![8](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_19-59-29.png)


# Финал

1. Переходим по ссылке http://localhost:8080/ и вводим почту user@test.com и пароль zxcvb12345

![9](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_20-27-35.png)

2. Мы вошли! Нажимаем "Добавть" и выбираем файл.

![10](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_20-28-18.png)

3. Все наши выбранные файлы перед нами. Мы можем изменить их имя, скачать на жесткий диск, а так же удалить из хранилища.

![11](https://github.com/Znobishindi/Cloud_service/blob/main/screenshots/2023-07-02_20-29-44.png)

