document.addEventListener("DOMContentLoaded", function () {
    const selectEmbarazada = document.getElementById("selectEmbarazada");
    if (!selectEmbarazada) return;

    const inputFecha = document.querySelector('input[name="fechaProbableParto"]');

    function actualizar() {
        const esFemenino = selectEmbarazada.value === "true";
        if (inputFecha) {
            inputFecha.closest("div").style.display = esFemenino ? "block" : "none";
        }
    }

    selectEmbarazada.addEventListener("change", actualizar);
    actualizar();
});
