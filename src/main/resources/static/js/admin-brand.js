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
