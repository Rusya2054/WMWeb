<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
<script src="https://cdn.zingchart.com/zingchart.min.js"></script>
<style>
    body {
        background-color: #f0f8ff; /* Светлый фон */
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
    #container {
        display: flex;
        height: 100vh; /* Высота контейнера на всю высоту экрана */
    }
    #left-panel {
        width: 20%; /* Левая панель займет 20% ширины */
        padding: 20px;
        box-sizing: border-box;
        background-color: #f0f8ff; /* Светлый фон */
    }

    #right-panel {
        width: 80%; /* Правая панель займет 80% ширины */
        padding: 20px;
        box-sizing: border-box;
    }

    #chartContainer {
        width: 100%;
        height: 100%; /* Займет всю доступную высоту и ширину родителя */
        background-color: #f0f8ff; /* Светлый фон */
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
</head>
<body>
    <h1>ID: ${well.id}; Имя скважины: ${well.name}; Месторождение: <#if well.field?has_content>${well.field}<#else>м-е отсутствует</#if></h1>
    <h2>Типоразмер: <#if pumpCard.typeSize?has_content>${pumpCard.typeSize}<#else> Нет информации</#if>;
    Глубина спуска: <#if pumpCard.pumpDepth?has_content>${pumpCard.pumpDepth}<#else> Нет информации</#if>;
Последняя дата отказа: <#if pumpCard.lastFailureDate?has_content>${pumpCard.lastFailureDate}<#else> Нет информации</#if>
<#if repairInterval?has_content>; МРП: ${repairInterval?c} суток</#if>
</h2>
    <hr>
    <div id="container">
        <div id="left-panel" class="tool-group">
            с <input type="date" name="minDate"><br><br>
            до <input type="date" name="maxDate"> <br><br>
            от <input type="number" id="minValue" placeholder="минимальное значение" oninput="axeOYHandler()"> <br><br>
            до <input type="number" id="maxValue" placeholder="максимальное значение" oninput="axeOYHandler()"> <br>
            <br>
            <input type="button" value="Очистить параметры" onclick="сlearParameters()"><br>
            <dib id="text">
                Параметры
            </dib><br>
            <div id="checkbox-list"></div>
        </div>
        <div id="right-panel">
            <div id="chartContainer"></div>
        </div>
    </div>
