document.addEventListener("DOMContentLoaded", function() {

    let variantIndex = 1;

    const addVariantBtn = document.getElementById("add-variant-btn");
    const variantsContainer = document.getElementById("variants-container");

    if (addVariantBtn) {
        addVariantBtn.addEventListener("click", function () {
            const template = document.getElementById("variant-template").innerHTML;

            const filledHTML = template.replace(/__index__/g, variantIndex);

            const wrapper = document.createElement("div");
            wrapper.innerHTML = filledHTML;

            variantsContainer.appendChild(wrapper.firstElementChild);

            variantIndex++;
            updateVariantTitles();
        });
    }

    if (variantsContainer) {
        variantsContainer.addEventListener("click", function (e) {
            const removeBtn = e.target.closest(".remove-variant-btn");

            if (removeBtn) {
                removeBtn.closest(".variant-block").remove();
                updateVariantTitles();
            }
        });
    }

    if (variantsContainer) {
        variantsContainer.addEventListener("change", function(e) {
            if (e.target.classList.contains("file-input-actual")) {
                const fileLabel = e.target.nextElementSibling;
                const fileTextSpan = fileLabel.querySelector(".file-text");

                if (e.target.files && e.target.files.length > 0) {
                    fileTextSpan.textContent = e.target.files.length + " file(s) selected";
                    fileTextSpan.style.color = "#000";
                } else {
                    fileTextSpan.textContent = "No Files Selected";
                    fileTextSpan.style.color = "#555";
                }
            }
        });
    }

    function updateVariantTitles() {
        const blocks = document.querySelectorAll(".variant-block");

        blocks.forEach((block, i) => {
            const title = block.querySelector(".variant-title");
            if (title) {
                title.textContent = "Variant Information " + (i + 1);
            }

            const removeBtn = block.querySelector(".remove-variant-btn");
            if (removeBtn) {
                if (blocks.length === 1) {
                    removeBtn.style.display = "none";
                } else {
                    removeBtn.style.display = "block";
                }
            }
        });
    }

    updateVariantTitles();
});