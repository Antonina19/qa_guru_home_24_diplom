# Автотесты на страницу https://mcdonalds.ru/

![Intelij_IDEA](img/icons/Intelij_IDEA.png)![Java](img/icons/Java.png)![Selenide](img/icons/Selenide.png)![Selenoid](img/icons/Selenoid.png)![Gradle](img/icons/Gradle.png)![JUnit5](img/icons/JUnit5.png)![Allure Report](img/icons/Allure_Report.png)![AllureTestOps](img/icons/AllureTestOps.png)![Github](img/icons/Github.png)![Jenkins](img/icons/Jenkins.png)![Rest-Assured](img/icons/Rest-Assured.png)![Telegram](img/icons/Telegram.png)![Jira](img/icons/Jira.png)

##### Посмотреть сборки в Jenkins можно [по ссылке](https://jenkins.autotests.cloud/job/07-antonina1901-mcdonalds/)

##### Посмотреть проект в Allure TestOps можно [по ссылке](https://allure.autotests.cloud/project/564/dashboards)

##### Перейти в телеграмм канал можно [по ссылке](https://t.me/joinchat/sMZ0AnmoWmVhNDVi)

##### Перейти в Jira можно [по ссылке](https://jira.autotests.cloud/browse/HOMEWORK-256)

### Реализованы проверки:

**UI**

* Отрытие страниц меню
* Проверка, информации о руководстве компании
* Проверка, что открывается страница 'Франчайзинг'
* Открытие окна информации о продукте
* Проверка, что по-умолчанию корзина пустая

**API**

* Проверяем, что добавленный товар отображается в корзине
* Проверяем, что добавленный товар удаляется из корзины
* Получаем информацию о продукте 'Цезарь-ролл'
* Получаем информацию о названии промо-акций
* Получаем информацию о бургерах и их цене

### Пример списка тестов в Allure TestOps

![Allure TestOps](./img/Allure_TestOps.png)

### Пример прохождения тестов в Allure TestOps

![Allure Reports](./img/Allure_Reports.png)

### Используемые параметры по умолчанию

* browser (default chrome)
* browserVersion (default 89.0)
* browserSize (default 1920x1080)
* browserMobileView (mobile device name, for example iPhone X)
* remoteDriverUrl (url address from selenoid or grid)
* videoStorage (url address where you should get video)
* threads (number of threads)

![Jenkins](./img/Jenkins.png)

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
annotations serve build/annotations-results
```

### Анализ результатов в Jenkins через Allure Reports

![Jenkins_Allure_Reports](./img/Jenkins_Allure_Reports.png)

### Результаты запусков Launches в Allure Reports

![Allure_Launches](./img/Allure_Launches.png)

### Анализ результатов в Allure TestOps

![Allure TestOps_Results](./img/Allure_TestOps_Results.png)

### Основной Dashboards

![Allure Dashboards](img/Allure_Dashboards.png)

### Dashboards по разным типам тестов

![Allure Dashboards_Automation](./img/Allure_Dashboards_Automation.png)

### Dashboards по команде

![Allure Dashboards_Team](./img/Allure_Dashboards_Team.png)

### Интеграция с Jira

![Jira](./img/Jira.png)

### Оповещение о результатах прохождения тестов через бот в телеграмме

![Telegram](img/Telegram.png)

### Пример прохождения тестов можно посмотреть на видео

![video](./img/video.gif)