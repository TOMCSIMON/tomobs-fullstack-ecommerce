document.addEventListener("DOMContentLoaded", function () {

    document.querySelectorAll(".increase-btn").forEach(btn => {
        btn.addEventListener("click", function () {

            const cartItemId = this.getAttribute("data-cart-item-id");
            const qtySpan = this.parentElement.querySelector(".qty-value");

            const currentQty = parseInt(qtySpan.innerText);
            updateQuantity(cartItemId, currentQty + 1);
        });
    });

    document.querySelectorAll(".decrease-btn").forEach(btn => {
        btn.addEventListener("click", function () {

            const cartItemId = this.getAttribute("data-cart-item-id");
            const qtySpan = this.parentElement.querySelector(".qty-value");


            const currentQty = parseInt(qtySpan.innerText);
            updateQuantity(cartItemId, currentQty - 1);
        });
    });

    document.querySelectorAll(".remove-btn").forEach(btn => {
        btn.addEventListener("click", function () {

            const cartItemId = this.getAttribute("data-cart-item-id");
            updateQuantity(cartItemId, 0);
        });
    });

    function updateQuantity(cartItemId, quantity) {

        fetch("/cart/update-quantity", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `cartItemId=${cartItemId}&quantity=${quantity}`
        })
        .then(res => {
            if (!res.ok) throw new Error("Update failed");
            location.reload();
        })
        .catch(err => console.error(err));
    }
});
