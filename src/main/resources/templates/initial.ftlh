<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Well Manager Web</title>
    <style>
        body {
            display: flex;
            flex-direction: column;
            align-items: center; /* Центрируем по горизонтали */
            justify-content: center; /* Центрируем по вертикали */
            height: 100vh; /* Занять всю высоту окна */
            margin: 0; /* Убираем отступы */
            background-color: #f0f8ff; /* Светлый фон */
        }

        h1 {
            color: #007bff; /* Цвет заголовка */
            margin-bottom: 10px; /* Отступ снизу */
        }

        h3 {
            color: #ff0000; /* Цвет заголовка для отсутствующих данных */
        }

        hr {
            width: 100%;
            border: 1px solid #007bff; /* Цвет линии */
            margin: 10px 0; /* Отступы для линии */
        }

        p {
            margin: 10px 0; /* Отступы для абзацев */
        }
    </style>
</head>
<body>
    <h1>Исходные данные</h1>
    <hr>
    <#list wells as well>
        <p>
            <#if well.field?has_content>
                <b>${well.field}</b>
            <#else>
                <h3>Данных нет</h3>
            </#if>
        </p>
    </#list>
</body>
</html>
