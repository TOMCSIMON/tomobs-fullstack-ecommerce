document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.getElementById('searchInput');
    const clearBtn = document.getElementById('clearSearch');
    const searchForm = document.getElementById('searchForm');

    function toggleClearButton() {
        if (searchInput.value.length > 0) {
            clearBtn.style.display = 'block';
        } else {
            clearBtn.style.display = 'none';
        }
    }
    toggleClearButton();
    searchInput.addEventListener('input', toggleClearButton);
    clearBtn.addEventListener('click', function() {
        searchInput.value = '';
        toggleClearButton();
        searchForm.submit();
    });
});

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
