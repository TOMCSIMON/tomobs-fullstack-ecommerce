document.addEventListener("DOMContentLoaded", function () {

    const addToCartForm = document.getElementById('addToCartForm');

    if (addToCartForm) {
        addToCartForm.addEventListener('submit', function (e) {
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
                console.error('Error adding to cart:', error);
                if (error.message !== 'User not logged in') {
                    alert('Failed to add item to cart. Please try again.');
                }
            });
        });
    }
});