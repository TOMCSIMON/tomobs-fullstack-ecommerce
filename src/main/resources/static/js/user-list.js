document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.getElementById('searchInput');
    const clearBtn = document.getElementById('clearSearch');
    const searchForm = document.getElementById('searchForm');

    // Function to toggle the visibility of the close button
    function toggleClearButton() {
        if (searchInput.value.length > 0) {
            clearBtn.style.display = 'block';
        } else {
            clearBtn.style.display = 'none';
        }
    }

    // Check on initial load (in case there's an existing search keyword)
    toggleClearButton();

    // Check while user is typing
    searchInput.addEventListener('input', toggleClearButton);

    // Action when 'Clear' is clicked
    clearBtn.addEventListener('click', function() {
        searchInput.value = ''; // Clear the input
        toggleClearButton(); // Hide the button
        searchForm.submit(); // Automatically submit form to load all users
    });
});