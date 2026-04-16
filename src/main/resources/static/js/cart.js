document.addEventListener("DOMContentLoaded", function () {

    document.querySelectorAll(".increase-btn, .decrease-btn, .remove-btn").forEach(btn => {
        btn.addEventListener("click", function () {

            const cartItemId = this.getAttribute("data-cart-item-id");
            const cartCard = this.closest(".cart-card");
            const qtySpan = cartCard.querySelector(".qty-value");

            let currentQty = parseInt(qtySpan.innerText);
            let newQty = currentQty;

            if (this.classList.contains("increase-btn")) newQty++;
            if (this.classList.contains("decrease-btn")) newQty--;
            if (this.classList.contains("remove-btn")) newQty = 0;

            if (newQty < 0) return;

            updateQuantity(cartItemId, newQty, cartCard);
        });
    });

    function updateQuantity(cartItemId, quantity, cartCard) {

        fetch("/cart/update-quantity", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `cartItemId=${cartItemId}&quantity=${quantity}`
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {

                    if (quantity === 0) {
                        cartCard.remove();
                        checkIfCartIsEmpty();
                    } else {
                        cartCard.querySelector(".qty-value").innerText = quantity;
                    }
                    recalculateCartTotals();
                }
            })
            .catch(err => console.error("Error updating cart:", err));
    }

    function recalculateCartTotals() {
        let newTotal = 0;
        const cartItems = document.querySelectorAll(".cart-card");

        cartItems.forEach(card => {
            const priceText = card.querySelector(".price").innerText.replace(/[^0-9.]/g, "");
            const price = parseFloat(priceText);
            const qty = parseInt(card.querySelector(".qty-value").innerText);

            newTotal += (price * qty);
        });

        const formattedTotal = "₹" + newTotal.toFixed(2);

        const summaryPrices = document.querySelectorAll(".summary-card .d-flex span:nth-child(2)");
        if (summaryPrices.length >= 4) {
            summaryPrices[0].innerText = formattedTotal;
            summaryPrices[3].innerText = formattedTotal;
        }
    }

    function checkIfCartIsEmpty() {
        const cartItems = document.querySelectorAll(".cart-card");
        if (cartItems.length === 0) {
            location.reload();
        }
    }
});