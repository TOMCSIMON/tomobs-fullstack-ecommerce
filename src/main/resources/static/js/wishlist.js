document.addEventListener("DOMContentLoaded", () => {

    document.querySelectorAll(".add-to-cart-btn").forEach(button => {
        button.addEventListener("click", async (e) => {
            e.preventDefault();

            const variantId = button.dataset.variantId;
            const itemRow = button.closest('.wishlist-item');

            const response = await fetch(`/cart/add?variantId=${variantId}`, {
                method: "POST"
            });

            const result = await response.json();

            if(result.success){
                Toast.fire({
                    icon: 'success',
                    title: 'Product added to cart successfully!'
                });

                if (itemRow) {
                    itemRow.remove();
                }
                if (document.querySelectorAll('.wishlist-item').length === 0) {
                    setTimeout(() => location.reload(), 1000);
                }
            } else {
                Toast.fire({
                    icon: 'error',
                    title: 'Something went wrong!'
                });
            }
        });
    });

    document.querySelectorAll(".remove-btn").forEach(button => {
        button.addEventListener("click", async (e) => {
            e.preventDefault();

            const variantId = button.dataset.variantId;
            const itemRow = button.closest('.wishlist-item');

            const response = await fetch(`/wishlist/delete?variantId=${variantId}`, {
                method: "DELETE"
            });

            const result = await response.json();

            if(result.success){
                Toast.fire({
                    icon: 'success',
                    title: 'Item removed from wishlist!'
                });
                if (itemRow) {
                    itemRow.remove();
                }
                if (document.querySelectorAll('.wishlist-item').length === 0) {
                    setTimeout(() => location.reload(), 1000);
                }
            } else {
                Toast.fire({
                    icon: 'error',
                    title: 'Failed to remove item.'
                });
            }
        });
    });
});