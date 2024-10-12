<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
</head>
<script>
    function wellFilterHandler(element){
        console.log(element.value)
    }

    function fieldFilterHandler(element){
        console.log(element.value)
    }

</script>
<body>
    <h1>Экспорт</h1>
    <hr>
    Фильтр по месторождению <input type="text" oninput="fieldFilterHandler(this)"><br>
    Фильтр по имени скважины <input type="text" oninput="wellFilterHandler(this)"><br>
    <br><br>

</body>
</html>
