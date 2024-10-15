<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
</head>
<style>
    body {
        font-family: Arial, sans-serif;
        margin: 20px;
        background-color: #f0f8ff; /* Светлый фон */
    }

    h1 {
        font-size: 24px;
        margin-bottom: 20px;
    }

    hr {
        margin: 20px 0;
        border: 1px solid #007bff;
    }

    button {
        padding: 10px 20px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }

    button:hover {
        background-color: #0056b3;
    }

    form {
        padding: 15px;
        border-radius: 5px;
        border: 1px solid #ddd;
        margin-bottom: 20px;
        width: 350px;
        text-align: left;
        border: 1px solid #007bff;
    }

    label {
        display: block;
        margin-bottom: 10px;
    }

    input[type="radio"] {
        margin-right: 10px;
    }

    b {
        color: #007bff;
    }

    /* Добавляем стили для поля ввода */
    input[type="radio"]:checked + b {
        color: #28a745;
        font-weight: bold;
    }
</style>
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
        fetch('/well-dublicates/upload', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            }).then(response => {
                if (response.redirected){
                    window.location.href = response.url;
                }
            });
    }
</script>
<body>
    <h1>Выбор из соответствующих дубликатов скважин</h1>
    <hr>
    <button onclick="dublicateWellsValidate()">Загрузить данные</button><br><br>

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
