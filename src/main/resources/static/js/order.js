document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('orderSearchInput');
    const fragmentContainer = document.getElementById('orders-fragment-container');

    function debounce(func, wait) {
        let timeout;
        return function(...args) {
            const context = this;
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(context, args), wait);
        };
    }

    const performSearch = (query) => {
        fetch(`/orders/search?search=${encodeURIComponent(query)}`)
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.text();
            })
            .then(html => {
                fragmentContainer.innerHTML = html;
            })
            .catch(error => console.error('Error fetching orders:', error));
    };

    if (searchInput) {
        const debouncedSearch = debounce((e) => {
            performSearch(e.target.value);
        }, 300);

        searchInput.addEventListener('input', debouncedSearch);
    }
});