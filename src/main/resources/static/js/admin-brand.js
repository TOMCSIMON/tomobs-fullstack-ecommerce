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
        fetchBrands();
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
            fetchBrands();
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
                fetchBrands();
            }
        }
    });

    function fetchBrands() {
        const keyword = searchInput ? searchInput.value.trim() : "";
        const params = new URLSearchParams();

        if (keyword) params.append("keyword", keyword);
        params.append("page", currentPage);
        params.append("size", 5);

        const newUrl = window.location.pathname + "?" + params.toString();
        window.history.pushState({ path: newUrl }, '', newUrl);

        fetch(`/admin/brands?${params.toString()}`, {
            headers: { "X-Requested-With": "XMLHttpRequest" }
        })
        .then(response => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.text();
        })
        .then(html => {
            const tableContainer = document.getElementById("brandTableContainer");
            if (tableContainer) {
                tableContainer.outerHTML = html;
            }
        })
        .catch(err => console.error("Error fetching brands:", err));
    }
});

const editModal = document.getElementById('editBrandModal');
const editForm = document.getElementById('editBrandForm');
const statusCheckbox = document.getElementById('editBrandStatusInput');
const statusHiddenInput = document.getElementById('editBrandStatusHidden');
editModal.addEventListener('show.bs.modal', function (event) {

    const triggerElement = event.relatedTarget;
    const brandId = triggerElement.getAttribute('data-id');
    const brandName = triggerElement.getAttribute('data-name');
    const statusValue = triggerElement.getAttribute('data-status');
    document.getElementById('editBrandNameInput').value = brandName || '';
    const isActive = (statusValue === 'true' || statusValue === true);
    statusCheckbox.checked = isActive;

    statusHiddenInput.value = isActive ? 'true' : 'false';

    editForm.action = `/admin/brands/edit/${brandId}`;
});

statusCheckbox.addEventListener('change', function() {
    statusHiddenInput.value = this.checked ? 'true' : 'false';
});

const deleteModal = document.getElementById('deleteBrandModal');
let deleteCategoryId;

deleteModal.addEventListener('show.bs.modal', function (event) {

    const triggerElement = event.relatedTarget;
    deleteBrandId = triggerElement.getAttribute('data-id');
    const BrandName = triggerElement.getAttribute('data-name');
    document.getElementById('deleteBrandName').textContent = BrandName;
});

document.getElementById('confirmDeleteBtn').addEventListener('click', function () {

    fetch(`/admin/brands/delete/${deleteBrandId}`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {

            const modalElement = document.getElementById('deleteBrandModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement);
            modalInstance.hide();

            const toast = document.getElementById('toastMessage');
            toast.style.display = 'block';

            setTimeout(() => {
                toast.style.display = 'none';
                window.location.reload();
            }, 5000);
        }
    });
});
