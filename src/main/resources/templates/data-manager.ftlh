<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>Менеджер данных</title>
    </head>
    <body>
        <h1>Менеджер данных</h1><hr>
        <form action="/pump-card/create" method="post">
            Название месторождения<input type="text" name="field"/></br>
            Название месторождения<input type="text" name="well"/></br>
            Типоразмер<input type="text" name="typeSize"/></br>
            Глубина спуска насоса<input type="text" name="pumpDepth"/></br>
            Последняя дата отказа<input type="text" name="lastFailureDate"/></br>
            <input type="submit" value="Добавить скважину"/>
        </form>

            <h1>Выберите файлы</h1>
        <form action="/pump-card/upload" method="post" enctype="multipart/form-data">
            <input type="file" name="files[]" multiple>
            <button type="submit">Загрузить файлы</button>
        </form>

    </body>
</html>