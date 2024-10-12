<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Менеджер данных</title>
    <style>
        body {
            display: flex;
            flex-direction: column;
            align-items: center; /* Центрируем по горизонтали */
            justify-content: flex-start; /* Выравниваем по верхнему краю */
            height: 100vh;
            margin: 0;
            background-color: #f0f8ff; /* Светлый фон */
            padding-top: 20px; /* Отступ сверху */
        }

        h1 {
            margin-bottom: 10px; /* Отступы для заголовка */
            color: #007bff; /* Цвет заголовка */
        }

        hr {
            width: 100%;
            border: 1px solid #007bff; /* Цвет линии */
            margin: 10px 0; /* Отступы для линии */
        }

        p {
            margin: 10px 0; /* Отступы для абзацев */
        }

        a {
            color: #007bff; /* Цвет ссылок */
            text-decoration: none; /* Убираем подчеркивание */
        }

        a:hover {
            text-decoration: underline; /* Подчеркивание при наведении */
        }

        form {
            background-color: white; /* Цвет фона формы */
            padding: 20px;
            border-radius: 8px; /* Скругленные углы */
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Тень */
            opacity: 0; /* Начальная непрозрачность для анимации */
            animation: fadeIn 1s forwards; /* Анимация появления */
            margin-top: 20px; /* Отступ сверху */
            width: 300px; /* Ширина формы */
        }

        @keyframes fadeIn {
            to {
                opacity: 1; /* Конечная непрозрачность */
            }
        }

        input[type="file"] {
            margin: 10px 0; /* Отступы для поля выбора файла */
            width: 100%; /* Полная ширина для поля выбора файла */
        }

        button {
            background-color: #007bff; /* Цвет кнопки */
            color: white; /* Цвет текста кнопки */
            border: none; /* Убрать границу */
            padding: 10px 20px; /* Отступы внутри кнопки */
            border-radius: 5px; /* Скругленные углы кнопки */
            cursor: pointer; /* Курсор при наведении */
            width: 100%; /* Полная ширина для кнопки */
        }

        button:hover {
            background-color: #0056b3; /* Цвет кнопки при наведении */
        }
    </style>
    <script>
        function wellListInit() {
            console.log("123");
        }

        window.onload = wellListInit; // Исправлено: убрано лишнее выполнение функции
    </script>
</head>
<body>
    <h1>Менеджер данных</h1>
    <hr>
    <#list wellList as well>
        <p>
            <#if well.field?has_content>
                <b>${well.field}</b>
            <#else>
                <b>м-е отсутствует</b>
            </#if>
            | ${well.name} | <a href="/well/${well.id}">Подробнее</a>
        </p>
    </#list>
    <h1>Выберите файлы</h1>
    <form action="/pump-card/upload" method="post" enctype="multipart/form-data">
        <input type="file" name="files[]" multiple>
        <button type="submit">Загрузить файлы</button>
    </form>
</body>
</html>
