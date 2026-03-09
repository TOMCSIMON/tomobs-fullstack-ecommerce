document.addEventListener("DOMContentLoaded", function() {

    let variantIndex = 1;

    let cropper = null;
    let currentFileInput = null;
    let currentVariantBlock = null;


    const variantFilesTracker = new Map();

    const addVariantBtn = document.getElementById("add-variant-btn");
    const variantsContainer = document.getElementById("variants-container");

    // ==========================================
    //  ADD NEW VARIANT
    // ==========================================
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

    // ==========================================
    // REMOVE VARIANT
    // ==========================================
    if (variantsContainer) {
        variantsContainer.addEventListener("click", function (e) {
            const removeBtn = e.target.closest(".remove-variant-btn");

            if (removeBtn) {
                const block = removeBtn.closest(".variant-block");
                const vIndex = block.getAttribute('data-index');

                variantFilesTracker.delete(vIndex);

                block.remove();
                updateVariantTitles();
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

    // ==========================================
    // IMAGE SELECTION & CROPPER MODAL
    // ==========================================
    if (variantsContainer) {
        variantsContainer.addEventListener("change", function(e) {

            if (e.target.classList.contains("file-input-actual")) {
                const files = e.target.files;

                if (files && files.length > 0) {
                    currentFileInput = e.target;
                    currentVariantBlock = e.target.closest('.variant-block');

                    const file = files[0];

                    const reader = new FileReader();
                    reader.onload = function(event) {
                        const imageToCrop = document.getElementById('imageToCrop');
                        imageToCrop.src = event.target.result;

                        const modal = new bootstrap.Modal(document.getElementById('cropperModal'));
                        modal.show();

                        document.getElementById('cropperModal').addEventListener('shown.bs.modal', function () {
                            if (cropper) {
                                cropper.destroy();
                            }
                            cropper = new Cropper(imageToCrop, {
                                aspectRatio: NaN,
                                viewMode: 1,
                                autoCropArea: 1,
                            });
                        }, { once: true });
                    };
                    reader.readAsDataURL(file);
                }
            }
        });
    }

    // ==========================================
    // CROP & SAVE BUTTON
    // ==========================================
    document.getElementById('cropAndSaveBtn').addEventListener('click', function() {
        if (!cropper) return;

        cropper.getCroppedCanvas().toBlob(function(blob) {

            const fileName = `cropped_${Date.now()}.png`;
            const croppedFile = new File([blob], fileName, { type: 'image/png' });

            const vIndex = currentVariantBlock.getAttribute('data-index');

            if (!variantFilesTracker.has(vIndex)) {
                variantFilesTracker.set(vIndex, new DataTransfer());
            }

            const dt = variantFilesTracker.get(vIndex);
            dt.items.add(croppedFile);

            currentFileInput.files = dt.files;

            updatePreviews(currentVariantBlock, dt, currentFileInput);

            const modalEl = document.getElementById('cropperModal');
            const modal = bootstrap.Modal.getInstance(modalEl);
            modal.hide();

        }, 'image/png');
    });

    // ==========================================
    // RENDER PREVIEWS & UPDATE LABEL
    // ==========================================
    function updatePreviews(variantBlock, dt, inputElement) {
        const previewContainer = variantBlock.querySelector('.image-preview-container');
        previewContainer.innerHTML = '';

        Array.from(dt.files).forEach((file, index) => {
            const reader = new FileReader();
            reader.onload = function(e) {
                const div = document.createElement('div');
                div.className = 'preview-image-wrapper';
                div.innerHTML = `
                    <img src="${e.target.result}" class="preview-image">
                    <button type="button" class="delete-img-btn" data-index="${index}">&times;</button>
                `;
                previewContainer.appendChild(div);
            }
            reader.readAsDataURL(file);
        });

        const fileLabel = inputElement.nextElementSibling.querySelector('.file-text');
        if (dt.files.length > 0) {
            fileLabel.textContent = dt.files.length + " file(s) selected";
            fileLabel.style.color = "#000";
        } else {
            fileLabel.textContent = "Click to add an image";
            fileLabel.style.color = "#555";
        }
    }

    // ==========================================
    // DELETE SPECIFIC IMAGE
    // ==========================================
    if (variantsContainer) {
        variantsContainer.addEventListener('click', function(e) {
            if (e.target.classList.contains('delete-img-btn')) {
                const btn = e.target;
                const indexToRemove = parseInt(btn.getAttribute('data-index'));
                const variantBlock = btn.closest('.variant-block');
                const vIndex = variantBlock.getAttribute('data-index');
                const inputElement = variantBlock.querySelector('.file-input-actual');

                const dt = variantFilesTracker.get(vIndex);

                if (dt) {
                    const newDt = new DataTransfer();

                    Array.from(dt.files).forEach((file, i) => {
                        if (i !== indexToRemove) {
                            newDt.items.add(file);
                        }
                    });

                    variantFilesTracker.set(vIndex, newDt);
                    inputElement.files = newDt.files;

                    updatePreviews(variantBlock, newDt, inputElement);
                }
            }
        });
    }
});