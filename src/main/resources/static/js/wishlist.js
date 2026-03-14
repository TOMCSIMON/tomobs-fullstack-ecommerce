document.addEventListener("DOMContentLoaded", () => {

    document.querySelectorAll(".remove-btn").forEach(button => {

        button.addEventListener("click", async () => {

            const variantId = button.dataset.variantId;

            const response = await fetch(`/wishlist/delete?variantId=${variantId}`, {
                method: "DELETE"
            });

            const result = await response.json();
             console.log("res", response);
            if(result.success){
                location.reload();
            }

        });

    });

});