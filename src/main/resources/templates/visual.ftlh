<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
<script src="https://cdn.zingchart.com/zingchart.min.js"></script>

</head>
<body>
    <h1>ID: ${well.id}; Имя скважины: ${well.name}; Месторождение: <#if well.field?has_content>${well.field}<#else>м-е отсутствует</#if></h1>
    <h2>Типоразмер: <#if pumpCard.typeSize?has_content>${pumpCard.typeSize}<#else> Нет информации</#if>; Глубина спуска: <#if pumpCard.pumpDepth?has_content>${pumpCard.pumpDepth}<#else> Нет информации</#if>; Последняя дата отказа: <#if pumpCard.lastFailureDate?has_content>${pumpCard.lastFailureDate}<#else> Нет информации</#if></h2>
    <hr>
    <hr>
    <div id="container">
        <div id="left-panel">
            <input type="date" name="minDate" > | <input type="date" name="maxDate"> <br>
            <h3>Параметр</h3>
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
            'LiquidflowRatio': 'Дебит жидкости',
            'IsolationResistance': 'Сопротивление изоляции'
        };
        const checkboxList = document.getElementById('checkbox-list');
        const parameterNames = Object.keys(parameterMappings);

        checkboxList.addEventListener('change', function(event) {
            const checkbox = event.target;
            const paramName = checkbox.value;

            if (checkbox.checked) {
                updateChart(paramName);
            } else {
                seriesData = seriesData.filter(series => series.text !== paramName);
                var chartData = {
                    type: 'line',
                    scaleX: {
                        label: { text: "Time" },
                        transform: {
                            type: 'date',
                            all: '%d.%M.%Y' // Формат даты
                        },
                        guide: { visible: false },
                        item: { angle: -45 },
                    },
                    scaleY: {
                        label: { text: "Value" }
                    },
                    series: seriesData // Используем обновленные линии данных
                };

                zingchart.render({
                    id: 'chartContainer',
                    data: chartData,
                    height: 400,
                    width: '100%'
                });
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



    function updateChart(col) {
        const minDateInput = document.querySelector('input[name="minDate"]');
        const maxDateInput = document.querySelector('input[name="maxDate"]');

        requestData = {
            minDate: minDateInput.value,
            maxDate: maxDateInput.value,
            parameter: col,
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
                text: requestData["parameter"]
            });

            var chartData = {
                type: 'line',
                    scaleX: {
                    label: { text: "Time" },
                    transform: {
                      type: 'date',
                      all: '%d.%M.%Y' // Формат даты и времени
                    },
                    guide: { visible: false },
                    item: { angle: -45 },
                },
                scaleY: {
                    label: { text: "Value" }
                },
                series: seriesData
            };
            zingchart.render({
              id: 'chartContainer',
              data: chartData,
              height: 400,
              width: '100%'
            });
        });
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return day+"."+month+"."+year;
    }

window.onload = initCheckbox;
</script>
</body>
</html>
