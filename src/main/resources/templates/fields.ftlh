<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
    <script>
        function toExport(field){
            requestData = {field: field};
            fetch('/export', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            }).then(response => {
                if (response.ok){
                    window.location.href = response.url;
                } else {
                    console.error('Ошибка при переходе');
                }
            });
        }

        function toInit(field){
            requestData = {field: field};
            fetch('/init', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            }).then(response => {
                if (response.ok){
                    window.location.href = response.url;
                } else {
                    console.error('Ошибка при переходе');
                }
            });
        }

    </script>
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

        button {
            background-color: #007bff; /* Цвет кнопки */
            color: white; /* Цвет текста кнопки */
            border: none; /* Убрать границу */
            padding: 10px 20px; /* Отступы внутри кнопки */
            border-radius: 5px; /* Скругленные углы кнопки */
            cursor: pointer; /* Курсор при наведении */
            width: 15%; /* Полная ширина для кнопки */
        }

        button:hover {
            background-color: #0056b3; /* Цвет кнопки при наведении */
        }
    </style>
</head>
<body>
    <h1>Месторождения</h1>
    <hr>
    <#if from?has_content && from == "export">
        <#if fields?has_content>
            <#list fields as field>
                <#if field?has_content>
                        <button onclick="toExport('${field}')">${field}</button><br>
                </#if>
            </#list>
        </#if>
    </#if>
    <#if from?has_content && from == "delete">
        <#if fields?has_content>
            <#list fields as field>
                <a href="toDelete(${field})">${field}</a>
            </#list>
        </#if>
    </#if>
    <#if from?has_content && from == "init">
        <#if fields?has_content>
            <#list fields as field>
                <#if field?has_content>
                        <button onclick="toInit('${field}')">${field}</button><br>
                </#if>
            </#list>
        </#if>
    </#if>
</body>
</html>