<script>
    let seriesData = [];
    let selectedParams = [];
    function initCheckbox(){
        plotGraph([]);
        const parameterMappings = {
            'Frequency': 'Частота вращения',
            'CurPhaseA': 'Ток фазы A',
            'CurPhaseB': 'Ток фазы B',
            'CurPhaseC': 'Ток фазы C',
            'CurrentImbalance': 'Дисбаланс токов',
            'LineCurrent': 'Линейный ток (ток сети)',
            'LineVoltage': 'Линейное напряжение (напряжение сети)',
            'ActivePower': 'Активная мощность',
            'EngineLoad': 'Загрузка ПЭД',
            'InputVoltageAB': 'Напряжения AB',
            'InputVoltageBC': 'Напряжения BC',
            'InputVoltageCA': 'Напряжения CA',
            'IntakePressure': 'Давление на приёме',
            'EngineTemp': 'Температура ПЭД',
            'LiquidTemp': 'Температура жидкости',
            'VibrationAccRadial': 'Радиальные вибрации',
            'VibrationAccAxial': 'Осевые вибрации',
            'LiquidFlowRatio': 'Дебит жидкости',
            'IsolationResistance': 'Сопротивление изоляции'
        };
        const checkboxList = document.getElementById('checkbox-list');
        const parameterNames = Object.keys(parameterMappings);

        checkboxList.addEventListener('change', function(event) {
            const checkbox = event.target;
            const paramName = checkbox.value;

            if (checkbox.checked) {
                updateChart(paramName, parameterMappings[paramName]);
            } else {
                seriesData = seriesData.filter(series => series.text !== parameterMappings[paramName]);
                plotGraph(seriesData);
            }

        });
        parameterNames.forEach(param => {
                const label = document.createElement('label');
                label.classList.add('checkbox-label');

                const checkbox = document.createElement('input');
                checkbox.setAttribute('type', 'checkbox');
                checkbox.setAttribute('value', param);

                label.appendChild(checkbox);
                label.appendChild(document.createTextNode(parameterMappings[param]));

                checkboxList.appendChild(label);
                checkboxList.appendChild(document.createElement('br'));
        });
    }



    const chartContainer = document.getElementById('chartContainer');
    zingchart.render({
        id: 'chartContainer',
        data: {
            type: 'line',
            series: []
        }
    });

    function updateChart(col, ruCol) {
        const minDateInput = document.querySelector('input[name="minDate"]');
        const maxDateInput = document.querySelector('input[name="maxDate"]');

        requestData = {
            minDate: minDateInput.value,
            maxDate: maxDateInput.value,
            parameter: col,
            ruParameter: ruCol
        }

        fetch('/visual/${well.id}', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        }).then(response => {
            return response.json();
        }).then(data=>{
            const valuesArray = Object.keys(data).map(key => [formatDate(key), data[key]]);
            seriesData.push({
                values: valuesArray,
                text: requestData["ruParameter"]
            });
            plotGraph(seriesData);
        });
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return date.getTime();
    }

    function сlearParameters() {
        const checkboxList = document.getElementById('checkbox-list');
        const checkboxes = checkboxList.querySelectorAll('input[type="checkbox"]');
        checkboxes.forEach((checkbox) => {
            checkbox.checked = false;
        });
        seriesData = [];
        plotGraph(seriesData);
        zingchart.exec('chartContainer', 'clear');
    }

    function plotGraph(inputData, min, max){
        const config = {
            type: 'line',
            backgroundColor: 'transparent',
            plotarea: {
                backgroundColor: 'transparent'
            },
            series: inputData,
            legend: {
                visible: true,
                backgroundColor: 'transparent',
                borderWidth: 1,
                borderColor: 'black',
                item: {
                    fontColor: 'black'
                }
            },
            scaleX: {
                lineColor: '#000000',
                lineWidth: 1,
                transform: {
                    type: 'date',
                    all: '%dd.%mm.%y %H:%i:%s',
                    itemsOverlap: false,
                },
                zooming: true,
                item: {
                    bold: true,
                    fontColor: '#000000',
                    fontSize: 10
                },
                guide: {
                    lineStyle: 'solid',
                    lineColor: '#000000',
                }
            },
            scaleY: {
                lineColor: '#000000',
                lineWidth: 1,
                zooming: true,
                item: {
                    bold: true,
                    fontColor: '#000000',
                    fontSize: 10
                },
                guide: {
                  lineStyle: 'solid',
                  lineColor: '#000000',
                }
            },
            tooltip: {
                text: '%t: %v'
            }
        };

        if (min !== undefined || max !== undefined) {
            config.scaleY = {
                zooming: true,
            };
            if (min !== undefined) {
                config.scaleY.minValue = min;
            }
            if (max !== undefined) {
                config.scaleY.maxValue = max;
            }
        }

         zingchart.render({
            id: 'chartContainer',
            data: config
        });
    }

    function axeOYHandler() {
        const minValueNumber = Number(document.getElementById('minValue').value);
        const maxValueNumber = Number(document.getElementById('maxValue').value);
        if (minValueNumber == "" && maxValueNumber == ""){
            plotGraph(seriesData);
        }

        if (typeof minValueNumber === 'number' && !isNaN(minValueNumber) && typeof maxValueNumber === 'number' && !isNaN(maxValueNumber)){
            if (minValueNumber < maxValueNumber){
                plotGraph(seriesData, minValueNumber, maxValueNumber);
            }
        }
    }
window.onload = initCheckbox;
</script>
</body>
</html>
