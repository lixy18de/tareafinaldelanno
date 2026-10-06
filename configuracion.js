// Confirmacion simple antes de deshabilitar/habilitar un usuario
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll("form button[type='submit']").forEach(function (btn) {
        btn.addEventListener("click", function (e) {
            const accion = btn.textContent.trim();
            if (accion === "Deshabilitar" || accion === "Habilitar") {
                if (!confirm("¿Seguro que deseas " + accion.toLowerCase() + " este usuario?")) {
                    e.preventDefault();
                }
            }
        });
    });
});
