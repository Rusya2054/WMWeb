<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
</head>
<body>
    <h1>Выбор из соответствующих дубликатов скважин</h1>
    <hr>
    ${sessionID?c}
    <#list duplicateWellList as wells>
        <#list wells as well>
            ${well}
        </#list>
    </#list>
</body>
</html>
