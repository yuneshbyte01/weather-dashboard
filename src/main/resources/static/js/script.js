document.addEventListener('DOMContentLoaded', function () {
    const currentLocationBtn = document.getElementById('current-location');
    const cityInput = document.getElementById('city-input');
    const form = document.querySelector('.search-form');
    const hourlyContainer = document.querySelector('.hourly-cards');

    if (!currentLocationBtn || !form) return;

    const defaultIcon = `
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20"
             viewBox="0 0 24 24" fill="none" stroke="currentColor"
             stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
            <circle cx="12" cy="10" r="3"></circle>
        </svg>`;

    // 📍 Current Location Fetch
    currentLocationBtn.addEventListener('click', function () {
        if (!navigator.geolocation) {
            showError('Geolocation is not supported by your browser.');
            return;
        }

        setLoading(true);

        navigator.geolocation.getCurrentPosition(
            async function (position) {
                try {
                    const { latitude, longitude } = position.coords;
                    const response = await fetch(
                        `/api/weather/reverse-geocode?lat=${latitude}&lon=${longitude}`
                    );
                    if (response.ok) {
                        const data = await response.json();
                        if (data.city) {
                            cityInput.value = data.city;
                            form.submit();
                            return;
                        }
                    }
                    throw new Error('Could not resolve coordinates to city');
                } catch (error) {
                    console.error('Error:', error);
                    showError('Unable to get your location. Try searching manually.');
                } finally {
                    setLoading(false);
                }
            },
            function (error) {
                console.error('Geolocation error:', error);
                showError('Location access denied. Please enable location services.');
                setLoading(false);
            }
        );
    });

    // ✅ Loading state
    function setLoading(isLoading) {
        currentLocationBtn.disabled = isLoading;
        currentLocationBtn.innerHTML = isLoading
            ? '<div class="spinner"></div>'
            : defaultIcon;
    }

    // ⚠️ Error message
    function showError(message) {
        let status = document.getElementById('location-status');
        if (!status) {
            status = document.createElement('div');
            status.id = 'location-status';
            status.className = 'subtle mt-2';
            status.setAttribute('role', 'alert');
            form.appendChild(status);
        }
        status.textContent = message;
    }

    // 📱 Responsive behavior: hourly forecast scrolls like carousel
    if (hourlyContainer) {
        hourlyContainer.style.scrollSnapType = 'x mandatory';

        Array.from(hourlyContainer.children).forEach((card) => {
            card.style.scrollSnapAlign = 'center';
        });

        // Auto adjust scroll position for small screens
        const checkScreen = () => {
            if (window.innerWidth <= 576) {
                hourlyContainer.style.overflowX = 'auto';
                hourlyContainer.style.paddingBottom = '0.5rem';
            } else {
                hourlyContainer.style.overflowX = 'visible';
            }
        };
        checkScreen();
        window.addEventListener('resize', checkScreen);
    }
});
