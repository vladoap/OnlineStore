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
