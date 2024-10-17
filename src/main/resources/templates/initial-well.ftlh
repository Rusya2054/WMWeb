<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>WMWeb</title>
</head>
<body>
    <h1>ID: ${well.id} Name: ${well.name} Field: <#if well.field?has_content>${well.field}<#else>м-е отсутствует</#if></h1>
    <h2>Pump: <#if pumpCard.typeSize?has_content>${pumpCard.typeSize}<#else> Нет информации</#if> </h2>
    <hr>
    <table border="1">
        <#if indicators?has_content>
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
                <br>
            </#list>
        </#if>
    </table>
</body>
</html>
