document.addEventListener("DOMContentLoaded", function() {

    const searchInput = document.getElementById('searchInput');
    const clearBtn = document.getElementById('clearSearch');
    let currentPage = 0;

    function debounce(func, delay) {
        let timeoutId;
        return function (...args) {
            clearTimeout(timeoutId);
            timeoutId = setTimeout(() => {
                func.apply(this, args);
            }, delay);
        };
    }

    function toggleClearButton() {
        if (searchInput && clearBtn) {
            clearBtn.style.display = searchInput.value.trim() !== "" ? "block" : "none";
        }
    }

    toggleClearButton();

    const debouncedSearch = debounce(() => {
        currentPage = 0;
        fetchUsers();
    }, 500);

    if (searchInput) {
        searchInput.addEventListener('input', function() {
            toggleClearButton();
            debouncedSearch();
        });

        searchInput.addEventListener("keypress", function (e) {
            if (e.key === "Enter") e.preventDefault();
        });
    }

    if (clearBtn) {
        clearBtn.addEventListener('click', function() {
            searchInput.value = '';
            toggleClearButton();
            currentPage = 0;
            fetchUsers();
        });
    }

    document.addEventListener("click", function (e) {
        const targetLink = e.target.closest("a.page-box");

        if (targetLink) {
            e.preventDefault();
            if (targetLink.classList.contains("disabled")) return;

            const urlParams = new URLSearchParams(targetLink.search);
            const pageStr = urlParams.get('page');

            if (pageStr !== null) {
                currentPage = parseInt(pageStr, 10);
                fetchUsers();
            }
        }
    });

    function fetchUsers() {
        const keyword = searchInput ? searchInput.value.trim() : "";
        const params = new URLSearchParams();

        if (keyword) params.append("keyword", keyword);
        params.append("page", currentPage);

        const newUrl = window.location.pathname + "?" + params.toString();
        window.history.pushState({ path: newUrl }, '', newUrl);

        fetch(`/admin/users?${params.toString()}`, {
            headers: { "X-Requested-With": "XMLHttpRequest" }
        })
        .then(response => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.text();
        })
        .then(html => {
            const tableContainer = document.getElementById("userTableContainer");
            if (tableContainer) {
                tableContainer.outerHTML = html;
            }
        })
        .catch(err => console.error("Error fetching users:", err));
    }
});

let currentTargetButton = null;

window.openConfirmModal = function(button) {
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

const confirmActionBtn = document.getElementById('confirmActionBtn');
if (confirmActionBtn) {
    confirmActionBtn.addEventListener('click', function() {
        if(currentTargetButton) {
            executeToggleBlock(currentTargetButton);
        }
    });
}

function executeToggleBlock(button) {
    const userId = button.getAttribute('data-id');
    const isCurrentlyBlocked = button.getAttribute('data-blocked') === 'true';

    fetch(`/admin/users/toggle-block/${userId}`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {
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
                statusTd.style.color = '#212529';
                statusTd.style.fontWeight = 'normal';
            }

            const myModalEl = document.getElementById('confirmBlockModal');
            const modal = bootstrap.Modal.getInstance(myModalEl);
            if(modal) {
                 modal.hide();
            }

             if (typeof Toast !== 'undefined') {
                Toast.fire({
                    icon: 'success',
                    title: `User successfully ${newBlockedStatus ? 'blocked' : 'unblocked'}`
                });
            }

        } else {
            if (typeof Toast !== 'undefined') {
                Toast.fire({
                    icon: 'error',
                    title: 'Something went wrong!'
                });
            } else {
                alert("Something went wrong!");
            }
        }
    })
    .catch(error => console.error('Error:', error));
}