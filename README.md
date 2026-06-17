# RoiseClans

Полнофункциональный плагин системы кланов для Minecraft 1.21.1

## Возможности

- ✅ Система кланов с лидерами и заместителями
- ✅ MySQL база данных
- ✅ PlaceholderAPI поддержка
- ✅ Интеграция с системой убийств/смертей
- ✅ Рейтинг кланов
- ✅ Приватный клановый чат
- ✅ PvP режимы

## Установка

1. Скачайте плагин
2. Поместите JAR файл в папку `plugins/`
3. Перезагрузите сервер
4. Отредактируйте `config.yml` с параметрами MySQL
5. Перезагрузите сервер заново

## Команды

```
/clan help - Показывает список команд
/clan create {имя} - Создать клан
/clan delete - Удалить клан
/clan info {клан} - Информация о клане
/clan invite {игрок} - Пригласить игрока в клан
/clan accept {клан} - Принять запрос в клан
/clan decline {клан} - Отклонить запрос в клан
/clan kick {игрок} - Кикнуть игрока
/clan leave - Покинуть клан
/clan member {игрок} - Информация об игроке
/clan pvp - Изменить режим PvP в клане
/clan top {kills/level} - Топ кланов
/clan chat {сообщение} - Написать в клановый чат
/clan setZam {игрок} - Установить заместителя
/clan removeZam - Убрать заместителя
/clan setLeader {игрок} - Установить создателя клана
```

## Плейсхолдеры (PlaceholderAPI)

```
%riseclans_pvp_status% - Статус PvP клана
%riseclans_clan_name% - Имя клана
%riseclans_clan_kills% - Убийства клана
%riseclans_clan_deaths% - Смерти клана
%riseclans_clan_level% - Уровень клана
%riseclans_clan_name_colored% - Имя клана с цветом
%riseclans_clan_color% - Цвет клана
```

## Конфигурация

Отредактируйте `plugins/RiseClans/config.yml`:

```yaml
mysql:
  host: localhost
  port: 3306
  database: riseclans
  username: root
  password: password
```

## Поддержка

При возникновении проблем создавайте Issue на GitHub.
