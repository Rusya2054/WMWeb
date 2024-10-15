<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
</head>
<script>

    function dublicateWellsValidate(){
        const requestData = {}
        const blocks = document.querySelectorAll('form');

        blocks.forEach((block) => {
            const selected_input = block.querySelector('input[name="option"]:checked')
            const locData = {
                id: selected_input.id,
                name: selected_input.value,
                sessionID: "${sessionID?c}",
                separator: "${separator}",
            }
            requestData[block.name] = locData;
        });
        console.log(requestData);
        fetch('/well-dublicates/upload', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            });
    }

</script>
<body>
    <h1>Выбор из соответствующих дубликатов скважин</h1>
    <hr>
    <button onclick="dublicateWellsValidate()">Загрузить данные</button><br>


    <#list duplicateWellMap?keys as wells>
        <#if duplicateWellMap[wells]?has_content>
            <form name="${wells}">
                    <#list duplicateWellMap[wells] as well>
                        <label>
                            <input type="radio"  name="option" value="${duplicateWellMap[wells][0].name}" id="${well.id}"> ${well.id} | ${well.name} |
                                <#if well.field?has_content>
                                    <b>${well.field}</b>
                                <#else>
                                    <b>м-е отсутствует</b>
                                </#if><br>
                        </label>
                    </#list>
                    <label>
                        <input type="radio" name="option" value="${duplicateWellMap[wells][0].name}" id="-1" checked="checked"> new id | ${duplicateWellMap[wells][0].name} | new field<br>
                    </label>
            </form>
            <br><br>
        </#if>
    </#list>
</body>
</html>
