# Автотесты на страницу https://mcdonalds.ru/

### Пример списка тестов в Allure TestOps

![Allure TestOps](./img/Allure_TestOps.png)

### Пример прохождения тестов в Allure TestOps

![Allure TestOps](./img/Allure_Reports.png)

### Используемые параметры по умолчанию

* browser (default chrome)
* browserVersion (default 89.0)
* browserSize (default 1920x1080)
* browserMobileView (mobile device name, for example iPhone X)
* remoteDriverUrl (url address from selenoid or grid)
* videoStorage (url address where you should get video)
* threads (number of threads)

![Allure TestOps](./img/Jenkins.png)

Run tests with filled remote.properties:

```bash
gradle clean test
```

Run tests with not filled remote.properties:

```bash
gradle clean -DremoteDriverUrl=https://user1:1234@selenoid.autotests.cloud/wd/hub/ -DvideoStorage=https://selenoid.autotests.cloud/video/ -Dthreads=1 test
```

Serve report:

```bash
allure serve build/allure-results
```

### Оповещение о результатах прохождения тестов через бот в телеграмме

![Allure TestOps](img/Telegram.png)

#### Перейти в телеграмм канал можно [по ссылке](https://t.me/joinchat/sMZ0AnmoWmVhNDVi)

### Анализ результатов в Jenkins через Allure Reports

![Allure TestOps](./img/Jenkins_Allure_Reports.png)

#### Посмотреть сборки в Jenkins можно [по ссылке](https://jenkins.autotests.cloud/job/07-antonina1901-it_one/)

### Анализ результатов в Allure TestOps

![Allure TestOps](./img/Allure_TestOps_Results.png)

### Пример прохождения тестов можно посмотреть на видео

![Allure TestOps](./img/video.gif)