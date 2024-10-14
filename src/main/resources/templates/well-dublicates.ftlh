<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
</head>
<body>
    <h1>Выбор из соответствующих дубликатов скважин</h1>
    <hr>

    <#list duplicateWellList as wells>
        <form>
            <#list wells as well>
                <label>
                    <input type="radio" name="option" value="1" > ${well.id} | ${well.name} |
                        <#if well.field?has_content> <b>${well.field}</b>
                            <#else>
                            <b>м-е отсутствует</b>
                        </#if><br>
                </label>
            </#list>
            <label>
                <input type="radio" name="option" value="0" checked="checked"> new id | new name | new field<br>
            </label>
        </form>
        <br><br>
    </#list>
</body>
</html>
