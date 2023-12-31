
// Code for Products Management
document.addEventListener('DOMContentLoaded', function () {
    if (window.location.pathname.includes('/admin/products')) {

        loadProducts(1);

    }
});

const productContainer = document.getElementById('product-container');
const paginationContainer = document.getElementById('pagination-container');
let currentPage = 1;

function loadProducts(page) {
    const url = `/api/admin/products?page=${page}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const products = data.products;
            const pages = data.pages;

            productContainer.innerHTML = '';
            paginationContainer.innerHTML = '';

            products.forEach(product => {
                const productCard = createProductCard(product);
                productContainer.appendChild(productCard);
            });

            pages.forEach(page => {
                const pageLink = createPageLink(page);
                paginationContainer.appendChild(pageLink);
            });
        })
        .catch(error => {
            console.error('Error fetching offers:', error);
        });
}

function createProductCard(product) {
    const productCard = document.createElement('div');
    productCard.className = 'col-md-4';

    productCard.innerHTML = `
        <div class="card mb-4 product-wap rounded-0" style="height: 400px;">
            <div class="card rounded-0">
                <div class="d-flex justify-content-center align-items-center" style="height: 100%;">
                    <img class="card-img rounded-0 img-fluid" src="${product.imageUrl}">
                </div>

                <div class="card-img-overlay rounded-0 product-overlay d-flex flex-column justify-content-end">
                    <ul class="list-unstyled mt-auto">
                        <li><a class="btn btn-success text-white" href="/products/details/${product.id}"><i class="far fa-eye"></i></a></li>

                    </ul>
                </div>
            </div>
            <div class="card-body text-center d-flex flex-column align-items-center">
                <a href="/products/details/${product.id}" class="h3 text-decoration-none text-center">${product.name}</a>
                <p class="mt-2">${product.price} Lv.</p>
            </div>

        </div>
        <div class="button-bar" style="margin-top: 5px;">
      <button data-id="${product.id}" class="btn btn-danger delete-button rounded-0 shadow-sm border-top-0 border-left-0" style="width: 100%;" onclick="deleteProduct(${product.id})">Delete</button>
    </div>
    `;

    return productCard;
}

function createPageLink(page) {
    const pageLink = document.createElement('li');
    pageLink.innerHTML = `<a href="javascript:void(0);" data-page="${page}" class="page-link rounded-0 mr-3 shadow-sm border-top-0 border-left-0 text-dark">${page}</a>`;

    if (page === currentPage) {
        pageLink.querySelector('a').classList.add('clicked');
    }

    pageLink.addEventListener('click', function () {
        if (page !== currentPage) {
            loadProducts(page);
            currentPage = page;
        }
    });

    return pageLink;
}

function deleteProduct(productId) {
    console.log(productId);
    const url = `/api/admin/products/delete/${productId}`;

    fetch(url, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                console.log('Product deleted successfully.');
                window.location.reload();
            } else {
                console.error('Error deleting product:', response.statusText);
            }
        })
        .catch(error => {
            console.error('Error deleting product:', error);
        });
}






// Code for Users Management
document.addEventListener('DOMContentLoaded', function () {
    if (window.location.pathname.includes('/admin/users')) {
        loadUsers();
    }
});

function loadUsers() {
    fetch('/api/admin/users')
        .then(response => response.json())
        .then(users => {
            const userList = document.getElementById('user-list');

            users.forEach(user => {
                const userCard = document.createElement('div');
                userCard.classList.add('col', 'col-md-9', 'col-lg-7', 'col-xl-5');
                userCard.innerHTML = `
                 <div class="card user-card" style="border-radius: 15px;">
    <div class="card-body p-4">
        <div class="d-flex flex-wrap text-black">
            <div class="flex-shrink-0 user-image-container">
                <img src="${user.profilePicture}" alt="User Profile Picture" class="img-fluid user-image">
            </div>
            <div class="flex-grow-1 ms-3">
                <h5 class="mb-1">${user.fullName}</h5>
                <p class="mb-2 pb-1" style="color: #2b2a2a;">@${user.username}</p>
                <p class="mb-2 pb-1" style="color: #2b2a2a;">E-mail: ${user.email}</p>
            </div>
        </div>
        <div class="bg-light rounded-3 p-2 mb-2" style="margin-top: 20px;">
            <div class="row">
                <div class="col-md-4 text-center">
                    <p class="small text-muted mb-1">Products</p>
                    <p class="mb-0">${user.productsCount}</p>
                </div>
                <div class="col-md-4 text-center">
                    <p class="small text-muted mb-1">Orders</p>
                    <p class="mb-0">${user.ordersCount}</p>
                </div>
                <div class="col-md-4 text-center">
                    <p class="small text-muted mb-1">Role</p>
                    <p class="mb-0" style="font-weight: bold !important;">${user.role}</p>
                </div>
            </div>
        </div>
        <div class="d-flex justify-content-end">
            <button type="button" class="btn btn-secondary custom-button" 
                onclick="deleteUser(${user.id})">Delete</button>
            ${user.role !== 'ADMIN' ? `<button type="button" class="btn btn-primary custom-button" 
                style="margin-left: 20px;"
                onclick="promoteToAdmin(${user.id})">Promote to Admin</button>` : ''}
        </div>
    </div>
</div>
                `;

                userList.appendChild(userCard);

                checkUserIsNotCurrentUser(user.id, userCard);
            });
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
        });
}

function deleteUser(userId) {
    fetch(`/api/admin/users/delete/${userId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
               console.log('User deleted.')
                window.location.reload();
            } else {
                console.error('Error deleting user');
            }

        })
        .catch(error => {
            console.error('Error deleting user:', error);
        });
}

function promoteToAdmin(userId) {
    console.log("OK in promoteToAdmin function");
    fetch(`/api/admin/users/promote/${userId}`, {
        method: 'PATCH',
    })
        .then(response => {
            if (response.ok) {
                console.log("User successfully promoted to Admin")
                window.location.reload();
            } else {
                console.error('Error promoting user to admin');
            }
        })
        .catch(error => {
            console.error('Error promoting user to admin:', error);
        });
}


function checkUserIsNotCurrentUser(userId, userCard) {

    // Make an AJAX request to check if the user is not the current user
    fetch(`/api/admin/user/${userId}`)
        .then(response => response.json())
        .then(isNotCurrentUser => {

            if (isNotCurrentUser) {
                const deleteButton = userCard.querySelector('button.btn-secondary');
                deleteButton.style.display = 'none'; // Or 'inline-block' to match the other button
            }

        })
        .catch(error => {
            console.error('Error checking user:', error);
        });
}