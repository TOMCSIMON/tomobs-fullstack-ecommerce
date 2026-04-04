document.addEventListener("DOMContentLoaded", function() {

    const searchInput = document.getElementById('searchInput');
    const clearBtn = document.getElementById('clearSearch');
    let currentPage = 0;

    function debounce(func, delay) {
        let timeoutId;
        return function (...args) {
            clearTimeout(timeoutId);
            timeoutId = setTimeout(() => {
                func.apply(this, args);
            }, delay);
        };
    }

    function toggleClearButton() {
        if (searchInput && clearBtn) {
            clearBtn.style.display = searchInput.value.trim() !== "" ? "block" : "none";
        }
    }

    toggleClearButton();

    const debouncedSearch = debounce(() => {
        currentPage = 0;
        fetchProducts();
    }, 500);

    if (searchInput) {
        searchInput.addEventListener('input', function() {
            toggleClearButton();
            debouncedSearch();
        });

        searchInput.addEventListener("keypress", function (e) {
            if (e.key === "Enter") e.preventDefault();
        });
    }

    if (clearBtn) {
        clearBtn.addEventListener('click', function() {
            searchInput.value = '';
            toggleClearButton();
            currentPage = 0;
            fetchProducts();
        });
    }

    document.addEventListener("click", function (e) {
        const targetLink = e.target.closest("a.page-box");

        if (targetLink) {
            e.preventDefault();
            if (targetLink.classList.contains("disabled")) return;

            const urlParams = new URLSearchParams(targetLink.search);
            const pageStr = urlParams.get('page');

            if (pageStr !== null) {
                currentPage = parseInt(pageStr, 10);
                fetchProducts();
            }
        }
    });

    function fetchProducts() {
        const keyword = searchInput ? searchInput.value.trim() : "";
        const params = new URLSearchParams();

        if (keyword) params.append("keyword", keyword);
        params.append("page", currentPage);
        params.append("size", 5);

        const newUrl = window.location.pathname + "?" + params.toString();
        window.history.pushState({ path: newUrl }, '', newUrl);

        fetch(`/admin/products?${params.toString()}`, {
            headers: { "X-Requested-With": "XMLHttpRequest" }
        })
        .then(response => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.text();
        })
        .then(html => {
            const tableContainer = document.getElementById("productTableContainer");
            if (tableContainer) {
                tableContainer.outerHTML = html;
            }
        })
        .catch(err => console.error("Error fetching products:", err));
    }


    const deleteProductModal = document.getElementById('deleteProductModal');
    let deleteProductId = null;

    if (deleteProductModal) {
        deleteProductModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            deleteProductId = button.getAttribute('data-id');
            const productName = button.getAttribute('data-name');

            document.getElementById('deleteProductName').textContent = productName;
        });
    }

    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener('click', function (e) {
            e.preventDefault();
            if (!deleteProductId) return;

            fetch(`/admin/products/delete/${deleteProductId}`, {
                method: 'POST'
            })
            .then(response => {
                if (response.ok) {
                    const modalElement = document.getElementById('deleteProductModal');
                    const modalInstance = bootstrap.Modal.getInstance(modalElement);
                    modalInstance.hide();
                    window.location.reload();
                }
            })
            .catch(err => console.error("Error deleting product:", err));
        });
    }
});