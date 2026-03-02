
function startEdit(iconElement) {
    const group = iconElement.closest('.field-group');
    const inputField = group.querySelector('.profile-input');
    const actionButtons = group.querySelector('.action-buttons');

    inputField.setAttribute('data-original-value', inputField.value);
    inputField.removeAttribute('readonly');
    inputField.focus();

    iconElement.style.display = 'none';
    actionButtons.classList.remove('d-none');
}

function cancelEdit(buttonElement) {
    const group = buttonElement.closest('.field-group');
    const inputField = group.querySelector('.profile-input');
    const iconElement = group.querySelector('.edit-icon');
    const actionButtons = group.querySelector('.action-buttons');

    inputField.value = inputField.getAttribute('data-original-value');
    inputField.setAttribute('readonly', true);

    actionButtons.classList.add('d-none');
    iconElement.style.display = 'inline-block';
}

function saveEdit(buttonElement) {
    const group = buttonElement.closest('.field-group');
    const inputField = group.querySelector('.profile-input');
    const iconElement = group.querySelector('.edit-icon');
    const actionButtons = group.querySelector('.action-buttons');

    const fieldName = inputField.getAttribute('data-field');
    const newValue = inputField.value.trim();
    const originalValue = inputField.getAttribute('data-original-value');

    if (newValue === originalValue) {
        cancelEdit(buttonElement);
        return;
    }

    const payload = {
        field: fieldName,
        value: newValue
    };

    fetch('/profile/update', {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (response.ok) {
            inputField.setAttribute('data-original-value', newValue);
            inputField.setAttribute('readonly', true);
            actionButtons.classList.add('d-none');
            iconElement.style.display = 'inline-block';
            Toast.fire({
                icon: 'success',
                title: 'Profile updated successfully!'
            });
        } else {
            Toast.fire({
                icon: 'error',
                title: 'Failed to update. Please try again.'
            });
            cancelEdit(buttonElement);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        Toast.fire({
            icon: 'error',
            title: 'Something went wrong!'
        });
        cancelEdit(buttonElement);
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const passwordForm = document.getElementById('changePasswordForm');
    if (passwordForm) {
        passwordForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const oldPassword = document.getElementById('oldPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmNewPassword = document.getElementById('confirmNewPassword').value;

            if (newPassword !== confirmNewPassword) {
                Toast.fire({
                    icon: 'warning',
                    title: 'New passwords do not match!'
                });
                return;
            }

            const payload = {
                oldPassword: oldPassword,
                newPassword: newPassword,
                confirmNewPassword: confirmNewPassword
            };

            fetch('/profile/password', {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            })
            .then(async response => {
                if (response.ok) {
                    Toast.fire({
                        icon: 'success',
                        title: 'Password successfully updated!'
                    });

                    document.getElementById('changePasswordForm').reset();
                    const modalEl = document.getElementById('changePasswordModal');
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    modal.hide();
                } else {
                    const errorData = await response.json();
                    Toast.fire({
                        icon: 'error',
                        title: errorData.message || "Failed to change password."
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                Toast.fire({
                    icon: 'error',
                    title: 'Something went wrong!'
                });
            });
        });
    }
});