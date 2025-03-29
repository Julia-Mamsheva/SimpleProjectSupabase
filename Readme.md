<h1 align="center">Простой пример использования Supabase</h1>

## Функицонал приложения

*   Кастомный загрузочный экран с анимацией
*   Авторизация пользователя в системе по почте и паролю
*   Регистрация пользователя в системе по почте и паролю, с занесением данных в таблицу "profile" (имя не пустое, фамилия не пустое, дата рождения, изображение)
*   Вывод списка книг из базы данных, с возможностью поиска по наимнование и описанию, и фильтрацие по категории
*   Редактирование и удаление книги в базе данных

## Создание приложения
1.  Добавление архитектуры приложения:
  *  Папка Domain (хранение и обработка бизнес-логики, хранение данных)
      * Директория State (Создоваемые классы могут использоваться для сохранения состояния форм между различными событиями (например, поворотом экрана))
          *  BookState - это эффективный способ структурировать и управлять данными, связанными с процессом редактированием/удалением книги, что приводит к более чистому, читаемому и поддерживаемому коду
  *  Папка Presentation (отображение и управление интерфейса пользователя)
      * Директория Screens
          *  Components
              * TextField - добавление кастомного textField для редактирования атрибутов книги, и для выпадающего спсика категории
          *  DetailBook - вся инфорамция о книге    
              * DetailsBookScreen -   отвечает за отображение подробной информации о книге и обработку событий на экране
              * DetailsBookViewModel - viewModel для управления состоянием и логикой отображения, редактирования и удаления книги
2. Добавление записей в файлы приложения:
   * Директория drawable - добавление картинок используемых в приложении
       * NavigationRoutes - имя для навигация
       * NavHost - опеределение навигации для перехода на полную информации о книге, с передачей id книги
       * MainScreen - переход на подробную информацию о книге
