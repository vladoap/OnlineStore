document.addEventListener("DOMContentLoaded", function () {

    var quantityInput = document.getElementById("product-quanity");
    var minusButton = document.getElementById("btn-minus");
    var plusButton = document.getElementById("btn-plus");

    quantityInput.value = "1";

    var quantity = parseInt(quantityInput.value);

    function updateQuantity() {
        quantityInput.value = quantity;
        document.getElementById("var-value").textContent = quantity;
    }

    minusButton.addEventListener("click", function () {
        if (quantity > 1) {
            quantity--;
            updateQuantity();
        }
    });

    plusButton.addEventListener("click", function () {
        quantity++;
        updateQuantity();
    });
});



document.addEventListener("DOMContentLoaded", function () {
    var addToCartButtons = document.querySelectorAll(".addToCartButton");
    addToCartButtons.forEach(function (button) {
        button.addEventListener("click", function (event) {
            event.preventDefault();
            var form = button.closest(".purchaseForm");
            form.submit();
        });
    });
});




// remove pictures - product-update.html
let selectedPictures = [];

function addPictureToSelection(pictureUrl) {
    if (!selectedPictures.includes(pictureUrl)) {
        selectedPictures.push(pictureUrl);
    }
}

function removePictureFromSelection(pictureUrl) {


    const index = selectedPictures.findIndex(url => url.toLowerCase() === pictureUrl.toLowerCase());

    if (index !== -1) {
        selectedPictures.splice(index, 1);
    }
}

function deletePicture(deleteButton) {

    const pictureFrame = deleteButton.parentElement;
    const pictureUrl = deleteButton.getAttribute('data-url').trim();

    pictureFrame.remove();


    removePictureFromSelection(pictureUrl);

    const selectedPicturesInput = document.querySelector('#selectedPictures');
    selectedPicturesInput.value = selectedPictures.join(',');

}

document.addEventListener("DOMContentLoaded", function() {

    const productPictures = document.getElementById('productPictures').getAttribute('data-pictures');

    const pictureUrls = productPictures.replace(/^\[|\]$/g, '').split(',').map(url => url.trim());


    if (selectedPictures.length === 0) {
        pictureUrls.forEach(pictureUrl => {
            addPictureToSelection(pictureUrl);
        });
    }
});


const form = document.querySelector('form');

form.addEventListener('submit', function (event) {
    // Update the selectedPictures input with the current selectedPictures array
    const selectedPicturesInput = document.querySelector('#selectedPictures');
    selectedPicturesInput.value = selectedPictures.join(',');
    console.log(selectedPicturesInput.value);
});




function updateTitle(input) {
    const titleInput = document.getElementById("title");
    titleInput.value = input.files[0].name;
}
