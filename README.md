# gazprom_parser
Парсер тендеров с сайта https://etpgaz.gazprombank.ru/#com/procedure/index , реализовано с помощью Seleniumа. Парсятся архивные тендеры. Сделан простенький интерфейс с возможностью задать ключевые слова (можно несколько слов с любым разделителем), даты подведения итогов, имя файла. Сохраняется все в Excel таблицу, на рабочий стол (позже изменю на папку проекта/программы). В случае кодга в тендере несколько лотов - каждый записывается в отдельную строку, если лотов больше 10 - добавляется строка с текстом "больше 10 лотов", ибо реализовать проход по ним не удалось. В силу особенностей сайта, лучше не задавать сразу большие объемы тендеров, до 2-3 тысяч парсится более или менее стабильно, потом либо перестают грузится скрипты на сайте, либо программа не может найти/нажать нужную кнопку. При сбое предложит сохранить промежуточный результат.

# Установка
Пользователи Windows могут не грузить весь проект, в папке target есть .exe файл. Тем не менее, для запуска необходимы jre (с прописанным JAVAPATH) и Chrome драйвер для селениума

# P.S.
Первая серьезная прога, писалось всё в сжатые сроки, код очень грязный. Надеюсь, дойдут руки привести все в нормальный вид и доделать некоторые вещи.
