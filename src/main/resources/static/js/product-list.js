document.addEventListener("DOMContentLoaded", function () {

    const checkboxes = document.querySelectorAll('.filter-checkbox');
    const activeFiltersContainer = document.getElementById('activeFiltersContainer');
    const clearAllBtn = document.getElementById('clearAllFilters');

    // --- SEARCH ELEMENTS ---
    const searchInput = document.getElementById('search-input');
    const clearBtn = document.getElementById('clear-search');
    const searchIcon = document.querySelector('.search-img-icon');

    let currentPage = 0;

    updateActiveTags();

    // --- SEARCH EVENT LISTENERS ---
    if (searchInput) {
        // 1. Listen for the 'Enter' key
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                currentPage = 0; // Reset page on new search
                filterProducts();
            }
        });

        // 2. Listen for clicks on the search image icon
        if (searchIcon) {
            searchIcon.addEventListener('click', function() {
                currentPage = 0;
                filterProducts();
            });
        }

        // 3. Live search (Debouncing)
        let debounceTimer;
        searchInput.addEventListener('input', function() {
            if (clearBtn) clearBtn.style.display = this.value ? 'block' : 'none';
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                currentPage = 0;
                filterProducts();
            }, 500);
        });

        // 4. Clear search button functionality
        if (clearBtn) {
            clearBtn.addEventListener('click', function() {
                searchInput.value = '';
                this.style.display = 'none';
                currentPage = 0;
                filterProducts();
            });
            // Initial setup for clear button
            clearBtn.style.display = searchInput.value ? 'block' : 'none';
        }
    }
    // ------------------------------

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            updateActiveTags();
            currentPage = 0;
            filterProducts();
        });
    });

    function updateActiveTags() {
        activeFiltersContainer.innerHTML = '';
        checkboxes.forEach(checkbox => {
            if (checkbox.checked) {
                const displayName = checkbox.getAttribute('data-name');
                const checkboxId = checkbox.id;

                const tag = document.createElement('span');
                tag.className = 'filter-tag';
                tag.innerHTML = `${displayName} <i class="bi bi-x ms-1" data-target="${checkboxId}"></i>`;

                activeFiltersContainer.appendChild(tag);
            }
        });
    }

    activeFiltersContainer.addEventListener('click', function (e) {
        if (e.target.classList.contains('bi-x')) {
            const targetCheckboxId = e.target.getAttribute('data-target');
            const targetCheckbox = document.getElementById(targetCheckboxId);

            if (targetCheckbox) {
                targetCheckbox.checked = false;
                updateActiveTags();
                currentPage = 0;
                filterProducts();
            }
        }
    });

    if (clearAllBtn) {
        clearAllBtn.addEventListener('click', function (e) {
            e.preventDefault();
            checkboxes.forEach(checkbox => {
                checkbox.checked = false;
            });
            updateActiveTags();
            currentPage = 0;
            filterProducts();
        });
    }

    const filterHeaders = document.querySelectorAll('.filter-header');
    filterHeaders.forEach(header => {
        header.addEventListener('click', function () {
            const icon = this.querySelector('.toggle-icon');
            if (icon.classList.contains('bi-chevron-up')) {
                icon.classList.remove('bi-chevron-up');
                icon.classList.add('bi-chevron-down');
            } else {
                icon.classList.remove('bi-chevron-down');
                icon.classList.add('bi-chevron-up');
            }
        });
    });

    const sortItems = document.querySelectorAll('.sort-item');
    sortItems.forEach(item => {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            const clickedSortValue = this.getAttribute('data-sort');

            sortItems.forEach(s => {
                if (s.getAttribute('data-sort') === clickedSortValue) {
                    s.classList.add('active');
                } else {
                    s.classList.remove('active');
                }
            });

            currentPage = 0;
            filterProducts();
        });
    });

    // Handle Wishlist & Pagination Clicks
    document.addEventListener('click', function (e) {
        const wishlistBtn = e.target.closest('.add-to-wishlist-btn');
        if (wishlistBtn) {
            e.preventDefault();
            const variantId = wishlistBtn.getAttribute('data-variant-id');
            const icon = wishlistBtn.querySelector('i');

            if (!variantId) return;

            const formData = new URLSearchParams();
            formData.append('variantId', variantId);
            fetch('/wishlist/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData.toString()
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Network response was not ok');
                }
            })
            .then(data => {
                if (data && data.success) {
                    icon.classList.remove('bi-heart', 'text-secondary');
                    icon.classList.add('bi-heart-fill', 'text-danger');
                    Toast.fire({
                        icon: 'success',
                        title: 'item added to wishlist'
                    });
                }
            })
            .catch(error => console.error('Error adding to wishlist:', error));
        }

        if (e.target.classList.contains('ajax-page-link')) {
            e.preventDefault();
            const selectedPage = e.target.getAttribute('data-page');
            if (selectedPage !== null) {
                currentPage = selectedPage;
                filterProducts();
            }
        }
    });

    // --- MAIN FETCH FUNCTION ---
    function filterProducts() {
        let selectedCategories = [];
        let selectedBrands = [];
        let selectedRams = [];
        let selectedStorages = [];

        let activeSort = document.querySelector('.sort-item.active');
        let sortValue = activeSort ? activeSort.getAttribute('data-sort') : 'Relevance';

        checkboxes.forEach(checkbox => {
            if (checkbox.checked) {
                if (checkbox.id.startsWith('category-')) selectedCategories.push(checkbox.value);
                else if (checkbox.id.startsWith('brand-')) selectedBrands.push(checkbox.value);
                else if (checkbox.id.startsWith('ram-')) selectedRams.push(checkbox.value);
                else if (checkbox.id.startsWith('storage-')) selectedStorages.push(checkbox.value);
            }
        });

        const queryParams = new URLSearchParams();

        // --- NEW: Grab the search value ---
        if (searchInput && searchInput.value.trim() !== '') {
            queryParams.append('search', searchInput.value.trim());
        }

        // Append the rest of the arrays
        if (selectedCategories.length > 0) queryParams.append('categories', selectedCategories.join(','));
        if (selectedBrands.length > 0) queryParams.append('brands', selectedBrands.join(','));
        if (selectedRams.length > 0) queryParams.append('rams', selectedRams.join(','));
        if (selectedStorages.length > 0) queryParams.append('storages', selectedStorages.join(','));

        queryParams.append('sort', sortValue);
        queryParams.append('page', currentPage);
        // queryParams.append('size', 6); // Optional: append size if you want to explicitly control it from JS

        // Execute the single fetch call
        fetch('/products/filter?' + queryParams.toString(), {
            headers: {
                'X-Requested-With': 'XMLHttpRequest' // Helps Spring distinguish AJAX from full page loads
            }
        })
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.text();
            })
            .then(htmlFragment => {
                const container = document.getElementById('product-grid-container');
                if (container) {
                    container.innerHTML = htmlFragment;
                }
            })
            .catch(error => console.error('Error fetching filtered products:', error));
    }
});