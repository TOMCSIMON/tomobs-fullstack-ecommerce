document.addEventListener("DOMContentLoaded", function() {

    // --- 1. SEARCH BAR LOGIC (Your existing code) ---
    const searchInput = document.getElementById('searchInput');
    const clearBtn = document.getElementById('clearSearch');
    const searchForm = document.getElementById('searchForm');

    function toggleClearButton() {
        if (searchInput && searchInput.value.length > 0) {
            clearBtn.style.display = 'block';
        } else if(clearBtn) {
            clearBtn.style.display = 'none';
        }
    }

    if (searchInput && clearBtn) {
        toggleClearButton();
        searchInput.addEventListener('input', toggleClearButton);
        clearBtn.addEventListener('click', function() {
            searchInput.value = '';
            toggleClearButton();
            searchForm.submit();
        });
    }

    // --- 2. DELETE MODAL LOGIC (New code) ---
    const deleteProductModal = document.getElementById('deleteProductModal');

    if (deleteProductModal) {
        deleteProductModal.addEventListener('show.bs.modal', function (event) {

            // Button that triggered the modal
            const button = event.relatedTarget;

            // Extract info from data-* attributes
            const productId = button.getAttribute('data-id');
            const productName = button.getAttribute('data-name');

            // Update the modal's text content
            const modalProductName = document.getElementById('deleteProductName');
            modalProductName.textContent = productName;

            // Update the 'Yes, Delete' button's href to route to your Spring Boot controller
            const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
            confirmDeleteBtn.href = '/admin/products/delete/' + productId;
        });
    }
});