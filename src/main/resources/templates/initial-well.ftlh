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
        align-items: center; /* Центрирование по горизонтали */
        margin: 0; /* Убираем отступы */
        background-color: #f0f8ff; /* Светлый фон */
        padding-top: 20px; /* Отступ сверху */
        min-height: 100vh; /* Занять всю высоту экрана */
    }

    h1, h2 {
        color: #007bff; /* Цвет заголовка */
        margin-bottom: 10px; /* Отступ снизу */
        text-align: center;
    }

    hr {
        width: 100%;
        border: 1px solid #007bff; /* Цвет линии */
        margin: 10px 0; /* Отступы для линии */
    }

    #wellTable {
        width: 100%; /* Ширина таблицы 100% */
        max-width: 800px; /* Максимальная ширина */
        border-collapse: collapse; /* Убираем двойные границы */
        margin: 0 auto; /* Центрируем таблицу */
        font-size: 14px; /* Уменьшаем размер шрифта до 9 */
    }

    #wellTable th,
    #wellTable td {
        padding: 8px; /* Отступы в ячейках */
        border: 1px solid #ccc; /* Цвет границы ячеек */
        text-align: center; /* Центрируем текст в ячейках */
    }

    #wellTable th {
        width: 80px; /* Установите ширину заголовков */
        white-space: normal; /* Позволить переносить текст */
        overflow: visible; /* Сделать переполнение видимым */
        text-overflow: clip; /* Убрать многоточие для переполнения */
        background-color: #007bff; /* Цвет фона заголовка */
        color: white; /* Цвет текста заголовка */
        word-wrap: break-word; /* Перенос слов в заголовках */
    }

    #wellTable td {
        word-wrap: break-word; /* Перенос слов в ячейках */
        max-width: 100px; /* Максимальная ширина ячеек */
    }
    .tool-group {
            display: flex;
            gap: 10px; /* Отступы между кнопками */
            margin-top: 10px;
        }

    input[type="date"] {
            padding: 5px;
            margin: 0 5px;
            border: 1px solid #007bff;
            border-radius: 5px;
        }
    input[type="number"] {
            padding: 5px;
            margin: 0 5px;
            border: 1px solid #007bff;
            border-radius: 5px;
            text-align: center; /* Центрируем текст */
            -moz-appearance: textfield;
        }
    input[type="number"]::-webkit-inner-spin-button,
    input[type="number"]::-webkit-outer-spin-button {
        -webkit-appearance: none; /* Убираем стрелки */
        margin: 0; /* Убираем отступы */
    }

    input[type="button"] {
        background-color: #007bff;
        color: white;
        border: none;

        border-radius: 3px;
        cursor: pointer;
        font-size: 14px;
        width: auto; /* Убираем фиксированную ширину */
    }

    input[type="button"]:hover {
        background-color: #0056b3;
    }
</style>
<script>
    function numberInputHandler(element){
        if (element.value < 50){
            element.value = 50;
        }
        if (element.value > 100000){
            element.value = 100000;
        }
    }
    function updateData(){
        const minDateElement = document.querySelector('input[name="minDate"]');
        const maxDateElement = document.querySelector('input[name="maxDate"]');
        const rowNumber = document.querySelector('input[name="rowNumber"]');
        const nRowsValue = parseInt(rowNumber.value, 10);
        requestData = {
            minDate: minDateElement.value,
            maxDate: maxDateElement.value,
            rowNumbers: nRowsValue,
        }

        fetch('/init/${well.id?c}', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            }).then(response => response.json())
            .then(data => {
                updateTable(data);
            })
    }
    function updateTable(data){

        const tableData = data["data"];

        var tableContent = document.querySelector('table[id="wellTable"]');
        var tbody = tableContent.querySelector('tbody');
        tbody.innerHTML = "";
        data["data"].forEach(item => {
            var row = document.createElement('tr');
            row.innerHTML =
                "<td>" + item.well.name + "</td>" +
                "<td>" + item.strDateTime + "</td>" +
                "<td>" + item.rotationDirection + "</td>" +
                "<td>" + item.frequency + "</td>" +
                "<td>" + item.curPhaseA + "</td>" +
                "<td>" + item.curPhaseB + "</td>" +
                "<td>" + item.curPhaseC + "</td>" +
                "<td>" + item.curPhaseC + "</td>" +
                "<td>" + item.currentImbalance + "</td>" +
                "<td>" + item.lineCurrent + "</td>" +
                "<td>" + item.lineVoltage + "</td>" +
                "<td>" + item.activePower + "</td>" +
                "<td>" + item.totalPower + "</td>" +
                "<td>" + item.powerFactor + "</td>" +
                "<td>" + item.engineLoad + "</td>" +
                "<td>" + item.inputVoltageAB + "</td>" +
                "<td>" + item.inputVoltageBC + "</td>" +
                "<td>" + item.inputVoltageCA + "</td>" +
                "<td>" + item.engineTemp + "</td>" +
                "<td>" + item.engineTemp + "</td>" +
                "<td>" + item.vibrationAccRadial + "</td>" +
                "<td>" + item.vibrationAccAxial + "</td>" +
                "<td>" + item.liquidFlowRatio + "</td>" +
                "<td>" + item.isolationResistance + "</td>";
        tbody.appendChild(row);
        });
    }
