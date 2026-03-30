document.addEventListener("DOMContentLoaded", function() {
    const ctx = document.getElementById('salesChart').getContext('2d');

    // Smooth Curves with Tension
    new Chart(ctx, {
        type: 'line',
        data: {
            // These labels can be bound dynamically from Backend JSON
            labels: ['Feb 10', 'Feb 11', 'Feb 12', 'Feb 13', 'Feb 14', 'Feb 15', 'Feb 16'],
            datasets: [{
                label: 'Daily Revenue (₹)',
                data: [12000, 19000, 15000, 25000, 22000, 30000, 28000],
                borderColor: '#212121', // Dark Theme
                backgroundColor: 'rgba(33, 33, 33, 0.05)',
                borderWidth: 3, // Slightly thicker line
                fill: true,
                tension: 0.4, // Making the line curvy
                pointBackgroundColor: '#fff', // White point background for contrast
                pointBorderColor: '#212121',
                pointBorderWidth: 2,
                pointRadius: 5,
                pointHoverRadius: 7
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: '#000',
                    titleFont: { family: 'Poppins', size: 14 },
                    bodyFont: { family: 'Poppins', size: 12 },
                    padding: 12,
                    cornerRadius: 8,
                    displayColors: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { borderDash: [5, 5], color: '#eaeaea' },
                    ticks: {
                        font: { family: 'Poppins', size: 12 },
                        callback: function(value) {
                            return '₹' + value.toLocaleString(); // Adding currency format
                        }
                    }
                },
                x: {
                    grid: { display: false },
                    ticks: { font: { family: 'Poppins', size: 12 } }
                }
            }
        }
    });
});