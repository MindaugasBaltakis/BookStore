var searchBar = document.querySelector('#searchBar');
var barcode = document.querySelector('#barcode');
var title = document.querySelector('#title');
var author = document.querySelector('#author');
var quantity = document.querySelector('#quantity');
var price = document.querySelector('#price');
var year = document.querySelector('#year');
var totalPrice = document.querySelector('#totalPrice');


var searchBtn = document.querySelector('#searchBtn');
searchBtn.addEventListener('click', loadBook);

var addBookBtn = document.querySelector('#addBookBtn');
addBookBtn.addEventListener('click', addNewBook);

var updateBookBtn = document.querySelector('#updateBookBtn');
updateBookBtn.addEventListener('click', updateBook);

var cancelBtn = document.querySelector('#cancel');
cancelBtn.addEventListener('click', hideUpdateBtn);

var calculateBtn = document.querySelector('#calculate');
calculateBtn.addEventListener('click', calculateTotalPrice);

var barcodeToUpdate;

function loadBook() {
    if(searchBar.value > 0){
        fetch(`api/book/${searchBar.value}`)
            .then(response => {
                if (!response.ok) { throw response }
                return response.json()
            })
            .then(data => {
                barcodeToUpdate = data.barcode;
                barcode.value = data.barcode;
                title.value = data.title;
                author.value = data.author;
                quantity.value = data.quantity;
                price.value = data.price;
                year.value = data.releaseYear;
                showUpdateBtn();
                searchBar.value = "";
            })
            .catch(error => error.text().then(errMessage => alert(errMessage)));
    }
}

function addNewBook() {
    //Checking if all required fields are filled
    if(barcode.value > 0 && title.value && author.value && price.value && quantity.value) {
        fetch(`api/book`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                barcode: barcode.value,
                title: title.value,
                author: author.value,
                quantity: quantity.value,
                price: price.value,
                releaseYear: year.value
            })
        }).then(response => response.text())
            .then(message => alert(message));
        resetValues();
    } else {
        totalPrice.innerHTML = "Fill required fields!"
    }
}

function updateBook() {
    if(barcode.value > 0 && title.value && author.value && price.value && quantity.value) {
        fetch(`api/book/${barcodeToUpdate}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                barcode: barcode.value,
                title: title.value,
                author: author.value,
                quantity: quantity.value,
                price: price.value,
                releaseYear: year.value
            })
        }).then(response => response.text())
            .then(message => alert(message));
        hideUpdateBtn();
    } else{
        totalPrice.innerHTML = "Fill required fields!"
    }
}

function calculateTotalPrice() {
    fetch(`api/book/price/${barcodeToUpdate}`)
        .then(response => response.text())
        .then(message => {
            totalPrice.innerHTML = `All books with barcode Nr. ${barcodeToUpdate} cost ${message} Eur.`
        });
}

function hideUpdateBtn() {
    addBookBtn.style.display = "inline-block";
    updateBookBtn.style.display = "none";
    cancelBtn.style.display = "none";
    calculateBtn.style.display = "none";
    resetValues();
}

function showUpdateBtn() {
    addBookBtn.style.display = "none";
    updateBookBtn.style.display = "inline-block";
    cancelBtn.style.display = "inline-block";
    calculateBtn.style.display = "inline-block";
}

function resetValues() {
    barcode.value = "";
    title.value = "";
    author.value = "";
    quantity.value = "";
    price.value = "";
    year.value = "";
    barcodeToUpdate = "";
    totalPrice.innerHTML = "";
}