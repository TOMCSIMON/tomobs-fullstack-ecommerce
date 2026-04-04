document.addEventListener("DOMContentLoaded", function () {

    const searchInput = document.getElementById("searchInput");
    const clearSearchBtn = document.getElementById("clearSearch");
    const tableContainer = document.getElementById("categoryTableContainer");

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
        fetchCategories();
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
            fetchCategories();
        });
    }


document.addEventListener("click", function (e) {
        const targetLink = e.target.closest("a.page-box");

        if (targetLink) {
            e.preventDefault();

            if (targetLink.classList.contains("disabled")) {
                return;
            }

            const urlParams = new URLSearchParams(targetLink.search);
            const pageStr = urlParams.get('page');

            if (pageStr !== null) {
                currentPage = parseInt(pageStr, 10);
                fetchCategories();
            }
        }
    });

    function fetchCategories() {
        const keyword = searchInput ? searchInput.value.trim() : "";

        const params = new URLSearchParams();
        if (keyword) params.append("keyword", keyword);
        params.append("page", currentPage);
        params.append("size", 5);
        const newUrl = window.location.pathname + "?" + params.toString();
        window.history.pushState({ path: newUrl }, '', newUrl);

        fetch("/admin/categories?" + params.toString(), {
            headers: {
                "X-Requested-With": "XMLHttpRequest"
            }
        })
        .then(response => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.text();
        })
        .then(html => {
            if (tableContainer) {
                const currentContainer = document.getElementById("categoryTableContainer");
                if (currentContainer) {
                    currentContainer.outerHTML = html;
                }
            }
        })
        .catch(err => console.error("Error fetching categories:", err));
    }

    const deleteModal = document.getElementById('deleteCategoryModal');

    let deleteCategoryId;

    deleteModal.addEventListener('show.bs.modal', function (event) {

        const triggerElement = event.relatedTarget;
        deleteCategoryId = triggerElement.getAttribute('data-id');
        const categoryName = triggerElement.getAttribute('data-name');

        document.getElementById('deleteCategoryName').textContent = categoryName;
    });

document.getElementById('confirmDeleteBtn').addEventListener('click', function () {

    fetch(`/admin/categories/delete/${deleteCategoryId}`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {

            const modalElement = document.getElementById('deleteCategoryModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement);
            modalInstance.hide();

            const toast = document.getElementById('toastMessage');
            toast.style.display = 'block';

            setTimeout(() => {
                toast.style.display = 'none';
            }, 3000);

            window.location.reload();
        }
    });
});

const editModal = document.getElementById('editCategoryModal');
const editForm = document.getElementById('editCategoryForm');
const statusCheckbox = document.getElementById('editCategoryStatusInput');
const statusHiddenInput = document.getElementById('editCategoryStatusHidden');

editModal.addEventListener('show.bs.modal', function (event) {

    const triggerElement = event.relatedTarget;
    const categoryId = triggerElement.getAttribute('data-id');
    const categoryName = triggerElement.getAttribute('data-name');
    const categoryDescription = triggerElement.getAttribute('data-description');
    const statusValue = triggerElement.getAttribute('data-status');

    document.getElementById('editCategoryNameInput').value = categoryName || '';
    document.getElementById('editCategoryDescriptionInput').value = categoryDescription || '';
    const isActive = (statusValue === 'true' || statusValue === true);
    statusCheckbox.checked = isActive;
    statusHiddenInput.value = isActive ? 'true' : 'false';
    editForm.action = `/admin/categories/edit/${categoryId}`;
});

statusCheckbox.addEventListener('change', function() {
    statusHiddenInput.value = this.checked ? 'true' : 'false';
});
});
