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

function toggleBlock(button) {

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
        } else {
            alert("Something went wrong on the server!");
        }
    })
    .catch(error => console.error('Error:', error));
}