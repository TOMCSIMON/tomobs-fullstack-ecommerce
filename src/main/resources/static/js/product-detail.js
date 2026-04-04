document.addEventListener("DOMContentLoaded", function () {

    initializeThumbnailLogic();
    initializeAddToCart();
    document.addEventListener('click', function (e) {
        if (e.target.classList.contains('option-btn')) {
            const variantId = e.target.getAttribute('data-variant-id');
            if (!variantId) return;

            fetch(`/products/details/${variantId}`)
                .then(response => {
                    if (!response.ok) throw new Error('Failed to load variant');
                    return response.text();
                })
                .then(html => {

                    const fragmentContainer = document.querySelector('.product-card').parentElement;

                    if (fragmentContainer) {
                        fragmentContainer.innerHTML = html;
                        initializeThumbnailLogic();
                        initializeAddToCart();
                    }
                })
                .catch(error => console.error('Error updating variant:', error));
        }
    });

    // --- 3. THUMBNAIL GALLERY FUNCTION ---
    function initializeThumbnailLogic() {
        const thumbnails = document.querySelectorAll('.thumb-img');
        const mainImage = document.querySelector('.main-img');

        if (thumbnails.length > 0 && mainImage) {
            thumbnails.forEach(thumb => {
                thumb.onclick = function () {
                    mainImage.src = this.src;
                    thumbnails.forEach(img => img.style.border = "1px solid #ddd");
                    this.style.border = "1px solid #010101";
                };
            });
        }
    }

    // --- 4. ADD TO CART FUNCTION ---
    function initializeAddToCart() {
        const addToCartForm = document.getElementById('addToCartForm');

        if (!addToCartForm) return;

        addToCartForm.onsubmit = function (e) {
            e.preventDefault();

            const formData = new FormData(addToCartForm);
            const variantId = formData.get('variantId');

            const params = new URLSearchParams();
            params.append('variantId', variantId);

            fetch('/cart/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: params.toString()
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    if (response.status === 401 || response.status === 403) {
                        window.location.href = '/login';
                        throw new Error('User not logged in');
                    }
                    throw new Error('Network response was not ok');
                }
            })
            .then(data => {
                if (data && data.success) {
                    if (typeof Toast !== 'undefined') {
                        Toast.fire({
                            icon: 'success',
                            title: 'Item added to cart!'
                        });
                    }
                }
            })
            .catch(error => {
                if (error.message !== 'User not logged in') {
                    Toast.fire({
                        icon: 'error',
                        title: 'something went wrong'
                    });
                }
            });
        };
    }
});