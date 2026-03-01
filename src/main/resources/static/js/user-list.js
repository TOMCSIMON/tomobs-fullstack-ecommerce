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

let currentTargetButton = null;

function openConfirmModal(button) {
    currentTargetButton = button;
    const isCurrentlyBlocked = button.getAttribute('data-blocked') === 'true';

    const actionText = isCurrentlyBlocked ? 'Unblock' : 'Block';
    document.getElementById('modalActionText').innerText = actionText;

    const confirmBtn = document.getElementById('confirmActionBtn');
    confirmBtn.innerText = `Yes, ${actionText}`;

    if(isCurrentlyBlocked) {
        confirmBtn.className = "btn btn-success px-5 py-2 fw-bold";
    } else {
        confirmBtn.className = "btn btn-danger px-5 py-2 fw-bold";
    }

    const myModal = new bootstrap.Modal(document.getElementById('confirmBlockModal'));
    myModal.show();
}

document.getElementById('confirmActionBtn').addEventListener('click', function() {
    if(currentTargetButton) {
        executeToggleBlock(currentTargetButton);
    }
});

function executeToggleBlock(button) {
    const userId = button.getAttribute('data-id');
    const isCurrentlyBlocked = button.getAttribute('data-blocked') === 'true';

    fetch(`/admin/users/toggle-block/${userId}`, {
        method: 'POST'
    })
    .then(response => {
        if (response) {
            const newBlockedStatus = !isCurrentlyBlocked;
            button.setAttribute('data-blocked', newBlockedStatus);
            button.querySelector('span').innerText = newBlockedStatus ? 'Unblock' : 'Block';

            const row = button.closest('tr');
            const statusTd = row.querySelector('.user-status');

            if (newBlockedStatus) {
                statusTd.innerText = 'Blocked';
                statusTd.style.color = '#dc3545';
                statusTd.style.fontWeight = '600';
            } else {
                statusTd.innerText = 'Active';
                statusTd.style.color = '#495057';
                statusTd.style.fontWeight = 'normal';
            }

            const myModalEl = document.getElementById('confirmBlockModal');
            const modal = bootstrap.Modal.getInstance(myModalEl);
            modal.hide();

        } else {
            alert("Something went wrong on the server!");
        }
    })
    .catch(error => console.error('Error:', error));
}