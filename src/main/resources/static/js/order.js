document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('orderSearchInput');
    const fragmentContainer = document.getElementById('orders-fragment-container');

    const updateOrderList = (page = 0, search = '') => {
        const url = `/orders/search?page=${page}&search=${encodeURIComponent(search)}`;

        fetch(url)
            .then(response => response.text())
            .then(html => {
                fragmentContainer.innerHTML = html;
            })
            .catch(error => console.error('Error:', error));
    };

    function debounce(func, wait) {
        let timeout;
        return (...args) => {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), wait);
        };
    }

    if (searchInput) {
        searchInput.addEventListener('input', debounce((e) => {
            updateOrderList(0, e.target.value);
        }, 300));
    }

    fragmentContainer.addEventListener('click', function(e) {
        const pageLink = e.target.closest('.ajax-page-link');

        if (pageLink && !pageLink.parentElement.classList.contains('disabled')) {
            e.preventDefault();
            const page = pageLink.getAttribute('data-page');
            const currentSearch = searchInput ? searchInput.value : '';
            updateOrderList(page, currentSearch);
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    });
});