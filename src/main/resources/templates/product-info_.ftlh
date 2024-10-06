<!DOCTYPE html>
<html>
    <head>
        <title>BUYSELL</title>
    </head>
    <body>
        <h1>BUYSELL</h1><hr>
        <h4>Информация о товаре</h4>
        <b>Название товара: ${product.title}</b><br>
        <b>Описание товара: ${product.description}</b><br>
        <b>Цена: ${product.price}</b><br>
        <b>Город: ${product.city}</b><br>
        <b>Автор: ${product.author}</b><br>
        <hr>
        <h3>Создать новый товар</h3>
            <form action="/product/delete/${product.id}" method="post">
                <input type="submit" value="Удалить товар">
            </form>
        </hr>
    </body>
</html>