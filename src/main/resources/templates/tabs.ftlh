<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Менеджер данных</title>
</head>
<style>
    body {
        font-family: Arial, sans-serif; /* Шрифт */
        background-color: #f0f8ff; /* Цвет фона */
        margin: 0; /* Убираем отступы */
        padding: 20px; /* Отступы внутри */
    }

    h2 {
        color: #007bff; /* Цвет заголовка */
        margin-bottom: 20px; /* Отступ снизу */
    }
    #tabTables {
        width: 100%; /* Ширина таблицы 100% */
        max-width: 800px; /* Максимальная ширина */
        border-collapse: collapse; /* Убираем двойные границы */
        margin: 0 auto; /* Центрируем таблицу */
        font-size: 9px; /* Уменьшаем размер шрифта до 9 */
    }

    #tabTables th,
    #tabTables td {
        padding: 8px; /* Отступы в ячейках */
        border: 1px solid #ccc; /* Цвет границы ячеек */
        text-align: center; /* Центрируем текст в ячейках */
    }

    #tabTables th {
        width: 80px; /* Установите ширину заголовков */
        white-space: normal; /* Позволить переносить текст */
        overflow: visible; /* Сделать переполнение видимым */
        text-overflow: clip; /* Убрать многоточие для переполнения */
        background-color: #007bff; /* Цвет фона заголовка */
        color: white; /* Цвет текста заголовка */
        word-wrap: break-word; /* Перенос слов в заголовках */
    }

    #tabTables td {
        word-wrap: break-word; /* Перенос слов в ячейках */
        max-width: 100px; /* Максимальная ширина ячеек */
    }

    #configTable {
        width: 100%; /* Ширина таблицы */
        border-collapse: collapse; /* Убираем пробелы между ячейками */
        margin-bottom: 20px; /* Отступ снизу */
    }

    #configTable th, #configTable td {
        border: 1px solid #007bff; /* Цвет границ */
        text-align: center; /* Выравнивание по центру */
    }

    #configTable th {
        background-color: #007bff; /* Цвет фона заголовка */
        color: white; /* Цвет текста заголовка */
    }

    .tab-container {
        display: flex; /* Отображение в строку */
        margin-bottom: 10px; /* Отступ снизу */
    }

    .tab {
        padding: 10px 20px; /* Отступы внутри вкладки */
        cursor: pointer; /* Курсор при наведении */
        background-color: #e0e0e0; /* Цвет фона вкладки */
        margin-right: 5px; /* Отступ между вкладками */
        border-radius: 5px; /* Скругление углов */
    }

    .tab:hover {
        background-color: #c0c0c0; /* Цвет фона при наведении */
    }

    .tab-content {
        display: none; /* Скрываем содержимое вкладок по умолчанию */
        margin-top: 10px; /* Отступ сверху для контента */
    }

    .tab.active {
        background-color: #007bff; /* Цвет фона для активной вкладки */
        color: white; /* Цвет текста для активной вкладки (по желанию) */
    }

    .tab-content.active {
        display: block; /* Показываем активный контент */
    }

