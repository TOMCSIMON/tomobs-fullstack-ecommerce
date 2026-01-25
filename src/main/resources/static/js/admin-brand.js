
// EDIT SECTION START
// Get the edit modal element by id
const editModal = document.getElementById('editBrandModal');

// Get the form inside the modal
const editForm = document.getElementById('editBrandForm');

// Get references to checkbox and hidden input
const statusCheckbox = document.getElementById('editBrandStatusInput');
const statusHiddenInput = document.getElementById('editBrandStatusHidden');

// Listen for when the modal is about to be shown
editModal.addEventListener('show.bs.modal', function (event) {

    // The button that triggered the modal
    const triggerElement = event.relatedTarget;

    // Read category id from data-id attribute
    const brandId = triggerElement.getAttribute('data-id');

    // Read category name from data-name attribute
    const brandName = triggerElement.getAttribute('data-name');

    // Read status value from data-status attribute (always a STRING)
    const statusValue = triggerElement.getAttribute('data-status');

    // Set category name into input field
    document.getElementById('editBrandNameInput').value = brandName || '';

    // Set checkbox state - explicitly convert string → boolean
    const isActive = (statusValue === 'true' || statusValue === true);
    statusCheckbox.checked = isActive;

    // Set the hidden input value to match the checkbox state
    statusHiddenInput.value = isActive ? 'true' : 'false';

    // Update the form action dynamically with the category id
    editForm.action = `/admin/brands/edit/${brandId}`;
});

// Handle checkbox change to update hidden input value in real-time
statusCheckbox.addEventListener('change', function() {
    // Update hidden input value based on checkbox state
    statusHiddenInput.value = this.checked ? 'true' : 'false';
});
