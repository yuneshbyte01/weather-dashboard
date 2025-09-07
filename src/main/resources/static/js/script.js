document.addEventListener('DOMContentLoaded', function() {
    const currentLocationBtn = document.getElementById('current-location');
    const cityInput = document.getElementById('city-input');

    if (currentLocationBtn) {
        currentLocationBtn.addEventListener('click', function() {
            if (!navigator.geolocation) {
                alert('Geolocation is not supported by your browser');
                return;
            }

            currentLocationBtn.disabled = true;
            currentLocationBtn.innerHTML = '<div class="spinner"></div>';

            navigator.geolocation.getCurrentPosition(
                async function(position) {
                    try {
                        const response = await fetch(`/api/weather/reverse-geocode?lat=${position.coords.latitude}&lon=${position.coords.longitude}`);
                        if (response.ok) {
                            const data = await response.json();
                            if (data.city) {
                                cityInput.value = data.city;
                                document.querySelector('.search-form').submit();
                                return;
                            }
                        }
                        throw new Error('Could not get location name');
                    } catch (error) {
                        console.error('Error getting location:', error);
                        alert('Could not get your location. Please try searching manually.');
                    } finally {
                        currentLocationBtn.disabled = false;
                        currentLocationBtn.innerHTML = `
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" 
                                     viewBox="0 0 24 24" fill="none" stroke="currentColor" 
                                     stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                                    <circle cx="12" cy="10" r="3"></circle>
                                </svg>
                            `;
                    }
                },
                function(error) {
                    console.error('Geolocation error:', error);
                    alert('Unable to retrieve your location. Please enable location services and try again.');
                    currentLocationBtn.disabled = false;
                    currentLocationBtn.innerHTML = `
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" 
                                 viewBox="0 0 24 24" fill="none" stroke="currentColor" 
                                 stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                                <circle cx="12" cy="10" r="3"></circle>
                            </svg>
                        `;
                }
            );
        });
    }
});