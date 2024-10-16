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
        align-items: center; /* Центрируем по горизонтали */
        justify-content: flex-start; /* Выравниваем по верхнему краю */
        height: 100vh;
        margin: 0;
        background-color: #f0f8ff; /* Светлый фон */
        padding-top: 20px; /* Отступ сверху */
    }

    h1 {
        margin-bottom: 10px; /* Отступы для заголовка */
        color: #007bff; /* Цвет заголовка */
    }

    @keyframes fadeIn {
            to {
                opacity: 1; /* Конечная непрозрачность */
            }
        }

    button {
        background-color: #007bff; /* Цвет кнопки */
        color: white; /* Цвет текста кнопки */
        border: none; /* Убрать границу */
        padding: 10px 20px; /* Отступы внутри кнопки */
        border-radius: 5px; /* Скругленные углы кнопки */
        cursor: pointer; /* Курсор при наведении */
        width: 100%; /* Полная ширина для кнопки */
    }

    button:hover {
        background-color: #0056b3; /* Цвет кнопки при наведении */
    }

</style>
<script>
    function exportFilterHandler(){
        const wellFilter = document.querySelector('input[id="wellFilter"]');
        const fieldFilter = document.querySelector('input[id="fieldFilter"]');
        console.log(wellFilter.value)
        console.log(fieldFilter.value)
        const blocks = document.querySelectorAll('div[name="wellInfo"]');
        blocks.forEach(block => {
            const htmlContent = block.innerHTML;
            const cleanText = htmlContent.replace(/<[^>]*>/g, '');
            const parts = cleanText.split('|').map(part => part.trim());
            console.log(parts);
        });
    }

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

    function exportData(){
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
            fetch('/export/files', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            }).then(response => {
                return response.json();
            }).then(data => {
                for (let key in data) {
                    const blob = new Blob([data[key]], { type: 'text/plain' }); // Создаем Blob из строки
                    const url = window.URL.createObjectURL(blob); // Создаем URL для Blob
                    const a = document.createElement('a'); // Создаем ссылку
                    a.href = url; // Присваиваем URL
                    a.download = key; // Имя файла для сохранения
                    document.body.appendChild(a); // Добавляем ссылку в документ
                    a.click(); // Программно кликаем на ссылку
                    a.remove(); // Убираем ссылку после скачивания
                    window.URL.revokeObjectURL(url); // Освобождаем URL
                }
            });
        }

    }

</script>
<body>
    <h1>Экспорт</h1>
    <hr>
        Фильтр по месторождению <input type="text" id="fieldFilter" oninput="exportFilterHandler()"><br>
        Фильтр по имени скважины <input type="text" id="wellFilter" oninput="exportFilterHandler()"><br>
    <br><br>
    <input type="button" value="1 месяц" onclick="setInterval(1)"> | <input type="button" value="3 месяца" onclick="setInterval(3)"> | <input type="button" value="6 месяцев" onclick="setInterval(6)"> <br>
    <input type="button" value="Выбрать все" onclick="selectButtonHandler()"> <br>
    <input type="button" value="Экспорт" onclick="exportData()"><br>
    <br>
    <#if wells?has_content>
        <#list wells as well>
            <div id="${well.id?c}" name="wellInfo">
                ${well.id} |
                <#if well.field?has_content>
                        <b>${well.field}</b>
                    <#else>
                        <b>м-е отсутствует</b>
                </#if> | ${well.name} | <input type="date" name="minDate" value="${well.minDate}"> | <input type="date" name="maxDate" value="${well.maxDate}"> <input type="checkbox" name="option" value="option"> <br>
            </div>
        </#list>
    </#if>

</body>
</html>
