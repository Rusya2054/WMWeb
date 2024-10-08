<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Менеджер данных</title>
</head>
<style>
    body {
        font-family: Arial, sans-serif; /* Шрифт для всего тела */
        margin: 0; /* Убираем отступы */
        padding: 0; /* Убираем внутренние отступы */
    }

    .tab-container {
        display: flex; /* Используем flexbox для горизонтального выравнивания */
        border-bottom: 1px solid #ccc; /* Разделительная линия между вкладками и содержимым */
        overflow-x: auto; /* Позволяет прокручивать вкладки, если они не помещаются в окно */
    }

    .tab {
        padding: 10px 20px; /* Отступы для вкладок */
        cursor: pointer; /* Указатель при наведении */
        border: 1px solid transparent; /* Скрываем границу по умолчанию */
        border-bottom: none; /* Убираем нижнюю границу для вкладок */
        background-color: #f1f1f1; /* Цвет фона вкладок */
        margin-right: 5px; /* Отступ между вкладками */
        white-space: nowrap; /* Не переносить текст на новую строку */
    }

    .tab:hover {
        background-color: #ddd; /* Цвет фона при наведении */
    }

    .active {
        font-weight: bold; /* Выделение активной вкладки */
        background-color: white; /* Цвет фона активной вкладки */
        border: 1px solid #ccc; /* Граница для активной вкладки */
        border-bottom: none; /* Убираем нижнюю границу для активной вкладки */
    }

    .tab-content {
        display: none; /* Скрываем содержимое всех вкладок по умолчанию */
        padding: 15px; /* Отступ для содержимого */
        border: 1px solid #ccc; /* Граница содержимого */
    }
</style>

<script>
    // Функция для обновления данных при изменении разделителя
    function updateSep() {
        const separator = document.getElementById('data-splitter').value;
        const sessionID = "${sessionID}";



        const data = {
                sessionID: sessionID,
                separator: separator
            };


        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/pump-card/upload/parse-indicators", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onload = function () {
            if (xhr.status === 200) {
                updateData();
                // document.getElementById("response").innerHTML = xhr.responseText;
            }
        };
        xhr.send(JSON.stringify(data));
    }

    function updateData(){
        const separator = "${separator}";
        var filesMap = {
            <#list uploadedParsedIndicatorsFiles?keys as key>
                "${key}": [
                    <#list uploadedParsedIndicatorsFiles[key] as value>
                        "${value}"<#if value_has_next>,</#if>
                    </#list>
                ]<#if key_has_next>,</#if>
            </#list>
        };
        console.log(filesMap)
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

                let tableHtml = '<table border="1"><thead><tr>';

                headerRow.forEach(function(header) {
                    tableHtml += "<th>"+header+"</th>";
                });

                for (let i = 1; i < 10; i++) {
                    const rowData = filesMap[key][i].split(separator);
                    tableHtml += "<tr>";
                    rowData.forEach(function(column) {
                        tableHtml += "<td>"+column+"</td>";
                        console.log(column);
                    });
                    tableHtml += "</tr>";
                }
                tabContent.innerHTML = tableHtml;
            }else {
                // Если данных нет, выводим сообщение
                tabContent.innerHTML = "<p>Нет данных для отображения.</p>";
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


</script>


<body>
    ${sessionID}
    <h2>Менеджер данных</h2>
    <table>
        <tr>
            <th scope="col">Разделительный знак</th>
            <th scope="col">Имя скважины</th>
            <td>
                <input type="button" value="загрузить">
            </td>
        </tr>
        <tr>
            <td>
                <select name="data-splitter" id="data-splitter" onchange="updateSep()">
                    <option value="\t" selected>Табуляция</option>
                    <option value=" ">Пробел</option>
                    <option value=".">Точка</option>
                    <option value=",">Запятая</option>
                    <option value=";">Точка с запятой</option>
                </select>
            </td>
            <td>
                <label>
                    <input type="checkbox" name="option" value="option1" onclick="toggleCheckbox(this)"> По имени файла
                </label><br>
                <label>
                    <input type="checkbox" name="option" value="option2" onclick="toggleCheckbox(this)"> По столбцу файла
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
                <table border="1">
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
                                        <td>${column}</td>
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
