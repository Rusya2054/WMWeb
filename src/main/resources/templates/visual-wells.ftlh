<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
    <style>
        body {
            display: flex;
            flex-direction: column;
            align-items: center; /* Центрируем элементы по горизонтали */
            margin: 0; /* Убираем отступы */
            background-color: #f0f8ff; /* Светлый фон */
            padding-top: 20px; /* Отступ сверху */
        }

        h1 {
            color: #007bff; /* Цвет заголовка */
            margin-bottom: 10px; /* Отступ снизу */
        }

        hr {
            width: 100%;
            border: 1px solid #007bff; /* Цвет линии */
            margin: 10px 0; /* Отступы для линии */
        }

        input[type="button"] {
            background-color: #007bff; /* Цвет кнопки */
            color: white; /* Цвет текста кнопки */
            border: none; /* Убираем границы */
            padding: 10px 20px; /* Отступы внутри кнопки */
            border-radius: 5px; /* Скругленные углы кнопки */
            cursor: pointer; /* Указатель при наведении */
            margin: 10px; /* Отступы между кнопками */
            font-size: 16px; /* Размер шрифта */
        }

        input[type="button"]:hover {
            background-color: #0056b3; /* Цвет кнопки при наведении */
        }
    </style>
<script>

</script>
</head>
<body>
    <h1>Визуализация скважин</h1>
    <hr>
    <#if wellList?has_content>
        <#list wellList as well>
            <div class="well">
                id: ${well.id} | ${well.name} <a href="/visual/${well.id?c}">Визуализация</a> </br>
            </div>
        </#list>
    </#if>
</body>
</html>
