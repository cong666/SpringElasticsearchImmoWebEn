(function() {
    var mymap = L.map('mapid').setView([51.505, -0.09], 13);
    var mytocken = "pk.eyJ1IjoiY2NiZGVyZCIsImEiOiJjamx1cXpjZ24wa2ttM3NwcGN1NTRnNWlrIn0.xqOd4NrRznVOMvX82-4eRA"
        L.tileLayer("https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token="+mytocken+"", {
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        maxZoom: 18,
        id: 'mapbox.streets',
        accessToken: mytocken
    }).addTo(mymap);
})();