</script>
<body>
    <h1>ID: ${well.id}; Имя скважины: ${well.name}; Месторождение: <#if well.field?has_content>${well.field}<#else>м-е отсутствует</#if></h1>
    <h2>Типоразмер: <#if pumpCard.typeSize?has_content>${pumpCard.typeSize}<#else> Нет информации</#if>; Глубина спуска: <#if pumpCard.pumpDepth?has_content>${pumpCard.pumpDepth}<#else> Нет информации</#if>; Последняя дата отказа: <#if pumpCard.lastFailureDate?has_content>${pumpCard.lastFailureDate}<#else> Нет информации</#if></h2>
    <hr>
    <div class="tool-group">
        <input type="date" name="minDate" value="${wellWrapper.minDate}"> | <input type="date" name="maxDate" value="${wellWrapper.maxDate}"> | <input type="number" value="50" name="rowNumber" oninput="numberInputHandler(this)"> | <input type="button" value="Построить" onclick="updateData()">
    </div>
    </br>
    <table id="wellTable">
        <#if indicators?has_content>
            <thead>
                <tr>
                    <th>Well Name</th>
                    <th>Date and Time</th>
                    <th>Rotation Direction</th>
                    <th>Frequency</th>
                    <th>Intake Pressure</th>
                    <th>Current Phase A</th>
                    <th>Current Phase B</th>
                    <th>Current Phase C</th>
                    <th>Current Imbalance</th>
                    <th>Line Current</th>
                    <th>Line Voltage</th>
                    <th>Active Power</th>
                    <th>Total Power</th>
                    <th>Power Factor</th>
                    <th>Engine Load</th>
                    <th>Input Voltage AB</th>
                    <th>Input Voltage BC</th>
                    <th>Input Voltage CA</th>
                    <th>Engine Temperature</th>
                    <th>Liquid Temperature</th>
                    <th>Vibration Acceleration (Radial)</th>
                    <th>Vibration Acceleration (Axial)</th>
                    <th>Liquid Flow Ratio</th>
                    <th>Isolation Resistance</th>
                </tr>
            </thead>
            <#list indicators as i>
                <tr>
                    <td>${i.well.name}</td>
                    <td>${i.strDateTime}</td>
                    <td>${i.rotationDirection}</td>
                    <td>${i.frequency}</td>
                    <td>${i.intakePressure}</td>
                    <td>${i.curPhaseA}</td>
                    <td>${i.curPhaseB}</td>
                    <td>${i.curPhaseC}</td>
                    <td>${i.currentImbalance}</td>
                    <td>${i.lineCurrent}</td>
                    <td>${i.lineVoltage}</td>
                    <td>${i.activePower}</td>
                    <td>${i.totalPower}</td>
                    <td>${i.powerFactor}</td>
                    <td>${i.engineLoad}</td>
                    <td>${i.inputVoltageAB}</td>
                    <td>${i.inputVoltageBC}</td>
                    <td>${i.inputVoltageCA}</td>
                    <td>${i.engineTemp}</td>
                    <td>${i.liquidTemp}</td>
                    <td>${i.vibrationAccRadial}</td>
                    <td>${i.vibrationAccAxial}</td>
                    <td>${i.liquidFlowRatio}</td>
                    <td>${i.isolationResistance}</td>
                </tr>
            </#list>
        </#if>
    </table>
</body>
</html>
