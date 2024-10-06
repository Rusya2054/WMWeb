<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Менеджер данных</title>
</head>
<script>
    // Функция для обновления данных при изменении разделителя
    function updateData() {
        const separator = document.getElementById('data-splitter').value;

        const data = ['value1', 'value2'];

        fetch('/pump-card/upload/parse-indicators', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ strings: data, sep: separator }),
            }).then(response => {
                console.log('Ответ сервера:', response);
            }).catch(error => {
                console.error('Ошибка:', error);
            });
    }
</script>


<body>
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
                <select name="data-splitter" id="data-splitter" onchange="updateData()">
                    <option value="\t">Табуляция</option>
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
    <table id="data-table" border="1">
        <thead>
            <tr>
                <th>File Name</th>
                <th>Value 1</th>
                <th>Value 2</th>
                <th>Value 3</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>

    <script>

        const data = JSON.parse('${uploadedIndicatorsFiles}');

        // Получаем доступ к tbody таблицы
        const tableBody = document.querySelector('#data-table tbody');

        // Проходим по ключам объекта
        for (const key in data) {
            if (data.hasOwnProperty(key)) {
                // Создаем новую строку таблицы
                const row = document.createElement('tr');

                // Создаем ячейку для ключа (имя файла)
                const fileNameCell = document.createElement('td');
                fileNameCell.textContent = key;
                row.appendChild(fileNameCell);

                // Создаем ячейки для значений массива
                data[key].forEach(value => {
                    const valueCell = document.createElement('td');
                    valueCell.textContent = value;
                    row.appendChild(valueCell);
                });

                // Добавляем строку в тело таблицы
                tableBody.appendChild(row);
            }
        }
    </script>
</body>
</html>
