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

    function selectButtonHandler(element){
        const wellData = ${wells?json_string}
        console.log(wellData);
         wellData.forEach(function(well) {
                console.log("Well Name: " + well.name);
            });
    }

    function setInterval(value){

    }

</script>
<body>
    <h1>Экспорт</h1>
    <hr>
        Фильтр по месторождению <input type="text" oninput="fieldFilterHandler(this)"><br>
        Фильтр по имени скважины <input type="text" oninput="wellFilterHandler(this)"><br>
    <br><br>
    <input type="button" value="1 месяц" onclick=""> | <input type="button" value="3 месяца" onclick=""> | <input type="button" value="6 месяцев" onclick=""> <br>
    <input type="button" value="Cнять все" onclick="selectButtonHandler(this)"> <br>
    <input type="button" value="Экспорт" onclick=""><br>
    <br>
    <#if wells?has_content>
        <#list wells as well>
            <#if well.field?has_content>
                    <b>${well.field}</b>
                <#else>
                    <b>м-е отсутствует</b>
            </#if> | ${well.name} | <input type="date" name="minDate" value="${well.minDate}"> | <input type="date" name="maxDate" value="${well.maxDate}"> <input type="checkbox" name="option" value="option" checked="checked"> <br>
        </#list>
    </#if>

</body>
</html>
