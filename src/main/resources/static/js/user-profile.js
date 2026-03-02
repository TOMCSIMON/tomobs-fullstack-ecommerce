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
        } else {
            alert("Failed to update. Please try again.");
            cancelEdit(buttonElement);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("Something went wrong!");
        cancelEdit(buttonElement);
    });
}