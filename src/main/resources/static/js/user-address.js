function populateEditModal(button) {
    document.getElementById('editId').value = button.getAttribute('data-id');
    document.getElementById('editFullName').value = button.getAttribute('data-fullname');
    document.getElementById('editPhone').value = button.getAttribute('data-phone');
    document.getElementById('editLine1').value = button.getAttribute('data-line1');
    document.getElementById('editLine2').value = button.getAttribute('data-line2');
    document.getElementById('editCity').value = button.getAttribute('data-city');
    document.getElementById('editState').value = button.getAttribute('data-state');
    document.getElementById('editZip').value = button.getAttribute('data-zip');
    document.getElementById('editCountry').value = button.getAttribute('data-country');

    const isDefault = button.getAttribute('data-default') === 'true';
    document.getElementById('editDefaultCheck').checked = isDefault;
}

document.addEventListener('DOMContentLoaded', function() {

     const editForm = document.getElementById('editAddressForm');

     if (editForm) {
         editForm.addEventListener('submit', function(e) {
             e.preventDefault();

             const formData = new FormData(editForm);
             const addressData = Object.fromEntries(formData.entries());

             addressData.isDefault = document.getElementById('editDefaultCheck').checked;

             const submitBtn = editForm.querySelector('button[type="submit"]');
             const originalBtnText = submitBtn.innerText;
             submitBtn.disabled = true;

             fetch('/address/edit', {
                 method: 'PUT',
                 headers: {
                     'Content-Type': 'application/json'
                 },
                 body: JSON.stringify(addressData)
             })
             .then(response => {
                 if (response.ok) {
                     const modalEl = document.getElementById('editAddressModal');
                     const modal = bootstrap.Modal.getInstance(modalEl);
                     modal.hide();

                     Toast.fire({
                         icon: 'success',
                         title: 'Address updated successfully!'
                     });

                     setTimeout(() => {
                         window.location.reload();
                     }, 1500);

                 } else {
                     Toast.fire({
                         icon: 'error',
                         title: 'Failed to update address.'
                     });
                     submitBtn.disabled = false;
                     submitBtn.innerText = originalBtnText;
                 }
             })
             .catch(error => {
                 console.error('Error:', error);
                 Toast.fire({
                     icon: 'error',
                     title: 'Something went wrong!'
                 });
                 submitBtn.disabled = false;
                 submitBtn.innerText = originalBtnText;
             });
         });
     }
 });

function setDeleteId(button) {
    const id = button.getAttribute('data-id');
    const confirmBtn = document.getElementById('confirmDeleteBtn');

    confirmBtn.onclick = function() {
        executeDelete(id);
    };
}

function executeDelete(id) {

    const confirmBtn = document.getElementById('confirmDeleteBtn');
    confirmBtn.disabled = true;

    fetch('/address/delete/' + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {

            const modalEl = document.getElementById('deleteAddressModal');
            const modal = bootstrap.Modal.getInstance(modalEl);
            modal.hide();

            Toast.fire({
                icon: 'success',
                title: 'Address deleted successfully!'
            });
            setTimeout(() => {
                window.location.reload();
            }, 1500);

        } else {
            Toast.fire({
                icon: 'error',
                title: 'Failed to delete address.'
            });
            resetDeleteButton(confirmBtn);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        Toast.fire({
            icon: 'error',
            title: 'Something went wrong!'
        });
        resetDeleteButton(confirmBtn);
    });
}
function resetDeleteButton(btn) {
    btn.disabled = false;
    btn.innerText = "Yes, Delete";
}