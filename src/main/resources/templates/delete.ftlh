<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
</head>
<script>
    function selectButtonHandler(){
        const checkboxes = document.querySelectorAll('input[type="checkbox"][name="option"][value="option"]');
        console.log(checkboxes);
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
    <input type="button" value="1 месяц" onclick="setInterval(1)"> | <input type="button" value="3 месяца" onclick="setInterval(3)"> | <input type="button" value="6 месяцев" onclick="setInterval(6)"> <br>
    <input type="button" value="Выбрать все" onclick="selectButtonHandler()"> <br>
    <input type="button" value="Удалить" onclick="deleteData()"><br>
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
