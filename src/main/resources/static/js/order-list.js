document.addEventListener("DOMContentLoaded", function () {

    const searchInput = document.getElementById("searchInput");
    const clearSearchBtn = document.getElementById("clearSearch");
    const statusFilter = document.getElementById("statusFilter");
    const sortFilter = document.getElementById("sortFilter");
    const gridContainer = document.getElementById("order-grid-container");

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
        if (searchInput && clearSearchBtn) {
            clearSearchBtn.style.display = searchInput.value.trim() !== "" ? "block" : "none";
        }
    }

    toggleClearButton();

    const debouncedSearch = debounce(() => {
        currentPage = 0;
        fetchOrders();
    }, 500);

    if (searchInput) {
        searchInput.addEventListener("input", function () {
            toggleClearButton();
            debouncedSearch();
        });

        searchInput.addEventListener("keypress", function (e) {
            if (e.key === "Enter") {
                e.preventDefault();
            }
        });
    }

    if (clearSearchBtn) {
        clearSearchBtn.addEventListener("click", function () {
            searchInput.value = "";
            toggleClearButton();
            currentPage = 0;
            fetchOrders();
        });
    }

    if (statusFilter) {
        statusFilter.addEventListener("change", () => {
            currentPage = 0;
            fetchOrders();
        });
    }

    if (sortFilter) {
        sortFilter.addEventListener("change", () => {
            currentPage = 0;
            fetchOrders();
        });
    }

    document.addEventListener("click", function (e) {
        if (e.target.classList.contains("ajax-page-link")) {
            e.preventDefault();
            currentPage = e.target.getAttribute("data-page");
            fetchOrders();
        }
    });

    function fetchOrders() {
        const keyword = searchInput ? searchInput.value.trim() : "";
        const status = statusFilter ? statusFilter.value : "";
        const sort = sortFilter ? sortFilter.value : "date_desc";

        const params = new URLSearchParams();
        if (keyword) params.append("keyword", keyword);
        if (status) params.append("status", status);
        if (sort) params.append("sort", sort);
        params.append("page", currentPage);
        params.append("size", 6);

        const newUrl = window.location.pathname + "?" + params.toString();
        window.history.pushState({ path: newUrl }, '', newUrl);

        fetch("/admin/orders/filter?" + params.toString())
            .then(response => {
                if (!response.ok) throw new Error("Network response was not ok");
                return response.text();
            })
            .then(html => {
                if (gridContainer) {
                    gridContainer.innerHTML = html;
                }
            })
            .catch(err => console.error("Error fetching orders:", err));
    }
});