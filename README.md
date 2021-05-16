# Kirin 
## Базовая реализация DI и IoC
## Модуль core
### Основная реализация контекста приложения
- Для того, чтобы создать свой компонент, 
необходимо пометить свой класс аннотацией @Component или любой 
другой из пакета stereotype.
- Для сканирования пакетов на наличие компонентов необходимо
проставить аннотацию @ComponentScan() и указать пути для сканирования
- Для запуска инициализации контекста нужно вызвать метод Application.run():
```
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = Application.run();
    }
}
```

## Модуль logger
### Простая реализация логгера kl4j
Для вызова логгера необходимо вызвать:
```
Klogger.getLogger().log()
```
Например:
```
Klogger.getLogger().log(LogLevel.INFO, "Это информационное сообщение");
```

Логгер имеет свои системные настройки:
```
logger.level=info
logger.level.priority=1
logger.level.color.info=GREEN
logger.level.color.warn=YELLOW
logger.level.color.error=RED

logger.stacktrace.enable=true
logger.stacktrace.priority=2
logger.stacktrace.color=BLUE

logger.date.enable=true
logger.date.pattern=yyyy.MM.dd'T'hh.mm.ss
logger.date.priority=3
logger.date.color=PURPLE
```

Их можно переопределить, создав файл kl4j.properties в папке ресурсов своего проекта
