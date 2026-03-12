document.addEventListener("DOMContentLoaded", function() {

    const checkboxes = document.querySelectorAll('.filter-checkbox');
    const activeFiltersContainer = document.getElementById('activeFiltersContainer');
    const clearAllBtn = document.getElementById('clearAllFilters');

    let currentPage = 0;

    updateActiveTags();

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            updateActiveTags();

            currentPage = 0;
            filterProducts();
        });
    });

    function updateActiveTags() {
        activeFiltersContainer.innerHTML = '';
        log.info("storage 512 : {}", storages);

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

    activeFiltersContainer.addEventListener('click', function(e) {
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

    if(clearAllBtn) {
        clearAllBtn.addEventListener('click', function(e) {
            e.preventDefault();

            checkboxes.forEach(checkbox => {
                checkbox.checked = false;
            });

            updateActiveTags();
            currentPage = 0;
            filterProducts();
        });
    }

    // 6. Handle Accordion Chevron Rotation
    const filterHeaders = document.querySelectorAll('.filter-header');
    filterHeaders.forEach(header => {
        header.addEventListener('click', function() {
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

    // ==========================================
    // NEW EVENT LISTENERS (SORT & PAGINATION)
    // ==========================================

    const sortItems = document.querySelectorAll('.sort-item');
    sortItems.forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            sortItems.forEach(s => s.classList.remove('active'));
            this.classList.add('active');

            currentPage = 0;
            filterProducts();
        });
    });

    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('ajax-page-link')) {
            e.preventDefault();
            const selectedPage = e.target.getAttribute('data-page');
            if (selectedPage !== null) {
                currentPage = selectedPage;
                filterProducts();
            }
        }
    });

    // ==========================================
    //  THE MAIN AJAX FETCH FUNCTION
    // ==========================================
    function filterProducts() {

        let selectedCategories = [];
        let selectedBrands = [];
        let selectedRams = [];
        let selectedStorages = [];

        let activeSort = document.querySelector('.sort-item.active');
        let sortValue = activeSort ? activeSort.getAttribute('data-sort') : 'Relevance';

        checkboxes.forEach(checkbox => {
            if (checkbox.checked) {
                if (checkbox.id.startsWith('category-')) {
                    selectedCategories.push(checkbox.value);
                } else if (checkbox.id.startsWith('brand-')) {
                    selectedBrands.push(checkbox.value);
                } else if (checkbox.id.startsWith('ram-')) {
                    selectedRams.push(checkbox.value);
                } else if (checkbox.id.startsWith('storage-')) {
                    selectedStorages.push(checkbox.value);
                }
            }
        });

        const queryParams = new URLSearchParams();

        if (selectedCategories.length > 0) queryParams.append('categories', selectedCategories.join(','));
        if (selectedBrands.length > 0) queryParams.append('brands', selectedBrands.join(','));
        if (selectedRams.length > 0) queryParams.append('rams', selectedRams.join(','));
        if (selectedStorages.length > 0) queryParams.append('storages', selectedStorages.join(','));

        queryParams.append('sort', sortValue);
                                                                                                                                                                                                                queryParams.append('page', currentPage);

        fetch('/products/filter?' + queryParams.toString())
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(htmlFragment => {

                document.getElementById('product-grid-container').innerHTML = htmlFragment;
            })
            .catch(error => console.error('Error fetching filtered products:', error));
    }

});