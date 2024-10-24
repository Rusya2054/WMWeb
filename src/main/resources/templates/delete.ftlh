<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
</head>
<style>
    body {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-start;
        height: 100vh;
        margin: 0;
        background-color: #f0f8ff;
        padding-top: 20px;
    }

    h1 {
        margin-bottom: 10px;
        color: #007bff;
    }

    hr {
        width: 100%;
        border: 1px solid #007bff;
        margin: 10px 0;
    }

    input[type="text"] {
        margin: 10px 0;
        padding: 8px;
        width: 300px;
        border-radius: 5px;
        border: 1px solid #007bff;
    }

    .button-group {
        display: flex;
        gap: 10px; /* Отступы между кнопками */
        margin-top: 10px;
    }

    input[type="button"] {
        background-color: #007bff;
        color: white;
        border: none;
        padding: 10px 20px;
        border-radius: 5px;
        cursor: pointer;
        font-size: 16px;
        width: auto; /* Убираем фиксированную ширину */
    }

    input[type="button"]:hover {
        background-color: #0056b3;
    }

    #exportList {
        margin-top: 20px;
        width: 100%;
        max-width: 800px;
    }

    div[name="wellInfo"] {
        padding: 10px;
        margin-bottom: 10px;
        background-color: white;
        border-radius: 8px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }

    input[type="date"] {
        padding: 5px;
        margin: 0 5px;
        border: 1px solid #007bff;
        border-radius: 5px;
    }

    input[type="checkbox"] {
        margin-left: 10px;
    }
</style>
<script>
    function selectButtonHandler(){
        const checkboxes = document.querySelectorAll('input[type="checkbox"][name="option"][value="option"]');
        checkboxes.forEach((checkbox) => {
            checkbox.checked = true;
        });
    }

    function setInterval(value){
        const blocks = document.querySelectorAll('div[name="wellInfo"]');
        blocks.forEach((element) => {
            const minDateInput = element.querySelector('input[name="minDate"]');
            const maxDateInput = element.querySelector('input[name="maxDate"]');

            const newMinDate = new Date(maxDateInput.value);
            newMinDate.setMonth(newMinDate.getMonth() - value);
            const formattedDate = newMinDate.toISOString().split('T')[0];
            minDateInput.value = formattedDate;
        });
    }

    function deleteData(){
        const blocks = Array.from(document.querySelectorAll('div[name="wellInfo"]'));
        const filteredBlocks = blocks.filter((block) => {
            const checkbox = block.querySelector('input[type="checkbox"][name="option"]');
            return checkbox && checkbox.checked;
        });
        if (filteredBlocks.length > 0){
            const requestData = {};
            filteredBlocks.forEach((element) => {
                const data = {};
                data["minDate"] = element.querySelector('input[name="minDate"]').value;
                data["maxDate"] = element.querySelector('input[name="maxDate"]').value;
                requestData[element.id] = data;
            });
            fetch('/well/delete-data', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            }).then(response => {
                if (response.ok){
                    window.location.href = response.url;
                } else {
                    console.error('Ошибка при удалении');
                }
            });
        }
    }
</script>
<body>
    <h1>Удаление скважин</h1>
    <hr>
    <div class="button-group">
        <input type="button" value="1 месяц" onclick="setInterval(1)"> | <input type="button" value="3 месяца" onclick="setInterval(3)"> | <input type="button" value="6 месяцев" onclick="setInterval(6)"> <br>
        <input type="button" value="Выбрать все" onclick="selectButtonHandler()"> <br>
        <input type="button" value="Удалить" onclick="deleteData()"><br>
    </div>
    <br>
    <#if wellWrapperList?has_content>
        <#list wellWrapperList as well>
            <div id="${well.id?c}" name="wellInfo">
                ${well.id} |
                <#if well.field?has_content>
                        <b>${well.field}</b>
                    <#else>
                        <b>м-е отсутствует</b>
                </#if> | ${well.name} | <input type="date" name="minDate" value="${well.minDate}"> | <input type="date" name="maxDate" value="${well.maxDate}"> <input type="checkbox" name="option" value="option"> <br>
            </body>
        </#list>
    </#if>
</body>
</html>
