<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>Менеджер данных</title>
    </head>
    <script>
        function wellNameListInit(){
            const inputElement = document.getElementById("inputWellName");
            const list = document.getElementById('well-list');

            const requestData = {
                input:inputElement.value
            };
            fetch("/input-well-handler", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            }).then(response => response.json())
            .then(data=>{
                for (const well of data["wellList"]){
                    const option = document.createElement('option');
                    option.value = well;
                    console.log(well);
                }
            })
            .catch(err => console.log(err))
        }

        function inputDateHandler(element){
            const inputText = element.value;
            if (inputText.length > 10){
                element.value = "";
            } else if (inputText.length == 10) {
                console.log(inputText);
            }
        }


        document.getElementById('pumpForm').addEventListener('submit', function(event) {
        console.log(event);
        event.preventDefault();

        const formData = new FormData(this);
        const jsonData = {};

        formData.forEach((value, key) => {
            jsonData[key] = value;
        });


        fetch('/pump-card/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(jsonData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Success:', data);
        })
        .catch((error) => {
            console.error('Error:', error);
        });
    });

        window.onload = wellNameListInit;
    </script>
    <body>
        <h1>Менеджер данных</h1><hr>
        <form id="pumpCard">
            Название месторождения<input type="text" name="field"/></br>
            Название скважины<input type="text" name="wellName" id="inputWellName" list="well-list"/></br>
                <datalist id="well-list"></datalist>
            Типоразмер<input type="text" name="typeSize"/></br>
            Глубина спуска насоса, м <input type="text" name="pumpDepth"/></br>
            Последняя дата отказа, <input type="text" placeholder="ДД.MM.ГГГГ" name="lastFailureDate" oninput="inputDateHandler(this)"/></br>
            <input type="submit" value="Добавить скважину"/>
        </form>

            <h1>Выберите файлы</h1>
        <form action="/pump-card/upload" method="post" enctype="multipart/form-data">
            <input type="file" name="files[]" multiple>
            <button type="submit">Загрузить файлы</button>
        </form>
    </body>
</html>