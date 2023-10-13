
document.addEventListener("DOMContentLoaded", function () {

    const fileInput = document.getElementById("profileImage");
    const imagePreview = document.getElementById("profileImagePreview");



// Add an event listener to the file input
    fileInput.addEventListener("change", function () {
        if (fileInput.files && fileInput.files[0]) {
            const reader = new FileReader();

            reader.onload = function (e) {
                imagePreview.src = e.target.result;
            };

            reader.readAsDataURL(fileInput.files[0]);
        }
    });
});


