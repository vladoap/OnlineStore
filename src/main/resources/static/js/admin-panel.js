

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

            productContainer.innerHTML = ''; // Clear existing product content
            paginationContainer.innerHTML = ''; // Clear existing pagination content

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