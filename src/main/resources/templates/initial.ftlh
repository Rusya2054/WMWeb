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
        margin: 0; /* Убираем отступы */
        background-color: #f0f8ff; /* Светлый фон */
        padding-top: 20px; /* Отступ сверху */
        min-height: 100vh; /* Занимаем всю высоту экрана */
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

    .content {
        width: 100%;
        max-width: 800px; /* Максимальная ширина контента */
        text-align: center; /* Выравнивание текста по центру */
        margin-top: 0; /* Убираем верхние отступы */
        padding: 20px 0; /* Отступы для контента */
    }
</style>
</head>
<body>
    <h1>Исходные данные</h1>
    <hr>
    <div id="wellList">
        <#if wellListMap?has_content>
            <#list wellListMap?keys as wells>
                <div class="wellListInfo">
                    <#list wellListMap[wells] as well>
                        <div id="${well.id?c}" name="wellInfo">
                            ${well.id} |
                            <#if well.field?has_content>
                                    <b>${well.field}</b>
                                <#else>
                                    <b>м-е отсутствует</b>
                            </#if> | ${well.name} | <a href="/init/${well.id?c}"> Подробнее</a> <br>
                        </div>
                    </#list>
                </div>
                </br></br>
            </#list>
        </#if>
    </div>
</body>
</html>
