<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
    <style>
        body {
            display: flex;
            flex-direction: column;
            align-items: center; /* Центрируем элементы по горизонтали */
            margin: 0; /* Убираем отступы */
            background-color: #f0f8ff; /* Светлый фон */
            padding-top: 20px; /* Отступ сверху */
        }

        h1 {
            color: #007bff; /* Цвет заголовка */
            margin-bottom: 10px; /* Отступ снизу */
        }

        hr {
            width: 100%;
            border: 1px solid #007bff; /* Цвет линии */
            margin: 10px 0; /* Отступы для линии */
        }

        input[type="button"] {
            background-color: #007bff; /* Цвет кнопки */
            color: white; /* Цвет текста кнопки */
            border: none; /* Убираем границы */
            padding: 10px 20px; /* Отступы внутри кнопки */
            border-radius: 5px; /* Скругленные углы кнопки */
            cursor: pointer; /* Указатель при наведении */
            margin: 10px; /* Отступы между кнопками */
            font-size: 16px; /* Размер шрифта */
        }

        input[type="button"]:hover {
            background-color: #0056b3; /* Цвет кнопки при наведении */
        }
    </style>
<script>
    function toFieds(){
        requestData = {from: "init"}
        fetch('/fields', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        }).then(response => {
            if (response.ok){
                window.location.href = response.url;
            } else {
                console.error('Ошибка при переходе');
            }
        });
    }

    function toVisual(){
        requestData = {from: "visual"}
        fetch('/fields', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        }).then(response => {
            if (response.ok){
                window.location.href = response.url;
            } else {
                console.error('Ошибка при переходе');
            }
        });
    }
</script>
</head>
<body>
    <h1>База Данных Well Manager</h1>
    <hr>
    <input type="button" value="Исходные данные" onclick="toFieds()">
    <input type="button" value="Менеджер данных" onclick="window.location.href='/pump-card'">
    <input type="button" value="Визуализация данных" onclick="toVisual()">
</body>
</html>
