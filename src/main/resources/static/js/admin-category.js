// DELETE SECTION START
// Get the delete modal element
const deleteModal = document.getElementById('deleteCategoryModal');

// Store selected category id
let deleteCategoryId;

// When modal opens, read the category id
deleteModal.addEventListener('show.bs.modal', function (event) {

    const triggerElement = event.relatedTarget;

    // Read category id
    deleteCategoryId = triggerElement.getAttribute('data-id');

    // Read category name
    const categoryName = triggerElement.getAttribute('data-name');

    // Set name in modal
    document.getElementById('deleteCategoryName').textContent = categoryName;
});

// When user confirms delete
document.getElementById('confirmDeleteBtn').addEventListener('click', function () {

    fetch(`/admin/categories/delete/${deleteCategoryId}`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {

             // CLOSE MODAL
            const modalElement = document.getElementById('deleteCategoryModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement);
            modalInstance.hide();

            // SHOW SUCCESS MESSAGE ONLY AFTER DELETE
            const toast = document.getElementById('toastMessage');
            toast.style.display = 'block';

            // HIDE AFTER 5 SECONDS AND RELOAD
            setTimeout(() => {
                toast.style.display = 'none';
                 // Reload page to reflect changes
                 window.location.reload();
            }, 5000);
        }
    });
});
// DELETE SECTION END

// EDIT SECTION START
// Get the edit modal element by id
const editModal = document.getElementById('editCategoryModal');

// Get the form inside the modal
const editForm = document.getElementById('editCategoryForm');

// Get references to checkbox and hidden input
const statusCheckbox = document.getElementById('editCategoryStatusInput');
const statusHiddenInput = document.getElementById('editCategoryStatusHidden');

// Listen for when the modal is about to be shown
editModal.addEventListener('show.bs.modal', function (event) {

    // The button that triggered the modal
    const triggerElement = event.relatedTarget;

    // Read category id from data-id attribute
    const categoryId = triggerElement.getAttribute('data-id');

    // Read category name from data-name attribute
    const categoryName = triggerElement.getAttribute('data-name');

    // Read category description from data-description attribute
    const categoryDescription = triggerElement.getAttribute('data-description');

    // Read status value from data-status attribute (always a STRING)
    const statusValue = triggerElement.getAttribute('data-status');

    // Set category name into input field
    document.getElementById('editCategoryNameInput').value = categoryName || '';

    // Set category description into textarea field
    document.getElementById('editCategoryDescriptionInput').value = categoryDescription || '';

    // Set checkbox state - explicitly convert string → boolean
    const isActive = (statusValue === 'true' || statusValue === true);
    statusCheckbox.checked = isActive;
    
    // Set the hidden input value to match the checkbox state
    statusHiddenInput.value = isActive ? 'true' : 'false';

    // Update the form action dynamically with the category id
    editForm.action = `/admin/categories/edit/${categoryId}`;
});

// Handle checkbox change to update hidden input value in real-time
statusCheckbox.addEventListener('change', function() {
    // Update hidden input value based on checkbox state
    statusHiddenInput.value = this.checked ? 'true' : 'false';
});
