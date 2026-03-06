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
