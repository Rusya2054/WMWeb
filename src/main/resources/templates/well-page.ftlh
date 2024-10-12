<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Менеджер данных</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh; /* Занять всю высоту окна */
            margin: 0;
            background-color: #f0f8ff; /* Светлый фон */
        }

        form {
            background-color: white; /* Цвет фона формы */
            padding: 20px;
            border-radius: 8px; /* Скругленные углы */
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Тень */
            opacity: 0; /* Начальная непрозрачность для анимации */
            animation: fadeIn 1s forwards; /* Анимация появления */
        }

        @keyframes fadeIn {
            to {
                opacity: 1; /* Конечная непрозрачность */
            }
        }

        input[type="text"], input[type="submit"] {
            width: 80%; /* Полная ширина для полей ввода */
            padding: 10px;
            margin: 10px 0; /* Отступы между полями */
            border: 1px solid #007bff; /* Граница поля */
            border-radius: 5px; /* Скругленные углы */
        }

        input[type="submit"] {
            background-color: #007bff; /* Цвет кнопки */
            color: white; /* Цвет текста кнопки */
            border: none; /* Убрать границу */
            cursor: pointer; /* Курсор при наведении */
        }

        input[type="submit"]:hover {
            background-color: #0056b3; /* Цвет кнопки при наведении */
        }
    </style>
    <script>
        function inputDateHandler(element) {
            const inputText = element.value;
            const regex = /^\d{2}.\d{2}.\d{4}$/;
            if (inputText.length > 10) {
                element.value = "";
            } else if (inputText.length == 10) {
                if (!regex.test(inputText)) {
                    element.value = "";
                }
            }
        }
    </script>
</head>
<body>
    <form action="/well/${well.id}/update">
        ID скважины: ${well.id}</br>
        <#if well.field?has_content>
            Название месторождения: <input type="text" name="field" placeholder="${well.field}"/></br>
        <#else>
            <input type="text" name="field" placeholder="м-е отсутствует"/></br>
        </#if>
        Название скважины: ${well.name}</br>
        <datalist id="well-list"></datalist>
        Типоразмер:
        <#if pumpCard.typeSize?has_content>
            <input type="text" name="typeSize" placeholder="${pumpCard.typeSize}"/></br>
        <#else>
            <input type="text" name="typeSize"/></br>
        </#if>
        Глубина спуска насоса, м:
        <#if pumpCard.pumpDepth?has_content>
            <input type="text" name="pumpDepth" placeholder="${pumpCard.pumpDepth}"/></br>
        <#else>
            <input type="text" name="pumpDepth"/></br>
        </#if>
        Последняя дата отказа:
        <#if pumpCard.lastFailureDate?has_content>
            <input type="text" placeholder="${lastFailureDate}" name="lastFailureDate" oninput="inputDateHandler(this)"/></br>
        <#else>
            <input type="text" placeholder="ДД.MM.ГГГГ" name="lastFailureDate" oninput="inputDateHandler(this)"/></br>
        </#if>
        <input type="submit" value="Обновить данные"/>
    </form>
</body>
</html>