</style>
<script>
    function updateSep() {
        const separator = document.getElementById('data-splitter').value;
        const sessionID = "${sessionID}";

        const requestData = {
                sessionID: sessionID,
                separator: separator
            };


        fetch('/pump-card/upload/parse-indicators', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
        .then(response => response.json())
        .then(data => {
            updateData(data);
        })
    }

    function updateData(data){
        const separator = data["separator"];
        var filesMap = data["uploadedParsedIndicatorsFiles"];

        var tabContents = document.querySelectorAll('.tab-content');
        tabContents.forEach(function(tabContent) {
            // Очищаем содержимое вкладки
            tabContent.innerHTML = "";
        });

        for (var key in filesMap){
            // const values = filesMap[key];
            const tabContent = document.getElementById("tab_" + key);

            if (filesMap[key].length > 0){
                const headerRow = filesMap[key][0].split(separator);

                let tableHtml = '<table border="1" style="width: 100%; border-collapse: collapse;">';
                tableHtml += '<thead><tr>';

                headerRow.forEach(function(header) {
                    tableHtml += "<th style='text-align: center; word-wrap: break-word;'>" + header + "</th>";
                });

                for (let i = 1; i < 10; i++) {
                    const rowData = filesMap[key][i].split(separator);
                    tableHtml += "<tr>";
                    rowData.forEach(function(column) {
                        tableHtml += "<td style='padding: 8px; text-align: center; word-wrap: break-word;'>" + column + "</td>";
                        // console.log(column);
                    });
                    tableHtml += "</tr>";
                }
                tabContent.innerHTML = tableHtml;
            }else {
                // Если данных нет, выводим сообщение
                tabContent.innerHTML = "<p>"+"Нет данных для отображения."+"</p>";
            }
        }
    }

    function openTab(tabId, event) {
            var i, tabContent, tabLinks;

            // Скрываем все содержимое вкладок
            tabContent = document.getElementsByClassName("tab-content");
            for (i = 0; i < tabContent.length; i++) {
                tabContent[i].style.display = "none";
            }

            // Убираем класс "active" со всех вкладок
            tabLinks = document.getElementsByClassName("tab");
            for (i = 0; i < tabLinks.length; i++) {
                tabLinks[i].classList.remove("active");
            }

            // Показываем выбранную вкладку
            document.getElementById(tabId).style.display = "block";
            event.currentTarget.classList.add("active");
        }

    function wellNameBoxValidator(checkbox){
        const option1 = document.querySelector('input[name="option"][value="option1"]');
        const option2 = document.querySelector('input[name="option"][value="option2"]');

        if (checkbox.value == "option1"){
            if (checkbox.checked){
                option1.checked = true;
                option2.checked = false;
            } else{
                option1.checked = false;
                option2.checked = true;
            }

        } else {
            if (checkbox.checked){
                option1.checked = false;
                option2.checked = true;
            } else{
                option1.checked = true;
                option2.checked = false;
            }
        }
    }
    function uploadToDb(){
        const separator = document.getElementById('data-splitter').value;
        const sessionID = "${sessionID}";
        const byFileName = document.querySelector('input[name="option"][value="option1"]').checked

        const requestData = {
                sessionID: sessionID,
                separator: separator,
                byFileName: byFileName
            };


        fetch('/pump-card/upload/indicators', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
        .then(response => {
            if (response.redirected){
                window.location.href = response.url;
            }
        })
    }

</script>


<body>
    <h2>Менеджер данных</h2>
    <table id="configTable">
        <tr>
            <th scope="col">Разделительный знак</th>
            <th scope="col">Имя скважины</th>
            <td>
                <input type="button" value="Загрузить" onclick=uploadToDb()>
            </td>
        </tr>
        <tr>
            <td>
                <select name="data-splitter" id="data-splitter" onchange="updateSep()">
                    <option value="\t" selected>Табуляция</option>
                    <option value=" ">Пробел</option>
                    <option value=",">Запятая</option>
                    <option value=";">Точка с запятой</option>
                </select>
            </td>
            <td>
                <label>
                    <input type="checkbox" name="option" value="option1" checked="true" onclick="wellNameBoxValidator(this)"> По имени файла
                </label><br>
                <label>
                    <input type="checkbox" name="option" value="option2" onclick="wellNameBoxValidator(this)"> По столбцу файла
                </label>
            </td>
            <td>

            </td>
        </tr>
    </table>
    <div class="tab-container">
        <#list uploadedParsedIndicatorsFiles?keys as key>
            <div class="tab" onclick="openTab('tab_${key}', event)">${key}</div>
        </#list>
    </div>

    <#list uploadedParsedIndicatorsFiles?keys as key>
        <div id="tab_${key}" class="tab-content">
            <#if (uploadedParsedIndicatorsFiles[key]?size > 0)>
                <#assign headerRow = uploadedParsedIndicatorsFiles[key][0]?split("${separator}") />
                <table border="1" id="tabTables">
                    <thead>
                        <tr>
                            <#list headerRow as header>
                                <th>${header}</th>
                            </#list>
                        </tr>
                    </thead>
                    <tbody>
                        <#list 1..9 as i>
                            <#if i < uploadedParsedIndicatorsFiles[key]?size>
                                <tr>
                                    <#assign rowData = uploadedParsedIndicatorsFiles[key][i]?split("${separator}") />
                                    <#list rowData as column>
                                        <td style='padding: 8px; text-align: center; word-wrap: break-word;'>${column}</td>
                                    </#list>
                                </tr>
                            </#if>
                        </#list>
                    </tbody>
                </table>
            <#else>
                <p>Нет данных для отображения.</p>
            </#if>
        </div>
    </#list>

</body>
</html>
