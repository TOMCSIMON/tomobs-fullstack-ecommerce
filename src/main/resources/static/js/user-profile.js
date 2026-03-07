
function startEdit(iconElement) {
    const group = iconElement.closest('.field-group');
    const inputField = group.querySelector('.profile-input');
    const actionButtons = group.querySelector('.action-buttons');

    inputField.setAttribute('data-original-value', inputField.value);

    inputField.removeAttribute('readonly');
    inputField.focus();
    inputField.classList.add('editing');

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
    inputField.classList.remove('editing');

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

    fetch('/profile/update', {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ field: fieldName, value: newValue })
    })
    .then(response => {
        if (response.ok) {
            inputField.setAttribute('data-original-value', newValue);
            inputField.setAttribute('readonly', true);
            inputField.classList.remove('editing');
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
function setupPasswordToggle(toggleId, inputId, hideIcon, viewIcon) {
    const toggle = document.getElementById(toggleId);
    const input = document.getElementById(inputId);

    if (toggle && input) {
        toggle.addEventListener("click", () => {
            const type = input.type === "password" ? "text" : "password";
            input.type = type;
            toggle.src = type === "password" ? hideIcon : viewIcon;
        });
    }
}

document.addEventListener('DOMContentLoaded', function() {

    setupPasswordToggle("toggleOldPassword", "oldPassword", "/icons/hide.png", "/icons/view.png");
    setupPasswordToggle("toggleNewPassword", "newPassword", "/icons/hide.png", "/icons/view.png");
    setupPasswordToggle("toggleConfirmNewPassword", "confirmNewPassword", "/icons/hide.png", "/icons/view.png");

    const passwordForm = document.getElementById('changePasswordForm');

    if (passwordForm) {
        const strongPasswordRegex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$/;

        passwordForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const oldPassword = document.getElementById('oldPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmNewPassword = document.getElementById('confirmNewPassword').value;

            const oldError = document.getElementById('oldPasswordError');
            const newError = document.getElementById('newPasswordError');
            const confirmError = document.getElementById('confirmNewPasswordError');

            oldError.textContent = "";
            newError.textContent = "";
            confirmError.textContent = "";

            let isValid = true;

            if (!oldPassword) {
                oldError.textContent = "Please enter your current password.";
                isValid = false;
            }

            if (!strongPasswordRegex.test(newPassword)) {
                newError.textContent = "Must contain at least 8 chars, 1 uppercase, 1 lowercase, 1 number & 1 special char.";
                isValid = false;
            } else if (oldPassword && oldPassword === newPassword) {
                newError.textContent = "New password cannot be the same as the old password.";
                isValid = false;
            }

            if (newPassword !== confirmNewPassword) {
                confirmError.textContent = "Passwords do not match.";
                isValid = false;
            }

            if (!isValid) return;

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

                    if (errorData.message && errorData.message.toLowerCase().includes('old password')) {
                        oldError.textContent = "Incorrect old password. Please try again.";
                    } else {
                        Toast.fire({
                            icon: 'error',
                            title: errorData.message || "Failed to change password."
                        });
                    }
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